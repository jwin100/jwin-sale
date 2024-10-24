package com.mammon.cashier.service;

import cn.hutool.core.util.StrUtil;
import com.mammon.cashier.channel.factory.TradeMemberChannel;
import com.mammon.cashier.channel.factory.TradeMemberFactory;
import com.mammon.cashier.channel.factory.dto.TradeMemberPayDto;
import com.mammon.cashier.channel.factory.enums.TradeMemberStatus;
import com.mammon.cashier.channel.factory.vo.TradeMemberPayVo;
import com.mammon.cashier.domain.dto.CashierOrderDto;
import com.mammon.cashier.domain.dto.CashierOrderPayDto;
import com.mammon.cashier.domain.dto.CashierOrderProductDto;
import com.mammon.cashier.domain.dto.CashierOrderSkuDto;
import com.mammon.cashier.domain.entity.CashierOrderEntity;
import com.mammon.cashier.domain.entity.CashierOrderPayEntity;
import com.mammon.cashier.domain.entity.CashierOrderProductEntity;
import com.mammon.cashier.domain.enums.*;
import com.mammon.cashier.domain.model.DivideAmountModel;
import com.mammon.cashier.domain.model.TradeOrderPayModel;
import com.mammon.cashier.domain.vo.*;
import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.goods.domain.enums.SpuCountedType;
import com.mammon.goods.domain.enums.UnitType;
import com.mammon.leaf.enums.DocketType;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.market.domain.vo.MarketRechargeRuleVo;
import com.mammon.market.domain.vo.MarketTimeCardRuleVo;
import com.mammon.market.service.MarketRechargeRuleService;
import com.mammon.market.service.MarketTimeCardRuleService;
import com.mammon.member.domain.entity.MemberAttrEntity;
import com.mammon.member.service.*;
import com.mammon.payment.domain.enums.PayModeConst;
import com.mammon.payment.domain.vo.PayModeModel;
import com.mammon.payment.service.PayModeService;
import com.mammon.sms.service.SmsSendNoticeService;
import com.mammon.stock.domain.dto.StockStoreLockDto;
import com.mammon.stock.domain.vo.StockChangeVo;
import com.mammon.stock.domain.vo.StockSkuVo;
import com.mammon.stock.service.StockSkuLockService;
import com.mammon.stock.service.StockSkuService;
import com.mammon.trade.model.dto.TradeDto;
import com.mammon.trade.model.enums.TradeCommonStatus;
import com.mammon.trade.model.vo.TradeAuthVo;
import com.mammon.trade.model.vo.TradeNativeVo;
import com.mammon.trade.service.TradeService;
import com.mammon.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单收款
 * <p>
 * 创建订单-锁住库存
 * 支付成功-商品出库。异步：会员积分，打印，短信通知
 * 支付失败-继续等待支付
 * 等待支付超时后取消、取消支付-检查是否有支付成功交易并退款
 * -库存回滚
 *
 * @author dcl
 * @since 2024/6/3 10:49
 */
@Service
@Slf4j
public class CashierOrderTradeService {

    @Resource
    private CashierOrderService cashierOrderService;

    @Resource
    private CashierOrderProductService cashierOrderProductService;

    @Resource
    private CashierOrderPayService cashierOrderPayService;

    @Resource
    private MarketRechargeRuleService marketRechargeRuleService;

    @Resource
    private MarketTimeCardRuleService marketTimeCardRuleService;

    @Resource
    private StockSkuService stockSkuService;

    @Resource
    private LeafCodeService leafCodeService;

    @Resource
    private MemberAttrService memberAttrService;

    @Resource
    private StockSkuLockService stockSkuLockService;

    @Resource
    private PayModeService payModeService;

    @Resource
    private TradeService tradeService;

    @Resource
    private MemberTimeCardService memberTimeCardService;


    @Resource
    private MemberAssetsService memberAssetsService;

    @Resource
    private CashierOrderTradeAsyncService cashierOrderTradeAsyncService;

    @Resource
    private SmsSendNoticeService smsSendNoticeService;

