package com.mammon.cashier.service;

import cn.hutool.core.util.DesensitizedUtil;
import com.mammon.cashier.domain.enums.*;
import com.mammon.cashier.domain.model.*;
import com.mammon.cashier.domain.query.CashierOrderPageQuery;
import com.mammon.cashier.domain.query.CashierOrderSummaryQuery;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.exception.CustomException;
import com.mammon.goods.domain.enums.UnitType;
import com.mammon.goods.domain.vo.SpuBaseVo;
import com.mammon.goods.service.SpuService;
import com.mammon.member.domain.vo.MemberInfoVo;
import com.mammon.member.service.MemberService;
import com.mammon.merchant.domain.entity.MerchantEntity;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.cashier.dao.CashierOrderDao;
import com.mammon.cashier.domain.entity.CashierRefundProductEntity;
import com.mammon.cashier.domain.entity.CashierOrderEntity;
import com.mammon.cashier.domain.entity.CashierOrderPayEntity;
import com.mammon.cashier.domain.entity.CashierOrderProductEntity;
import com.mammon.cashier.domain.vo.*;
import com.mammon.payment.domain.enums.PayModeConst;
import com.mammon.print.domain.dto.PrintActiveDto;
import com.mammon.print.domain.dto.PrintActiveProductDto;
import com.mammon.print.domain.dto.PrintActiveProductItemDto;
import com.mammon.print.domain.dto.PrintRecordSendDto;
import com.mammon.print.domain.enums.PrintTemplateType;
import com.mammon.print.service.PrintRecordService;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.clerk.service.AccountService;
import com.mammon.stock.domain.dto.StockStoreLockDto;
import com.mammon.stock.service.StockSkuLockService;
import com.mammon.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CashierOrderService {
    @Resource
    private CashierOrderDao cashierOrderDao;

    @Resource
    private CashierOrderProductService cashierOrderProductService;

    @Resource
    private MemberService memberService;

    @Resource
    private CashierOrderPayService cashierOrderPayService;

    @Resource
    private CashierRefundProductService cashierRefundProductService;

    @Resource
    private AccountService accountService;

    @Resource
    private MerchantService merchantService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private StockSkuLockService stockSkuLockService;

    @Resource
    private PrintRecordService printRecordService;

    @Resource
    private SpuService spuService;

    public void save(CashierOrderEntity entity) {
        cashierOrderDao.save(entity);
    }

    /**
     * 取消订单
     */
    public void orderCancel(String id) {
        CashierOrderVo order = findById(id);
        if (order == null) {
            throw new CustomException("订单信息错误");
        }
        if (order.getStatus() == CashierOrderStatus.FINISH.getCode()) {
            throw new CustomException("订单已完成，不可取消");
        }
        if (order.getStatus() == CashierOrderStatus.CLOSE.getCode()) {
            return;
        }
        List<CashierOrderProductEntity> products = cashierOrderProductService.findAllByOrderId(id);
        List<CashierOrderPayEntity> pays = cashierOrderPayService.findAllByOrderId(id);
        if (pays.stream().anyMatch(x -> x.getStatus() == CashierOrderPayStatus.PAY_SUCCESS.getCode())) {
            // 调用订单退款，退回金额，默认原路退回
            pays.forEach(x -> {
                // 把支付成功的，调用退款

            });
        }
        orderClose(id, "手动取消订单");
        callbackStock(order.getMerchantNo(), order.getStoreNo(), products);
    }


    /**
     * 删除订单
     *
     * @param merchantNo
     * @param id
     */
    public void orderDelete(long merchantNo, String id) {
        cashierOrderDao.delete(merchantNo, id);
    }

    /**
     * 打印订单
     *
     * @param accountId
     * @param id
     */
    public void orderPrint(String id) {
        CashierOrderDetailVo order = findDetailById(id);
        if (order == null) {
            throw new CustomException("订单信息错误，打印失败");
        }
        if (order.getStatus() != CashierOrderStatus.FINISH.getCode()) {
            throw new CustomException("订单状态错误，打印失败");
        }
        if (order.getCategory() != CashierOrderCategory.GOODS.getCode()) {
            throw new CustomException("非商品销售订单，不打印小票");
        }
        MerchantEntity merchant = merchantService.findByMerchantNo(order.getMerchantNo());
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(order.getMerchantNo(), order.getStoreNo());
        if (merchant == null || storeVo == null) {
            throw new CustomException("获取门店信息错误，打印失败");
        }

        String payName = order.getPays().stream().map(CashierOrderPayVo::getPayCodeName).collect(Collectors.joining(","));
        List<PrintActiveProductItemDto> skus = order.getSkus().stream().map(x -> {
            PrintActiveProductItemDto itemDto = new PrintActiveProductItemDto();
            itemDto.setName(x.getSkuName());
            itemDto.setQuantity(x.getSaleQuantity().toPlainString());
            itemDto.setReferenceAmount(x.getReferenceAmount().toPlainString());
            itemDto.setRealAmount(x.getPayableAmount().toPlainString());
            if (x.getDivideAmount() != null) {
                itemDto.setRealAmount(x.getDivideAmount().toPlainString());
            }
            return itemDto;
        }).collect(Collectors.toList());
        PrintActiveProductDto product = new PrintActiveProductDto();
        product.setItems(skus);
        product.setProductTotal(String.valueOf(order.getSkuTotal()));
        product.setRealAmount(order.getRealityAmount().toPlainString());

        PrintActiveDto printDto = new PrintActiveDto();
        printDto.setMerchantName(merchant.getMerchantName());
        printDto.setStoreName(storeVo.getStoreName());
        printDto.setBuyerRemark(order.getRemark());
        printDto.setDiscount(order.getDiscount() + "折");
        printDto.setIgnoreAmount(order.getIgnoreAmount().toPlainString());
        printDto.setTotalDiscountAmount(order.getPreferentialAmount().toPlainString());
        printDto.setRealAmount(order.getRealityAmount().toPlainString());
        printDto.setPayTypeName(payName);
        if (order.getMember() != null) {
            printDto.setMemberName(order.getMember().getName());
            printDto.setMemberPhone(DesensitizedUtil.mobilePhone(order.getMember().getPhone()));
            printDto.setMemberIntegral(String.valueOf(order.getMember().getNowIntegral()));
            printDto.setMemberRecharge(String.valueOf(order.getMember().getNowRecharge()));
        }
        printDto.setCreateOrderTime(DateUtil.format(order.getCashierTime()));
        if (order.getOperationAccount() != null) {
            printDto.setOperationName(order.getOperationAccount().getName());
        }
        if (!CollectionUtils.isEmpty(order.getServiceAccounts())) {
            String serviceName = order.getServiceAccounts().stream().map(UserVo::getName).collect(Collectors.joining(","));
            printDto.setServiceName(serviceName);
        }
        List<String> address = new ArrayList<>();
        address.add(storeVo.getProvinceName());
        address.add(storeVo.getCityName());
        address.add(storeVo.getAreaName());
        address.add(storeVo.getAddress());
        String shopAddress = address.stream().filter(Objects::nonNull).collect(Collectors.joining(","));
        printDto.setShopAddress(shopAddress);
        printDto.setOrderNo(order.getOrderNo());
        printDto.setProduct(product);

        PrintRecordSendDto dto = new PrintRecordSendDto();
        dto.setOrderNo(order.getOrderNo());
        dto.setType(PrintTemplateType.CASHIER_ORDER_TICKET.getCode());
        dto.setContents(printDto);
        printRecordService.send(order.getMerchantNo(), order.getStoreNo(), order.getOperationId(), dto);
    }

    /**
     * 修改支付方式
     *
     * @param id
     * @param orderPay
     */
    public void updateOrderPayType(String id, CashierOrderPayEntity orderPay) {
        CashierOrderVo entity = findById(id);
        if (entity == null) {
            return;
        }
        CashierPayTypeModel model = new CashierPayTypeModel();
        model.setPayId(orderPay.getId());
        model.setPayCode(orderPay.getPayCode());

        List<CashierPayTypeModel> models = JsonUtil.toList(entity.getPayType(), CashierPayTypeModel.class);
        models.add(model);
        cashierOrderDao.updateOrderPayType(id, JsonUtil.toJSONString(models));
    }

    /**
     * 订单完成
     *
     * @param orderId
     */
    public void orderFinish(String orderId) {
        cashierOrderDao.updateOrderStatus(orderId, CashierOrderStatus.FINISH.getCode(), null);
    }

    /**
     * 订单部分付款
     *
     * @param orderId
     */
    public void orderPartial(String orderId) {
        cashierOrderDao.updateOrderStatus(orderId, CashierOrderStatus.PARTIAL.getCode(), null);
    }

    /**
     * 订单关闭
     *
     * @param orderId
     * @param message
     */
    public void orderClose(String orderId, String message) {
        cashierOrderDao.updateOrderStatus(orderId, CashierOrderStatus.CLOSE.getCode(), message);
    }

    public void updateOrderAmount(String orderId, long realityAmount) {
        cashierOrderDao.updateOrderAmount(orderId, realityAmount);
    }

    public void updateOrderRefund(long merchantNo, String id, int refundMark, long refundAmount, long refundIntegral) {
        cashierOrderDao.updateOrderRefund(merchantNo, id, refundMark, refundAmount, refundIntegral);
    }

    public CashierOrderEntity findBaseById(String id) {
        return cashierOrderDao.findById(id);
    }

    public CashierOrderVo findById(String id) {
        CashierOrderEntity entity = findBaseById(id);
        if (entity == null) {
            return null;
        }
        CashierOrderVo vo = new CashierOrderVo();
        BeanUtils.copyProperties(entity, vo);
        convertAmount(vo, entity);
        return vo;
    }

    public List<CashierOrderVo> findAllByIds(List<String> ids) {
        List<CashierOrderEntity> list = cashierOrderDao.findByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(x -> {
            CashierOrderVo vo = new CashierOrderVo();
            BeanUtils.copyProperties(x, vo);
            convertAmount(vo, x);
            return vo;
        }).collect(Collectors.toList());
    }

    public CashierOrderDetailVo findDetailById(String id) {
        CashierOrderEntity order = cashierOrderDao.findById(id);
        if (order == null) {
            throw new CustomException("订单不存在");
        }
        return convertDetail(order);
    }

    public PageVo<CashierOrderListVo> getPage(long merchantNo, long storeNo, String accountId,
                                              CashierOrderPageQuery query) {
        int total = cashierOrderDao.countPage(merchantNo, null, null, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<CashierOrderEntity> orders = cashierOrderDao.findPage(merchantNo, null, null, query);
        if (CollectionUtils.isEmpty(orders)) {
            return PageResult.of();
        }
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, convertList(orders));
    }

    /**
     * 订单取消，回滚预扣库存
     *
     * @param merchantNo
     * @param storeNo
     * @param products
     */
    private void callbackStock(long merchantNo, long storeNo, List<CashierOrderProductEntity> products) {
        products.forEach(x -> {
            if (StringUtils.isNotBlank(x.getSkuId())) {
                StockStoreLockDto lockDto = new StockStoreLockDto();
                lockDto.setSpuId(x.getSpuId());
                lockDto.setSkuId(x.getSkuId());
                lockDto.setMerchantNo(merchantNo);
                lockDto.setStoreNo(storeNo);
                lockDto.setOrderId(x.getOrderId());
                stockSkuLockService.callbackStock(lockDto);
            }
        });
    }

    private void convertAmount(CashierOrderVo vo, CashierOrderEntity entity) {
        vo.setOriginalAmount(AmountUtil.parseBigDecimal(entity.getOriginalAmount()));
        vo.setIgnoreAmount(AmountUtil.parseBigDecimal(entity.getIgnoreAmount()));
        vo.setDiscountAmount(AmountUtil.parseBigDecimal(entity.getDiscountAmount()));
        vo.setCollectAmount(AmountUtil.parseBigDecimal(entity.getCollectAmount()));
        vo.setPreferentialAmount(AmountUtil.parseBigDecimal(entity.getPreferentialAmount()));
        vo.setAdjustAmount(AmountUtil.parseBigDecimal(entity.getAdjustAmount()));
        vo.setPayableAmount(AmountUtil.parseBigDecimal(entity.getPayableAmount()));
        vo.setRealityAmount(AmountUtil.parseBigDecimal(entity.getRealityAmount()));
        vo.setRefundAmount(AmountUtil.parseBigDecimal(entity.getRefundAmount()));
    }

    private List<CashierOrderListVo> convertList(List<CashierOrderEntity> orders) {
        List<String> orderIds = orders.stream().map(CashierOrderEntity::getId).collect(Collectors.toList());
        List<CashierOrderProductEntity> orderProducts = cashierOrderProductService.findAllByOrderIds(orderIds);
        List<CashierOrderPayEntity> orderPays = cashierOrderPayService.findAllByOrderIds(orderIds);
        List<CashierOrderPayEntity> successPays = orderPays.stream()
                .filter(x -> x.getStatus() == CashierOrderPayStatus.PAY_SUCCESS.getCode())
                .collect(Collectors.toList());
        return orders.stream().map(order -> {
            CashierOrderListVo vo = convertList(order);
            vo.setSkus(convertProducts(order.getMerchantNo(), order.getId(), orderProducts));
            vo.setPays(convertPays(order.getId(), successPays));
            vo.setSkuItem(vo.getSkus().size());
            vo.setSkuTotal(getSkuTotal(vo.getSkus()));
            vo.setMember(convertMember(vo.getMemberId()));
            return vo;
        }).collect(Collectors.toList());
    }

    private long getSkuTotal(List<CashierOrderProductVo> skus) {
        long weightCount = skus.stream()
                .filter(x -> x.getUnitType() == UnitType.WEIGHT.getCode())
                .count();
        long amountCount = skus.stream()
                .filter(x -> x.getUnitType() == UnitType.NUMBER.getCode())
                .mapToLong(x -> StockUtil.parse(x.getSaleQuantity()))
                .sum();
        BigDecimal amount = StockUtil.parseBigDecimal(amountCount);
        return weightCount + amount.longValue();
    }

    private CashierOrderDetailVo convertDetail(CashierOrderEntity order) {
        List<CashierOrderProductEntity> orderProducts = cashierOrderProductService.findAllByOrderId(order.getId());
        List<CashierOrderPayEntity> orderPays = cashierOrderPayService.findAllByOrderId(order.getId());
        orderPays = orderPays.stream()
                .filter(x -> x.getStatus() == CashierOrderPayStatus.PAY_SUCCESS.getCode())
                .collect(Collectors.toList());
        CashierOrderDetailVo vo = convertBase(order);
        vo.setSkus(convertProducts(order.getMerchantNo(), order.getId(), orderProducts));
        vo.setPays(convertPays(order.getId(), orderPays));
        vo.setServiceAccounts(convertServiceAccounts(order.getMerchantNo(), order.getServiceAccountIds()));
        vo.setOperationAccount(convertOperationAccount(order.getOperationId()));
        vo.setMember(convertMember(order.getMemberId()));
        vo.setSkuItem(vo.getSkus().size());
        vo.setSkuTotal(getSkuTotal(vo.getSkus()));
        return vo;
    }

    private CashierOrderDetailVo convertBase(CashierOrderEntity order) {
        CashierOrderDetailVo vo = new CashierOrderDetailVo();
        BeanUtils.copyProperties(order, vo);

        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(order.getMerchantNo(), order.getStoreNo());
        if (storeVo != null)
            vo.setStoreName(storeVo.getStoreName());
        vo.setOriginalAmount(AmountUtil.parseBigDecimal(order.getOriginalAmount()));
        vo.setIgnoreAmount(AmountUtil.parseBigDecimal(order.getIgnoreAmount()));
        vo.setDiscount(QuantityUtil.parseBigDecimal(order.getDiscount()));
        vo.setDiscountAmount(AmountUtil.parseBigDecimal(order.getDiscountAmount()));
        vo.setPreferentialAmount(AmountUtil.parseBigDecimal(order.getPreferentialAmount()));
        vo.setCollectAmount(AmountUtil.parseBigDecimal(order.getCollectAmount()));
        vo.setAdjustAmount(AmountUtil.parseBigDecimal(order.getAdjustAmount()));
        vo.setPayableAmount(AmountUtil.parseBigDecimal(order.getPayableAmount()));
        vo.setRealityAmount(AmountUtil.parseBigDecimal(order.getRealityAmount()));
        vo.setRefundAmount(AmountUtil.parseBigDecimal(order.getRefundAmount()));
        return vo;
    }

    private CashierOrderListVo convertList(CashierOrderEntity order) {
        CashierOrderListVo vo = new CashierOrderListVo();
        BeanUtils.copyProperties(order, vo);
        vo.setOriginalAmount(AmountUtil.parseBigDecimal(order.getOriginalAmount()));
        vo.setIgnoreAmount(AmountUtil.parseBigDecimal(order.getIgnoreAmount()));
        vo.setDiscount(QuantityUtil.parseBigDecimal(order.getDiscount()));
        vo.setDiscountAmount(AmountUtil.parseBigDecimal(order.getDiscountAmount()));
        vo.setPreferentialAmount(AmountUtil.parseBigDecimal(order.getPreferentialAmount()));
        vo.setCollectAmount(AmountUtil.parseBigDecimal(order.getCollectAmount()));
        vo.setAdjustAmount(AmountUtil.parseBigDecimal(order.getAdjustAmount()));
        vo.setPayableAmount(AmountUtil.parseBigDecimal(order.getPayableAmount()));
        vo.setRealityAmount(AmountUtil.parseBigDecimal(order.getRealityAmount()));
        vo.setRefundAmount(AmountUtil.parseBigDecimal(order.getRefundAmount()));
        return vo;
    }

    private List<CashierOrderProductVo> convertProducts(long merchantNo, String orderId, List<CashierOrderProductEntity> products) {
        return products.stream().filter(y -> y.getOrderId().equals(orderId))
                .map(y -> {
                    CashierOrderProductVo productVo = new CashierOrderProductVo();
                    BeanUtils.copyProperties(y, productVo);
                    productVo.setSaleQuantity(StockUtil.parseBigDecimal(y.getSaleQuantity()));
                    productVo.setReferenceAmount(AmountUtil.parseBigDecimal(y.getReferenceAmount()));
                    productVo.setPayableAmount(AmountUtil.parseBigDecimal(y.getPayableAmount()));
                    productVo.setDivideAmount(AmountUtil.parseBigDecimal(y.getDivideAmount()));
                    productVo.setRefundQuantity(StockUtil.parseBigDecimal(y.getRefundQuantity()));
                    if (y.getRefundQuantity() > 0) {
                        List<CashierRefundProductEntity> refundProducts = cashierRefundProductService
                                .findAllByOrderProductId(y.getId());
                        if (!CollectionUtils.isEmpty(refundProducts)) {
                            long payableAmount = refundProducts.stream()
                                    .mapToLong(CashierRefundProductEntity::getPayableAmount).sum();
                            productVo.setRefundAmount(AmountUtil.parseBigDecimal(payableAmount));
                        }
                    }
                    SpuBaseVo spuVo = spuService.findBaseById(merchantNo, y.getSpuId());
                    if (spuVo != null) {
                        productVo.setUnitId(spuVo.getUnitId());
                        productVo.setUnitName(spuVo.getUnitName());
                        productVo.setUnitType(spuVo.getUnitType());
                    }
                    return productVo;
                }).collect(Collectors.toList());
    }

    private List<CashierOrderPayVo> convertPays(String orderId, List<CashierOrderPayEntity> pays) {
        return pays.stream()
                .filter(y -> y.getOrderId().equals(orderId))
                .map(y -> {
                    CashierOrderPayVo payVo = new CashierOrderPayVo();
                    BeanUtils.copyProperties(y, payVo);
                    payVo.setPayableAmount(AmountUtil.parseBigDecimal(y.getPayableAmount()));
                    payVo.setRealityAmount(AmountUtil.parseBigDecimal(y.getRealityAmount()));
                    PayModeConst.payModeModels.stream()
                            .filter(x -> x.getCode() == y.getPayCode())
                            .findFirst().ifPresent(payTypeVo -> {
                                payVo.setPayCodeName(payTypeVo.getDesc());
                            });
                    return payVo;
                }).collect(Collectors.toList());
    }

    private List<UserVo> convertServiceAccounts(long merchantNo, String serviceAccountIds) {
        List<String> accountIds = JsonUtil.toList(serviceAccountIds, String.class);
        if (CollectionUtils.isEmpty(accountIds)) {
            return Collections.emptyList();
        }
        return accountService.findAllByIds(merchantNo, accountIds);
    }

    private UserVo convertOperationAccount(String operationId) {
        if (StringUtils.isBlank(operationId)) {
            return null;
        }
        return accountService.info(operationId);
    }

    private MemberInfoVo convertMember(String memberId) {
        if (StringUtils.isBlank(memberId)) {
            return null;
        }
        return memberService.findById(memberId);
    }

    public List<CashierOrderEntity> summaryList(CashierOrderSummaryQuery query) {
        if (query.getEndDate() != null) {
            query.setEndDate(query.getEndDate().plusDays(1));
        }
        query.setStatus(CashierOrderStatus.FINISH.getCode());
        return cashierOrderDao.summary(query);
    }

    /**
     * 获取会员最新下单记录
     *
     * @param memberId
     * @return
     */
    public CashierOrderEntity findLastByMemberId(long merchantNo, String memberId) {
        return cashierOrderDao.findLastByMemberId(merchantNo, memberId);
    }
}
