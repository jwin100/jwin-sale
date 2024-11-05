package com.mammon.cashier.service;

import com.mammon.cashier.domain.dto.CashierRefundDto;
import com.mammon.cashier.domain.dto.CashierRefundPayDto;
import com.mammon.cashier.domain.dto.CashierRefundSkuDto;
import com.mammon.cashier.domain.entity.*;
import com.mammon.cashier.domain.enums.*;
import com.mammon.cashier.domain.model.CashierPayTypeModel;
import com.mammon.cashier.domain.model.CashierRefundPayTypeModel;
import com.mammon.cashier.domain.vo.*;
import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.leaf.enums.DocketType;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.member.domain.entity.MemberAttrEntity;
import com.mammon.member.service.MemberAssetsService;
import com.mammon.member.service.MemberAttrService;
import com.mammon.member.service.MemberTimeCardService;
import com.mammon.payment.domain.enums.PayModeConst;
import com.mammon.sms.service.SmsSendNoticeService;
import com.mammon.stock.service.StockSkuService;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import com.mammon.utils.StockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * * 退款逻辑
 * <p>
 * 1.创建退款单
 * <p>
 * 2.异步提交到第三方今天退款，修改退款状态为已提交
 * <p>
 * 3.异步退款执行，并且都退款成功后返回退款订单退款完成
 * <p>
 * 4.失败后进行重试，包括手动介入，直到成功。退款没有撤销逻辑
 *
 * @author dcl
 * @since 2024/6/3 15:03
 */
@Service
@Slf4j
public class CashierRefundTradeService {

    @Resource
    private CashierOrderService cashierOrderService;

    @Resource
    private CashierOrderProductService cashierOrderProductService;

    @Resource
    private CashierOrderPayService cashierOrderPayService;

    @Resource
    private MemberAttrService memberAttrService;

    @Resource
    private LeafCodeService leafCodeService;

    @Resource
    private CashierRefundService cashierRefundService;

    @Resource
    private CashierRefundProductService cashierRefundProductService;

    @Resource
    private CashierRefundPayService cashierRefundPayService;

    @Resource
    private CashierRefundTradeAsyncService cashierRefundTradeAsyncService;

    @Resource
    private MemberTimeCardService memberTimeCardService;

    @Resource
    private MemberAssetsService memberAssetsService;

    @Resource
    private SmsSendNoticeService smsSendNoticeService;

    @Resource
    private StockSkuService stockSkuService;

    /**
     * 退货金额核算
     *
     * @param dto
     * @return
     */
    public CashierRefundComputeVo compute(CashierRefundDto dto) {
        CashierOrderDetailVo order = cashierOrderService.findDetailById(dto.getOrderId());
        validCashierRefund(order);
        List<CashierRefundSkuComputeVo> skus = refundProductCompute(order.getId(), order.getType(), dto.getSkus());

        BigDecimal originalAmount = AmountUtil.parseBigDecimal(skus.stream().mapToLong(x -> AmountUtil.parse(x.getPayableAmount())).sum());
        BigDecimal adjustAmount = BigDecimal.ZERO;
        BigDecimal payableAmount = originalAmount;
        boolean allRefund = skus.stream().allMatch(CashierRefundSkuComputeVo::isAllRefund);
        if (dto.getAdjustAmount() != null && dto.getAdjustAmount().compareTo(BigDecimal.ZERO) > 0) {
            adjustAmount = dto.getAdjustAmount();
            payableAmount = adjustAmount;
        }
        // 计算退款积分
        long refundIntegral = 0;
        if (order.getSkus().size() == skus.size() && allRefund) {
            // 如果为全退，积分=订单总积分-所有已退积分
            refundIntegral = order.getIntegral() - order.getRefundIntegral();
        } else {
            // 如果不为全退，积分=本次退款金额换算积分
            refundIntegral = divideIntegral(order.getMerchantNo(), order.getMemberId(),
                    adjustAmount, payableAmount);
        }

        CashierRefundComputeVo vo = new CashierRefundComputeVo();
        vo.setOriginalAmount(originalAmount);
        vo.setAdjustAmount(adjustAmount);
        vo.setPayableAmount(payableAmount);
        vo.setRefundIntegral(refundIntegral);
        vo.setSkus(skus);
        return vo;
    }