    /**
     * 金额核算
     *
     * @param merchantNo
     * @param dto
     */
    public CashierOrderComputeVo compute(long merchantNo, long storeNo, CashierOrderDto dto) {
        if (dto.getDiscount() == null) {
            dto.setDiscount(BigDecimal.ZERO);
        }
        if (dto.getAdjustAmount() == null) {
            dto.setAdjustAmount(BigDecimal.ZERO);
        }
        List<CashierOrderSkuComputeVo> skuComputeVos = cashierProductAmount(merchantNo,
                storeNo, dto.getCategory(), dto.getType(), dto.getCards());
        if (dto.getType() == CashierOrderType.COUNTED.getCode()) {
            CashierOrderComputeVo vo = new CashierOrderComputeVo();
            vo.setSkus(skuComputeVos);
            return vo;
        }
        // 原价
        BigDecimal originalAmount = AmountUtil.parseBigDecimal(
                skuComputeVos.stream()
                        .mapToLong(x -> AmountUtil.parse(x.getPayableAmount()))
                        .sum()
        );
        // 折扣金额
        BigDecimal discountAmount = BigDecimal.ZERO;
        // 抹零金额
        BigDecimal ignoreAmount = BigDecimal.ZERO;
        // 总优惠金额
        BigDecimal preferentialAmount = BigDecimal.ZERO;
        // 优惠后
        BigDecimal collectAmount = BigDecimal.ZERO;
        // 应收
        BigDecimal payableAmount = BigDecimal.ZERO;

        BigDecimal tempAmount = originalAmount;

        // 计算折扣
        if (!Objects.equals(dto.getDiscount(), BigDecimal.ZERO)) {
            BigDecimal discount = AmountUtil.divide(dto.getDiscount(), BigDecimal.valueOf(10));
            BigDecimal amount = AmountUtil.multiply(originalAmount, discount);
            discountAmount = originalAmount.subtract(amount);
            tempAmount = amount;
        }

        // 计算抹零
        if (dto.getIgnoreType() == 1) {
            BigDecimal amount = tempAmount.setScale(1, RoundingMode.FLOOR);
            ignoreAmount = tempAmount.subtract(amount);
        } else if (dto.getIgnoreType() == 2) {
            BigDecimal amount = tempAmount.setScale(0, RoundingMode.FLOOR);
            ignoreAmount = tempAmount.subtract(amount);
        } else if (dto.getIgnoreType() == 3 && tempAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal[] amounts = tempAmount.divideAndRemainder(BigDecimal.valueOf(10));
            if (amounts.length == 2) {
                ignoreAmount = amounts[1];
            }
        }
        // 总优惠金额=折扣金额 + 抹零金额
        preferentialAmount = discountAmount.add(ignoreAmount);
        // 优惠后金额 = 原价 - 总优惠金额
        collectAmount = originalAmount.subtract(preferentialAmount);
        // 应付 = 优惠后金额
        payableAmount = collectAmount;

        if (dto.getAdjustAmount().compareTo(BigDecimal.ZERO) > 0) {
            // 如果有手动调额，应付 = 手动调额
            dto.setAdjustAmount(dto.getAdjustAmount());
            payableAmount = dto.getAdjustAmount();
        }

        CashierOrderComputeVo vo = new CashierOrderComputeVo();
        vo.setOriginalAmount(originalAmount);
        vo.setIgnoreType(dto.getIgnoreType());
        vo.setIgnoreAmount(ignoreAmount);
        vo.setDiscount(dto.getDiscount());
        vo.setDiscountAmount(discountAmount);
        vo.setPreferentialAmount(preferentialAmount);
        vo.setCollectAmount(collectAmount);
        vo.setAdjustAmount(dto.getAdjustAmount());
        vo.setPayableAmount(payableAmount);
        vo.setCategory(dto.getCategory());
        vo.setSkuTotal(getSkuTotal(skuComputeVos));
        vo.setSkus(skuComputeVos);
        return vo;
    }

    private long getSkuTotal(List<CashierOrderSkuComputeVo> skus) {
        long weightCount = skus.stream()
                .filter(x -> x.getUnitType() == UnitType.WEIGHT.getCode())
                .count();
        long amountCount = skus.stream()
                .filter(x -> x.getUnitType() == UnitType.NUMBER.getCode())
                .mapToLong(x -> StockUtil.parse(x.getQuantity()))
                .sum();
        BigDecimal amount = StockUtil.parseBigDecimal(amountCount);
        return weightCount + amount.longValue();
    }