    /**
     * 创建退货单
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public String createRefund(long merchantNo, long storeNo, String accountId, CashierRefundDto dto) {
        String refundId = leafCodeService.generateDocketNo(DocketType.CASHIER_REFUND);
        String refundNo = refundId;
        if (StringUtils.isBlank(refundNo)) {
            throw new CustomException("退货单号创建错误，请重试");
        }

        CashierOrderDetailVo order = cashierOrderService.findDetailById(dto.getOrderId());
        // 订单验证是否可退款
        validCashierRefund(order);
        // 退款核算
        CashierRefundComputeVo computeVo = compute(dto);

        // 初始化保存退款商品
        List<CashierRefundProductEntity> products = cashierRefundProductService.initRefundProduct(order.getId(),
                refundId, computeVo.getSkus());
        if (CollectionUtils.isEmpty(products)) {
            throw new CustomException("订单中商品错误");
        }
        CashierRefundEntity entity = new CashierRefundEntity();
        entity.setId(refundId);
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setRefundNo(refundNo);
        entity.setOrderId(order.getId());
        entity.setOrderNo(order.getOrderNo());
        entity.setType(order.getType());
        entity.setCategory(order.getCategory());
        entity.setSubject(order.getSubject());
        entity.setOriginalAmount(AmountUtil.parse(computeVo.getOriginalAmount()));
        entity.setAdjustAmount(AmountUtil.parse(computeVo.getAdjustAmount()));
        entity.setPayableAmount(AmountUtil.parse(computeVo.getPayableAmount()));
        entity.setCountedTotal(dto.getCountedTotal());
        entity.setIntegral(computeVo.getRefundIntegral());
        entity.setStatus(CashierRefundStatus.REFUND_ING.getCode());
        entity.setMemberId(order.getMemberId());
        entity.setOperationId(accountId);
        entity.setRemark(dto.getRemark());
        entity.setCreateTime(LocalDateTime.now());
        cashierRefundService.save(entity);
        return refundId;
    }

    /**
     * 退款
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void refundPay(long merchantNo, long storeNo, String accountId, CashierRefundPayDto dto) {
        CashierRefundEntity refund = cashierRefundService.findBaseById(dto.getRefundId());
        if (refund == null) {
            throw new CustomException("退单信息错误");
        }
        CashierOrderVo order = cashierOrderService.findById(refund.getOrderId());
        validCashierRefund(order);
        // 如果为原路退回，组织退款方式
        List<CashierRefundPayTypeModel> payTypeModels = convertRefundPayType(dto, order.getPayType());

        List<CashierRefundPayEntity> refundPays = cashierRefundPayService.save(refund.getId(), payTypeModels);
        // 修改退款订单退款方式信息
        updateRefundPayType(refund.getId(), dto.getRefundMode(), refundPays);

        // 修改退款已提交
        cashierRefundService.refundSubmit(refund.getId());
        // 异步调用退款
        cashierRefundTradeAsyncService.tradeRefund(accountId, refund, refundPays);
        refundPaySuccess(merchantNo, storeNo, accountId, dto.getRefundId());
    }

    public void validCashierRefund(CashierOrderVo order) {
        if (order == null) {
            throw new CustomException("销售单错误");
        }
        if (order.getStatus() == CashierOrderStatus.CLOSE.getCode()) {
            throw new CustomException("订单已关闭,不能退款");
        }
        if (order.getStatus() != CashierOrderStatus.FINISH.getCode()) {
            throw new CustomException("订单未支付成功，不能退款");
        }
    }

    /**
     * 退款商品金额核算
     *
     * @param orderId 订单ID
     * @param type    订单类型
     * @param skus    退款商品信息列表
     * @return 退款商品计算信息列表
     * @throws CustomException 获取退换商品信息异常、退货异常：商品销售信息不存在、商品退款数量不能大于可退数量
     */
    private List<CashierRefundSkuComputeVo> refundProductCompute(String orderId, int type,
                                                                 List<CashierRefundSkuDto> skus) {
        List<CashierOrderProductEntity> orderProducts = cashierOrderProductService.findAllByOrderId(orderId);
        if (orderProducts.isEmpty()) {
            throw new CustomException("获取退换商品信息异常");
        }
        return skus.stream().map(x -> {
            CashierOrderProductEntity orderProduct = orderProducts.stream()
                    .filter(y -> y.getId().equals(x.getId()))
                    .findFirst().orElse(null);
            if (orderProduct == null) {
                //检查退货信息是否在此销售单中
                throw new CustomException("退货异常：商品销售信息不存在");
            }
            // 销售数量
            BigDecimal saleQuantity = StockUtil.parseBigDecimal(orderProduct.getSaleQuantity());
            // 已退数量
            BigDecimal saleRefundQuantity = StockUtil.parseBigDecimal(orderProduct.getRefundQuantity());
            // 剩余可退数量
            BigDecimal sureRefundQuantity = saleQuantity.subtract(saleRefundQuantity);
            // 本次退货数量
            BigDecimal refundQuantity = x.getQuantity();
            // 订单付款金额
            BigDecimal orderPayableAmount = AmountUtil.parseBigDecimal(orderProduct.getPayableAmount());

            if (sureRefundQuantity.compareTo(BigDecimal.ZERO) <= 0) {
                throw new CustomException("商品退款数量不能大于可退数量");
            }
            if (orderProduct.getDivideAmount() > 0) {
                // 判断是否有分摊金额
                orderPayableAmount = AmountUtil.parseBigDecimal(orderProduct.getDivideAmount());
            }
            // 计算单价
            BigDecimal referenceAmount = AmountUtil.divide(orderPayableAmount, saleQuantity);
            if (x.getAdjustAmount() != null && x.getAdjustAmount().compareTo(BigDecimal.ZERO) > 0) {
                referenceAmount = x.getAdjustAmount();
            }

            BigDecimal payableAmount = AmountUtil.multiply(referenceAmount, refundQuantity);
            boolean allRefund = false;
            if (sureRefundQuantity.equals(x.getQuantity())) {
                allRefund = true;
                payableAmount = AmountUtil.parseBigDecimal(orderProduct.getPayableAmount() - orderProduct.getRefundAmount());
                if (x.getAdjustAmount() != null && x.getAdjustAmount().compareTo(BigDecimal.ZERO) > 0) {
                    payableAmount = AmountUtil.multiply(referenceAmount, refundQuantity);
                }
                if (orderProduct.getDivideAmount() > 0) {
                    payableAmount = AmountUtil.parseBigDecimal(orderProduct.getDivideAmount() - orderProduct.getRefundAmount());
                }
            }
            if (type == CashierOrderType.COUNTED.getCode()) {
                CashierRefundSkuComputeVo vo = new CashierRefundSkuComputeVo();
                vo.setId(x.getId());
                vo.setSkuId(x.getSkuId());
                vo.setSkuName(orderProduct.getSkuName());
                vo.setQuantity(x.getQuantity());
                vo.setReferenceAmount(BigDecimal.ZERO);
                vo.setAdjustAmount(BigDecimal.ZERO);
                vo.setPayableAmount(BigDecimal.ZERO);
                vo.setAllRefund(allRefund);
                return vo;
            }
            CashierRefundSkuComputeVo vo = new CashierRefundSkuComputeVo();
            vo.setId(x.getId());
            vo.setSkuId(x.getSkuId());
            vo.setSkuName(orderProduct.getSkuName());
            vo.setQuantity(x.getQuantity());
            vo.setReferenceAmount(referenceAmount);
            vo.setAdjustAmount(x.getAdjustAmount());
            vo.setPayableAmount(payableAmount);
            vo.setAllRefund(allRefund);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 验证销售单是否可退款
     *
     * @param order 销售单信息
     * @throws CustomException 如果销售单为空，订单已关闭或订单未支付成功，则抛出异常
     */
    private void validCashierRefund(CashierOrderDetailVo order) {
        if (order == null) {
            throw new CustomException("销售单错误");
        }
        if (order.getStatus() == CashierOrderStatus.CLOSE.getCode()) {
            throw new CustomException("订单已关闭,不能退款");
        }
        if (order.getStatus() != CashierOrderStatus.FINISH.getCode()) {
            throw new CustomException("订单未支付成功，不能退款");
        }
    }

    /**
     * 计算退款积分
     *
     * @param merchantNo    商户编号
     * @param memberId      会员ID
     * @param adjustAmount  调整金额
     * @param payableAmount 应支付金额
     * @return 积分值
     */
    private long divideIntegral(long merchantNo, String memberId, BigDecimal adjustAmount, BigDecimal payableAmount) {
        if (StringUtils.isBlank(memberId)) {
            return 0;
        }
        if (adjustAmount == null && payableAmount == null) {
            return 0;
        }
        long payAmount = payableAmount.longValue();
        if (adjustAmount != null && adjustAmount.compareTo(BigDecimal.ZERO) > 0) {
            payAmount = adjustAmount.longValue();
        }
        MemberAttrEntity memberAttr = memberAttrService.findByMerchantNo(merchantNo);
        if (memberAttr == null || memberAttr.getConvertAmount() == 0) {
            return 0;
        }
        long amount = payAmount / memberAttr.getConvertAmount();
        return amount * memberAttr.getConvertIntegral();
    }

    /**
     * 原路退回
     */
    private List<CashierRefundPayTypeModel> convertRefundPayType(CashierRefundPayDto dto, String payType) {
        // 如果是原路退回，就一定是整单退款
        if (dto.getRefundMode() == 0) {
            // 判断是原路退回
            List<CashierPayTypeModel> models = JsonUtil.toList(payType, CashierPayTypeModel.class);
            List<String> orderPayIds = models.stream().map(CashierPayTypeModel::getPayId).collect(Collectors.toList());
            List<CashierOrderPayEntity> orderPays = cashierOrderPayService.findAllByIds(orderPayIds);
            return orderPays.stream().map(x -> {
                CashierRefundPayTypeModel payTypeDto = new CashierRefundPayTypeModel();
                payTypeDto.setPayCode(x.getPayCode());
                payTypeDto.setRefundAmount(AmountUtil.parseBigDecimal(x.getPayableAmount()));
                payTypeDto.setCountedId(x.getCountedId());
                payTypeDto.setCountedTotal(x.getCountedTotal());
                return payTypeDto;
            }).collect(Collectors.toList());
        }
        return dto.getPayTypes().stream().map(x -> {
            CashierRefundPayTypeModel payTypeDto = new CashierRefundPayTypeModel();
            payTypeDto.setPayCode(x.getPayCode());
            payTypeDto.setRefundAmount(x.getRefundAmount());
            return payTypeDto;
        }).collect(Collectors.toList());
    }

    /**
     * 更新退款支付方式
     *
     * @param refundId   退款ID
     * @param refundMode 退款模式
     * @param refundPays 退款支付实体列表
     */
    private void updateRefundPayType(String refundId, int refundMode, List<CashierRefundPayEntity> refundPays) {
        List<CashierPayTypeModel> payTypes = refundPays.stream().map(x -> {
            CashierPayTypeModel model = new CashierPayTypeModel();
            model.setPayId(x.getId());
            model.setPayCode(x.getPayCode());
            return model;
        }).collect(Collectors.toList());
        cashierRefundService.updateRefundPayType(refundId, refundMode, JsonUtil.toJSONString(payTypes));
    }

    /**
     * 退款支付成功回调
     *
     * @param merchantNo 商户编号
     * @param storeNo    门店编号
     * @param accountId  会员账号ID
     * @param refundId   退款ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void refundPaySuccess(long merchantNo, long storeNo, String accountId, String refundId) {
        CashierRefundDetailVo refund = cashierRefundService.findById(refundId);
        if (refund == null) {
            return;
        }
        CashierOrderDetailVo order = cashierOrderService.findDetailById(refund.getOrderId());
        // 退积分、库存
        refundUnActive(order, refund);
        // 同步修改对应销售订单中退款信息
        updateOrderRefund(merchantNo, storeNo, refund.getOrderId());
        refundFinish(refundId);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void refundFinish(String refundId) {
        // 异步调用退款短信通知和打印
        CashierRefundDetailVo refund = cashierRefundService.findById(refundId);
        if (refund == null) {
            return;
        }
        // 退成功执行
        if (refund.getPays().stream().anyMatch(x -> x.getPayCode() == PayModeConst.payModeTimeCard.getCode())) {
            // 计次卡退款暂时不打印小票和不发短信
            return;
        }
        try {
            cashierRefundService.refundPrint(refund.getMerchantNo(), refund.getOperationId(), refundId);
        } catch (CustomException e) {
            log.error("打印失败:{}", e.getResultJson());
        }
        // 发送短信通知
        smsSendNoticeService.cashierRefundSend(refundId);
    }

    /**
     * 退积分、退储值、退计次卡、退库存
     *
     * @param order
     * @param refund
     */
    @Transactional(rollbackFor = Exception.class)
    public void refundUnActive(CashierOrderDetailVo order, CashierRefundDetailVo refund) {
        if (order.getCategory() == CashierOrderCategory.COUNTED.getCode()) {
            // 计次开卡
            refund.getSkus().forEach(x -> {
                memberTimeCardService.refund(order.getOperationId(), order.getMemberId(), order.getOrderNo(), x.getRefundQuantity(), x.getSkuId());
            });
        } else if (order.getCategory() == CashierOrderCategory.RECHARGE.getCode()) {
            // 会员储值
            refund.getSkus().forEach(x -> {
                memberAssetsService.investReturnRecharge(refund.getMerchantNo(), refund.getMemberId(), order.getOrderNo(),
                        refund.getRefundNo(), x.getRefundQuantity(), x.getSkuId());
            });
        } else {
            // 批量退货入库，（有仅仅退款不退货的情况，这种情况从库存走报废，报废原因）
            batchReplenish(refund.getMerchantNo(), refund.getStoreNo(), refund.getSkus());
        }
        if (StringUtils.isNotBlank(refund.getMemberId()) && refund.getIntegral() != 0) {
            // 退积分
            memberAssetsService.removeIntegral(refund.getMerchantNo(), refund.getMemberId(), refund.getRefundNo(),
                    refund.getIntegral(), "消费退款退积分");
        }
    }

    /**
     * 退货商品批量入库
     *
     * @param merchantNo
     * @param storeNo
     * @param products
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchReplenish(long merchantNo, long storeNo, List<CashierRefundProductVo> products) {
        products.stream()
                .filter(x -> StringUtils.isNotBlank(x.getSkuId()))
                .forEach(x -> {
                    stockSkuService.editSellStockReplenish(merchantNo, storeNo, x.getSkuId(), x.getRefundQuantity());
                });
    }

    /**
     * 同步修改对应销售订单中退款信息
     *
     * @param merchantNo
     * @param storeNo
     * @param orderId
     */
    public void updateOrderRefund(long merchantNo, long storeNo, String orderId) {
        CashierOrderVo order = cashierOrderService.findById(orderId);
        if (order == null) {
            return;
        }
        int refundMark = CashierRefundMark.PART.getCode();
        List<CashierRefundEntity> refunds = cashierRefundService.findAllByOrderId(merchantNo, storeNo, orderId);
        List<CashierOrderProductEntity> products = cashierOrderProductService.findAllByOrderId(orderId);
        List<String> refundIds = refunds.stream().map(CashierRefundEntity::getId).collect(Collectors.toList());
        List<CashierRefundProductEntity> refundProducts = cashierRefundProductService.findAllByRefundIds(refundIds);

        products.forEach(x -> {
            long refundQuantity = refundProducts.stream()
                    .filter(y -> x.getId().equals(y.getOrderProductId()))
                    .mapToLong(CashierRefundProductEntity::getRefundQuantity).sum();
            long refundAmount = refundProducts.stream()
                    .filter(y -> x.getId().equals(y.getOrderProductId()))
                    .mapToLong(CashierRefundProductEntity::getPayableAmount).sum();
            x.setRefundQuantity(refundQuantity);
            x.setRefundAmount(refundAmount);
            cashierOrderProductService.updateRefund(x.getId(), refundQuantity, refundAmount);
        });

        long payableAmount = refunds.stream().mapToLong(CashierRefundEntity::getPayableAmount).sum();
        long refundIntegral = refunds.stream().mapToLong(CashierRefundEntity::getIntegral).sum();
        long orderPayableAmount = AmountUtil.parse(order.getRealityAmount());
        if (order.getType() == CashierOrderType.AMOUNT.getCode() && orderPayableAmount == payableAmount) {
            refundMark = CashierRefundMark.ALL.getCode();
        }
        if (order.getType() == CashierOrderType.COUNTED.getCode()) {
            if (products.stream().noneMatch(product -> product.getSaleQuantity() > product.getRefundQuantity())) {
                refundMark = CashierRefundMark.ALL.getCode();
            }
        }
        cashierOrderService.updateOrderRefund(merchantNo, orderId, refundMark, payableAmount, refundIntegral);
    }
}