    /**
     * 创建订单
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(long merchantNo, long storeNo, String accountId,
                              CashierOrderDto dto) {
        if (dto.getCategory() != CashierOrderCategory.GOODS.getCode() && StrUtil.isBlank(dto.getMemberId())) {
            throw new CustomException("会员不能为空");
        }
        String orderId = leafCodeService.generateDocketNo(DocketType.CASHIER_ORDER);
        String orderNo = orderId; // 后面删掉
        if (StringUtils.isBlank(orderNo)) {
            throw new CustomException("订单创建异常");
        }
        if (dto.getCashierTime() == null) {
            dto.setCashierTime(LocalDateTime.now());
        }
        // 金额核算
        CashierOrderComputeVo computeVo = compute(merchantNo, storeNo, dto);
        // 商品详情核算
        List<CashierOrderProductDto> products = initCashierOrderProduct(merchantNo, storeNo, orderId,
                dto.getCategory(), dto.getMemberId(), computeVo.getSkus());
        if (dto.getType() == CashierOrderType.AMOUNT.getCode()) {
            // 根据实际应收金额，计算每个商品分摊金额
            divideAmount(computeVo.getPayableAmount(), computeVo.getPreferentialAmount(), products);
        }
        cashierOrderProductService.batchSave(products);

        String subject = "";
        long integral = 0;
        if (dto.getCategory() == CashierOrderCategory.RECHARGE.getCode()) {
            long amount = computeVo.getSkus().stream().mapToLong(x -> AmountUtil.parse(x.getOriginAmount())).sum();
            integral = products.stream().mapToLong(CashierOrderProductDto::getIntegral).sum();
            subject = "储值：" + AmountUtil.parseYuan(amount) + "元";
        } else if (dto.getCategory() == CashierOrderCategory.COUNTED.getCode()) {
            subject = "计次开卡";
            integral = products.stream().mapToLong(CashierOrderProductDto::getIntegral).sum();
        } else {
            subject = products.size() > 1
                    ? products.get(0).getSkuName() + "等"
                    : products.stream().findFirst().map(CashierOrderProductDto::getSkuName).orElse("临时商品");
            // 计算积分
            integral = divideIntegral(merchantNo, dto.getMemberId(), computeVo.getAdjustAmount(), computeVo.getPayableAmount());
            // 锁定库存
            lockStock(merchantNo, storeNo, orderId, products);
        }
        CashierOrderEntity entity = new CashierOrderEntity();
        entity.setId(orderId);
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setCustomerNo(dto.getCustomerNo());
        entity.setOrderNo(orderNo);
        entity.setCategory(dto.getCategory());
        entity.setType(dto.getType());
        entity.setSubject(subject);
        entity.setOriginalAmount(AmountUtil.parse(computeVo.getOriginalAmount()));
        entity.setIgnoreType(computeVo.getIgnoreType());
        entity.setIgnoreAmount(AmountUtil.parse(computeVo.getIgnoreAmount()));
        entity.setDiscount(QuantityUtil.parse(computeVo.getDiscount()));
        entity.setDiscountAmount(AmountUtil.parse(computeVo.getDiscountAmount()));
        entity.setPreferentialAmount(AmountUtil.parse(computeVo.getPreferentialAmount()));
        entity.setCollectAmount(AmountUtil.parse(computeVo.getCollectAmount()));
        entity.setAdjustAmount(AmountUtil.parse(computeVo.getAdjustAmount()));
        entity.setPayableAmount(AmountUtil.parse(computeVo.getPayableAmount()));
        entity.setCountedTotal(dto.getCountedTotal());
        entity.setIntegral(integral);
        entity.setStatus(CashierOrderStatus.WAIT.getCode());
        entity.setRefundMark(CashierRefundMark.NONE.getCode());
        entity.setSource(dto.getSource());
        entity.setMemberId(dto.getMemberId());
        entity.setOperationId(accountId);
        entity.setRemark(dto.getRemark());
        entity.setServiceAccountIds(JsonUtil.toJSONString(dto.getServiceAccountIds()));
        entity.setCashierTime(dto.getCashierTime());
        entity.setCreateTime(LocalDateTime.now());
        cashierOrderService.save(entity);
        return orderId;
    }

    /**
     * 订单收款
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CashierOrderPayResultVo orderPay(long merchantNo, long storeNo, String accountId, CashierOrderPayDto dto) {
        CashierOrderEntity order = cashierOrderService.findBaseById(dto.getId());
        List<CashierOrderProductEntity> products = cashierOrderProductService.findAllByOrderId(dto.getId());
        // 订单验证，支付方式验证
        orderPayValidate(order, dto);
        CashierOrderPayEntity orderPay = cashierOrderPayService.save(dto);
        // 更新订单支付方式
        cashierOrderService.updateOrderPayType(order.getId(), orderPay);

        // 调用支付扣款
        TradeOrderPayModel payVo = tradePay(accountId, order, dto, orderPay);
        if (payVo.getStatus() == 0) {
            // 支付失败(不释放锁定库存，因为可以重新支付，手动调用取消支付时或者订单过期时再释放库存)
            throw new CustomException(payVo.getDescribe());
        }
        // 判断支付总金额是否等于待付金额，如果等于做支付成功处理
        List<CashierOrderPayEntity> orderPays = cashierOrderPayService.findAllByOrderId(order.getId());
        long payableAmount = order.getPayableAmount();
        long realityAmount = orderPays.stream()
                .filter(x -> x.getStatus() == CashierOrderPayStatus.PAY_SUCCESS.getCode())
                .mapToLong(CashierOrderPayEntity::getPayableAmount).sum();
        // 计算剩余待支付金额
        long waitAmount = payableAmount - realityAmount;
        // 更新已收金额
        cashierOrderService.updateOrderAmount(order.getId(), realityAmount);
        // 支付成功
        if (waitAmount == 0) {
            // 通知订单付款完成
            orderPaySuccess(order, products, orderPays);
        } else {
            orderPartial(order.getId());
        }
        CashierOrderPayResultVo vo = new CashierOrderPayResultVo();
        vo.setOrderId(order.getId());
        vo.setPayableAmount(AmountUtil.parseBigDecimal(order.getPayableAmount()));
        vo.setReceivedAmount(AmountUtil.parseBigDecimal(realityAmount));
        vo.setWaitAmount(AmountUtil.parseBigDecimal(waitAmount));
        return vo;
    }

    /**
     * 商品详情金额核算
     *
     * @param merchantNo
     * @param storeNo
     * @param group
     * @param type
     * @param skus
     * @return
     */
    public List<CashierOrderSkuComputeVo> cashierProductAmount(long merchantNo, long storeNo, int group, int type,
                                                               List<CashierOrderSkuDto> skus) {
        if (type == CashierOrderType.COUNTED.getCode()) {
            return skus.stream().map(x -> {
                CashierOrderSkuComputeVo vo = new CashierOrderSkuComputeVo();
                vo.setKey(x.getKey());
                vo.setSkuId(x.getSkuId());
                vo.setSkuName(x.getSkuName());
                vo.setQuantity(x.getQuantity());
                vo.setReferenceAmount(BigDecimal.ZERO);
                vo.setOriginAmount(BigDecimal.ZERO);
                vo.setAdjustAmount(BigDecimal.ZERO);
                vo.setPayableAmount(BigDecimal.ZERO);
                vo.setSellStock(x.getSellStock());
                vo.setPicture(x.getPicture());
                vo.setCountedType(x.getCountedType());
                return vo;
            }).collect(Collectors.toList());
        }
        return skus.stream().map(x -> {
            if (x.getAdjustAmount() == null) {
                x.setAdjustAmount(BigDecimal.ZERO);
            }
            BigDecimal referenceAmount = BigDecimal.ZERO;
            BigDecimal originAmount = BigDecimal.ZERO;
            BigDecimal adjustAmount = x.getAdjustAmount();
            BigDecimal payableAmount = BigDecimal.ZERO;
            String unitId = "";
            String unitName = "";
            int unitType = UnitType.NUMBER.getCode();
            String unitTypeName = UnitType.NUMBER.getName();
            if (StringUtils.isBlank(x.getSkuId())) {
                // 临时商品
                if (x.getAdjustAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new CustomException("临时商品金额不能为空");
                }
                referenceAmount = x.getAdjustAmount();
                adjustAmount = x.getAdjustAmount();
            } else {
                if (group == CashierOrderCategory.COUNTED.getCode()) {
                    MarketTimeCardRuleVo timeCardVo = marketTimeCardRuleService.findBaseById(x.getSkuId());
                    if (timeCardVo == null) {
                        throw new CustomException("计次卡信息错误");
                    }
                    x.setSkuName(timeCardVo.getName());
                    referenceAmount = timeCardVo.getRealAmount();
                } else if (group == CashierOrderCategory.RECHARGE.getCode()) {
                    MarketRechargeRuleVo rechargeVo = marketRechargeRuleService.findById(x.getSkuId());
                    if (rechargeVo == null) {
                        throw new CustomException("储值信息错误");
                    }

                    String skuName = "储值" + rechargeVo.getPrepaidAmount() + "元";
                    if (rechargeVo.getGiveAmount() != null && rechargeVo.getGiveAmount().compareTo(BigDecimal.ZERO) > 0) {
                        skuName += "，送" + rechargeVo.getGiveAmount() + "元";
                    }
                    x.setSkuName(skuName);
                    referenceAmount = rechargeVo.getPrepaidAmount();
                    adjustAmount = rechargeVo.getRealAmount();
                } else {
                    StockSkuVo skuVo = stockSkuService.findBySkuId(merchantNo, storeNo, x.getSkuId());
                    if (skuVo == null) {
                        throw new CustomException("商品信息错误");
                    }
                    if (skuVo.getCountedType() == SpuCountedType.NO.getCode() && x.getQuantity().compareTo(skuVo.getSellStock()) > 0) {
                        throw new CustomException("商品库存不足");
                    }
                    referenceAmount = skuVo.getReferenceAmount();
                    unitId = skuVo.getUnitId();
                    unitName = skuVo.getUnitName();
                    unitType = skuVo.getUnitType();
                    unitTypeName = skuVo.getUnitTypeName();
                }
            }
            originAmount = AmountUtil.multiply(referenceAmount, x.getQuantity());
            payableAmount = originAmount;
            if (adjustAmount.compareTo(BigDecimal.ZERO) > 0) {
                payableAmount = AmountUtil.multiply(adjustAmount, x.getQuantity());
            }

            CashierOrderSkuComputeVo vo = new CashierOrderSkuComputeVo();
            vo.setKey(x.getKey());
            vo.setSkuId(x.getSkuId());
            vo.setSkuName(x.getSkuName());
            vo.setQuantity(x.getQuantity());
            vo.setReferenceAmount(referenceAmount);
            vo.setOriginAmount(originAmount);
            vo.setAdjustAmount(adjustAmount);
            vo.setPayableAmount(payableAmount);
            vo.setSellStock(x.getSellStock());
            vo.setUnitId(unitId);
            vo.setUnitName(unitName);
            vo.setUnitType(unitType);
            vo.setUnitTypeName(unitTypeName);
            vo.setPicture(x.getPicture());
            vo.setCountedType(x.getCountedType());
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 初始化商品信息
     *
     * @param merchantNo
     * @param storeNo
     * @param orderId
     * @param category
     * @param memberId
     * @param skus
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public List<CashierOrderProductDto> initCashierOrderProduct(long merchantNo, long storeNo, String orderId, int category,
                                                                String memberId, List<CashierOrderSkuComputeVo> skus) {
        return skus.stream().map(x -> {
            CashierOrderProductInitVo initVo = initProduct(merchantNo, storeNo, memberId, category, x);
            CashierOrderProductDto entity = new CashierOrderProductDto();
            entity.setId(Generate.generateUUID());
            entity.setOrderId(orderId);
            entity.setSpuId(initVo.getSpuId());
            entity.setSkuId(initVo.getSkuId());
            entity.setSkuName(initVo.getSkuName());
            entity.setPicture(initVo.getPicture());
            entity.setSaleQuantity(x.getQuantity());
            entity.setReferenceAmount(x.getReferenceAmount());
            entity.setAdjustAmount(x.getAdjustAmount());
            entity.setPayableAmount(x.getPayableAmount());
            entity.setIntegral(initVo.getIntegral());
            entity.setCountedType(initVo.getCountedType());
            return entity;
        }).collect(Collectors.toList());
    }

    /**
     * 初始化商品信息
     *
     * @param merchantNo
     * @param storeNo
     * @param memberId
     * @param category
     * @return
     */
    private CashierOrderProductInitVo initProduct(long merchantNo, long storeNo, String memberId, int category, CashierOrderSkuComputeVo sku) {
        CashierOrderProductInitVo vo = new CashierOrderProductInitVo();
        vo.setSkuId(sku.getSkuId());
        vo.setSkuName(sku.getSkuName());
        if (StringUtils.isBlank(sku.getSkuId())) {
            // 临时商品
            MemberAttrEntity memberAttr = memberAttrService.findByMerchantNo(merchantNo);
            if (StringUtils.isNotBlank(memberId) && memberAttr != null && memberAttr.getConvertAmount() > 0) {
                BigDecimal amount = AmountUtil.divide(sku.getPayableAmount(), AmountUtil.parseBigDecimal(memberAttr.getConvertAmount()));
                vo.setIntegral(amount.longValue() * memberAttr.getConvertIntegral());
            }
            return vo;
        }
        if (category == CashierOrderCategory.COUNTED.getCode()) {
            MarketTimeCardRuleVo timeCardVo = marketTimeCardRuleService.findBaseById(sku.getSkuId());
            if (timeCardVo != null) {
                vo.setSkuId(timeCardVo.getId());
                vo.setSkuName(timeCardVo.getName());
                vo.setIntegral(timeCardVo.getGiveIntegral());
                vo.setCountedType(SpuCountedType.YES.getCode());
            }
            return vo;
        }
        if (category == CashierOrderCategory.RECHARGE.getCode()) {
            MarketRechargeRuleVo rechargeVo = marketRechargeRuleService.findById(sku.getSkuId());
            if (rechargeVo != null) {
                String skuName = "储值" + rechargeVo.getPrepaidAmount() + "元";
                if (rechargeVo.getGiveAmount() != null && rechargeVo.getGiveAmount().compareTo(BigDecimal.ZERO) > 0) {
                    skuName += "，送" + rechargeVo.getGiveAmount() + "元";
                }
                vo.setSkuId(rechargeVo.getId());
                vo.setSkuName(skuName);
                vo.setIntegral(rechargeVo.getGiveIntegral());
                vo.setCountedType(SpuCountedType.YES.getCode());
            }
            return vo;
        }

        StockSkuVo skuVo = stockSkuService.findBySkuId(merchantNo, storeNo, sku.getSkuId());
        if (skuVo != null) {
            long integral = 0;
            if (StringUtils.isNotBlank(memberId)) {
                MemberAttrEntity memberAttr = memberAttrService.findByMerchantNo(merchantNo);
                if (memberAttr != null && memberAttr.getConvertAmount() > 0) {
                    BigDecimal amount = AmountUtil.divide(sku.getPayableAmount(), AmountUtil.parseBigDecimal(memberAttr.getConvertAmount()));
                    integral = amount.longValue() * memberAttr.getConvertIntegral();
                }
            }
            if (skuVo.getCountedType() == SpuCountedType.NO.getCode() && sku.getQuantity().compareTo(skuVo.getSellStock()) > 0) {
                throw new CustomException(String.format("%s库存不足", sku.getSkuName()));
            }
            vo.setSpuId(skuVo.getSpuId());
            vo.setSkuId(skuVo.getSkuId());
            vo.setSkuName(skuVo.getSkuName());
            vo.setPicture(skuVo.getPicture());
            vo.setCountedType(skuVo.getCountedType());
            vo.setIntegral(integral);
        }
        return vo;
    }

    /**
     * 计算分摊金额
     *
     * @param payableAmount 应收
     * @param products      商品信息
     */
    private void divideAmount(BigDecimal payableAmount, BigDecimal preferentialAmount,
                              List<CashierOrderProductDto> products) {
        // 计算商品分摊权重=商品金额（单价*数量*商品的优惠）/所有商品总价(整体优惠后的)
        // 计算总优惠金额=应付-实付
        // 商品分摊金额=权重*总优惠金额
        // 商品实付金额=商品优惠后金额-分摊金额`
        if (preferentialAmount.compareTo(BigDecimal.ZERO) <= 0) {
            // ==0 说明没有整单优惠
            // < 0 说明有调整金额，且比商品应付金额大 也不分摊
            return;
        }
        List<DivideAmountModel> divides = products.stream().map(x -> {
            DivideAmountModel model = new DivideAmountModel();
            BigDecimal weight = AmountUtil.divide(x.getPayableAmount(), payableAmount);
            BigDecimal shareAmount = AmountUtil.multiply(weight, preferentialAmount);
            model.setId(x.getId());
            model.setWeight(weight);
            model.setShareAmount(shareAmount);
            return model;
        }).collect(Collectors.toList());
        long divideSum = divides.stream().mapToLong(x -> AmountUtil.parse(x.getShareAmount())).sum();
        BigDecimal diffAmount = preferentialAmount.subtract(AmountUtil.parseBigDecimal(divideSum));
        // 因为精度问题，要判断计算出来的总优惠是否等于要分摊的总优惠金额，给分摊比例最大的做增减
        if (diffAmount.compareTo(BigDecimal.ZERO) > 0) {
            divides.stream().max(Comparator.comparing(DivideAmountModel::getWeight))
                    .ifPresent(model -> {
                        model.setShareAmount(model.getShareAmount().subtract(diffAmount));
                    });
        }
        if (diffAmount.compareTo(BigDecimal.ZERO) < 0) {
            divides.stream().max(Comparator.comparing(DivideAmountModel::getWeight))
                    .ifPresent(model -> {
                        model.setShareAmount(model.getShareAmount().add(diffAmount));
                    });
        }
        products.forEach(x -> {
            DivideAmountModel model = divides.stream().filter(y -> y.getId().equals(x.getId())).findFirst()
                    .orElse(null);
            if (model != null) {
                BigDecimal divideAmount = x.getPayableAmount()
                        .subtract(model.getShareAmount());
                x.setDivideAmount(divideAmount);
            }
        });
    }

    /**
     * 计算积分
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
     * 订单创建，预扣库存
     *
     * @param merchantNo
     * @param storeNo
     * @param orderId
     * @param products
     */
    @Transactional(rollbackFor = Exception.class)
    public void lockStock(long merchantNo, long storeNo, String orderId, List<CashierOrderProductDto> products) {
        products.forEach(x -> {
            if (StringUtils.isNotBlank(x.getSkuId()) && x.getCountedType() == SpuCountedType.NO.getCode()) {
                // 预扣库存
                StockStoreLockDto lockDto = new StockStoreLockDto();
                lockDto.setSpuId(x.getSpuId());
                lockDto.setSkuId(x.getSkuId());
                lockDto.setMerchantNo(merchantNo);
                lockDto.setStoreNo(storeNo);
                lockDto.setOrderId(x.getOrderId());
                lockDto.setLockStock(x.getSaleQuantity());
                StockChangeVo vo = stockSkuLockService.lockStock(lockDto);
                if (vo.getCode() == 0) {
                    // 预扣库存异常，关闭订单
                    cashierOrderService.orderClose(orderId, vo.getMessage());
                    throw new CustomException(vo.getMessage());
                }
            }
        });
    }

    /**
     * 订单成功，删除预扣库存
     *
     * @param merchantNo
     * @param storeNo
     * @param products
     */
    @Transactional(rollbackFor = Exception.class)
    public void unLockStock(long merchantNo, long storeNo, List<CashierOrderProductEntity> products) {
        // 支付成功处理，删除锁定库存
        products.forEach(x -> {
            if (StringUtils.isNotBlank(x.getSkuId())) {
                StockStoreLockDto lockDto = new StockStoreLockDto();
                lockDto.setSpuId(x.getSpuId());
                lockDto.setSkuId(x.getSkuId());
                lockDto.setMerchantNo(merchantNo);
                lockDto.setStoreNo(storeNo);
                lockDto.setOrderId(x.getOrderId());
                stockSkuLockService.unLockStock(lockDto);
            }
        });
    }

    /**
     * 验证支付信息
     *
     * @param order
     * @param dto
     */
    private void orderPayValidate(CashierOrderEntity order, CashierOrderPayDto dto) {
        if (order == null) {
            throw new CustomException("订单不存在");
        }
        if (order.getStatus() == CashierOrderStatus.CLOSE.getCode()) {
            throw new CustomException("订单已取消，不允许支付");
        }
        if (order.getStatus() == CashierOrderStatus.FINISH.getCode()) {
            throw new CustomException("订单已完成，不允许支付");
        }
        if (order.getType() == CashierOrderType.AMOUNT.getCode() &&
                dto.getPayAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException("支付金额错误");
        }

        if (AmountUtil.parse(dto.getPayAmount()) > order.getPayableAmount() - order.getRealityAmount()) {
            throw new CustomException("支付金额不能大于待付款金额");
        }

        PayModeModel payModeModel = payModeService.findByPayCode(dto.getPayCode());
        if (payModeModel == null) {
            throw new CustomException("支付未开通，不支持此支付方式付款");
        }
        if (StringUtils.isBlank(order.getMemberId()) && payModeModel.isMemberOnly()) {
            throw new CustomException("支付异常，存在会员专用支付方式");
        }
    }

    /**
     * 调用第三方支付
     *
     * @param accountId
     * @param dto
     * @param orderPay
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TradeOrderPayModel tradePay(String accountId, CashierOrderEntity order,
                                       CashierOrderPayDto dto,
                                       CashierOrderPayEntity orderPay) {
        PayModeModel payModeModel = PayModeConst.payModeModels.stream()
                .filter(x -> x.getCode() == orderPay.getPayCode())
                .findFirst().orElse(null);
        if (payModeModel == null) {
            throw new CustomException("支付方式错误");
        }

        if (payModeModel.isThirdParty()) {
            // 调用第三方支付渠道
            return tradePay(orderPay.getId(), dto, order);
        } else if (payModeModel.isMemberOnly()) {
            // 调用会员支付
            return memberPay(orderPay.getId(), accountId, dto, order);
        } else {
            // 其他如现金、标记付款等
            cashierOrderPayService.paySuccess(orderPay.getId(), "", AmountUtil.parse(dto.getPayAmount()));
            return TradeOrderPayModel.success(null, null);
        }
    }

    public TradeOrderPayModel tradePay(String orderPayId, CashierOrderPayDto dto, CashierOrderEntity order) {
        TradeDto tradeDto = new TradeDto();
        tradeDto.setMerchantNo(order.getMerchantNo());
        tradeDto.setStoreNo(order.getStoreNo());
        tradeDto.setOrderNo(order.getOrderNo());
        tradeDto.setOrderSubject(order.getSubject());
        tradeDto.setOrderAmount(AmountUtil.parse(dto.getPayAmount()));
        tradeDto.setAuthCode(dto.getAuthCode());
        tradeDto.setMemberId(order.getMemberId());
        if (dto.getPayCode() == PayModeConst.payModeNative.getCode()) {
            TradeNativeVo nativeVo = tradeService.tradeNative(tradeDto);
            if (nativeVo.getStatus() == TradeCommonStatus.FAILED.getCode()) {
                cashierOrderPayService.payFailed(orderPayId, nativeVo.getTradeNo(), nativeVo.getDescribe());
                throw new CustomException(nativeVo.getDescribe());
            }
            cashierOrderPayService.paySuccess(orderPayId, nativeVo.getTradeNo(), AmountUtil.parse(dto.getPayAmount()));
            String qrCode = QrCodeUtil.getBase64QRCode(nativeVo.getCodeUrl());
            return TradeOrderPayModel.success(nativeVo.getTradeNo(), qrCode);
        } else {
            TradeAuthVo authVo = tradeService.tradeAuth(tradeDto);
            if (authVo.getStatus() == TradeCommonStatus.FAILED.getCode()) {
                cashierOrderPayService.payFailed(orderPayId, authVo.getTradeNo(), authVo.getDescribe());
                throw new CustomException(authVo.getDescribe());
            }
            cashierOrderPayService.paySuccess(orderPayId, authVo.getTradeNo(), AmountUtil.parse(dto.getPayAmount()));
            return TradeOrderPayModel.success(authVo.getTradeNo(), null);
        }
    }

    public TradeOrderPayModel memberPay(String orderPayId, String accountId,
                                        CashierOrderPayDto dto, CashierOrderEntity order) {
        TradeMemberChannel factory = TradeMemberFactory.get(dto.getPayCode());
        if (factory == null) {
            throw new CustomException("支付方式错误");
        }
        TradeMemberPayDto payDto = new TradeMemberPayDto();
        payDto.setMerchantNo(order.getMerchantNo());
        payDto.setStoreNo(order.getStoreNo());
        payDto.setAccountId(accountId);
        payDto.setOrderNo(order.getOrderNo());
        payDto.setMemberId(order.getMemberId());
        payDto.setSubject(order.getSubject());
        payDto.setAmount(AmountUtil.parse(dto.getPayAmount()));
        payDto.setCountedId(dto.getCountedId());
        payDto.setCountedTotal(dto.getCountedTotal());

        TradeMemberPayVo payVo = factory.pay(payDto);
        if (payVo.getStatus() == TradeMemberStatus.FAILED.getCode()) {
            cashierOrderPayService.payFailed(orderPayId, payVo.getTradeNo(), payVo.getDescribe());
            throw new CustomException(payVo.getDescribe());
        }
        cashierOrderPayService.paySuccess(orderPayId, payVo.getTradeNo(), AmountUtil.parse(dto.getPayAmount()));
        return TradeOrderPayModel.success(payVo.getTradeNo(), null);
    }

    /**
     * 订单支付成功执行
     *
     * @param order
     * @param products
     * @param orderPays
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderPaySuccess(CashierOrderEntity order, List<CashierOrderProductEntity> products,
                                List<CashierOrderPayEntity> orderPays) {
        if (order.getCategory() == CashierOrderCategory.GOODS.getCode()) {
            // 删除锁定库存
            unLockStock(order.getMerchantNo(), order.getStoreNo(), products);
        }
        if (order.getIntegral() > 0) {
            // 商品销售增加积分
            memberAssetsService.addIntegral(order.getMerchantNo(), order.getMemberId(), order.getOrderNo(), order.getIntegral(), "消费增加积分");
        }
        // 订单生效处理
        orderActive(order);
        // 更新订单已完成状态
        cashierOrderService.orderFinish(order.getId());
        // 打印订单小票
        cashierOrderTradeAsyncService.payFinishPrint(order.getId());
        // 事务提交后执行
        // TODO 改成同步
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 发送短信通知
                smsSendNoticeService.cashierOrderSend(order.getId());
            }
        });
    }

    /**
     * 修改订单状态为部分付款
     *
     * @param orderId
     */
    private void orderPartial(String orderId) {
        cashierOrderService.orderPartial(orderId);
    }

    /**
     * 订单生效处理
     *
     * @param order
     */
    private void orderActive(CashierOrderEntity order) {
        if (order == null) {
            return;
        }
        List<CashierOrderProductEntity> projects = cashierOrderProductService.findAllByOrderId(order.getId());
        if (order.getCategory() == CashierOrderCategory.COUNTED.getCode()) {
            // 计次开卡
            projects.forEach(x -> {
                int saleQuantity = StockUtil.parseBigDecimal(x.getSaleQuantity()).intValue();
                memberTimeCardService.created(order.getOperationId(), order.getMemberId(), order.getOrderNo(), saleQuantity, x.getSkuId());
            });
        } else if (order.getCategory() == CashierOrderCategory.RECHARGE.getCode()) {
            // 会员储值
            projects.forEach(x -> {
                int saleQuantity = StockUtil.parseBigDecimal(x.getSaleQuantity()).intValue();
                memberAssetsService.investRecharge(order.getMerchantNo(), order.getStoreNo(), order.getMemberId(),
                        order.getOrderNo(), saleQuantity, x.getSkuId());
            });
        }
    }
}
