package com.mammon.cashier.service;

import cn.hutool.core.util.DesensitizedUtil;
import com.mammon.cashier.domain.entity.*;
import com.mammon.cashier.domain.enums.*;
import com.mammon.cashier.domain.query.CashierRefundQuery;
import com.mammon.cashier.domain.query.CashierRefundSummaryQuery;
import com.mammon.cashier.domain.vo.*;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.exception.CustomException;
import com.mammon.cashier.dao.CashierRefundDao;
import com.mammon.member.domain.vo.MemberInfoVo;
import com.mammon.member.service.MemberService;
import com.mammon.merchant.domain.entity.MerchantEntity;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.payment.domain.enums.PayModeConst;
import com.mammon.print.domain.dto.PrintActiveDto;
import com.mammon.print.domain.dto.PrintActiveProductDto;
import com.mammon.print.domain.dto.PrintActiveProductItemDto;
import com.mammon.print.domain.dto.PrintRecordSendDto;
import com.mammon.print.domain.enums.PrintTemplateType;
import com.mammon.print.service.PrintRecordService;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.clerk.service.AccountService;
import com.mammon.sms.service.SmsTemplateSettingService;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.DateUtil;
import com.mammon.utils.StockUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CashierRefundService {

    @Resource
    private CashierRefundProductService cashierRefundProductService;

    @Resource
    private CashierRefundPayService cashierRefundPayService;

    @Resource
    private AccountService accountService;

    @Resource
    private CashierRefundDao cashierRefundDao;

    @Resource
    private CashierOrderService cashierOrderService;

    @Resource
    private CashierOrderProductService cashierOrderProductService;

    @Resource
    private SmsTemplateSettingService smsTemplateSettingService;

    @Resource
    private MerchantService merchantService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private MemberService memberService;

    @Resource
    private PrintRecordService printRecordService;

    /**
     * 退货单打印
     *
     * @param merchantNo 商户编号
     * @param accountId  会员账号ID
     * @param id         退货单ID
     * @throws CustomException 当退货单信息错误、退货单状态信息错误、获取门店信息错误时抛出异常
     */
    public void refundPrint(long merchantNo, String accountId, String id) {
        CashierRefundDetailVo refund = findById(id);
        if (refund == null) {
            throw new CustomException("退货单信息错误，打印失败");
        }
        if (refund.getStatus() == CashierRefundStatus.REFUND_CANCEL.getCode()) {
            throw new CustomException("退货单状态信息错误，打印失败");
        }
        MerchantEntity merchant = merchantService.findByMerchantNo(refund.getMerchantNo());
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(refund.getMerchantNo(), refund.getStoreNo());
        if (merchant == null || storeVo == null) {
            throw new CustomException("获取门店信息错误，打印失败");
        }

        String payName = refund.getPays().stream().map(CashierRefundPayVo::getPayCodeName).collect(Collectors.joining(","));
        List<PrintActiveProductItemDto> skus = refund.getSkus().stream().map(x -> {
            PrintActiveProductItemDto itemDto = new PrintActiveProductItemDto();
            itemDto.setName(x.getSkuName());
            itemDto.setQuantity(String.valueOf(x.getRefundQuantity()));
            itemDto.setReferenceAmount(x.getReferenceAmount().toPlainString());
            itemDto.setRealAmount(x.getPayableAmount().toPlainString());
            return itemDto;
        }).collect(Collectors.toList());
        long productTotal = refund.getSkus().stream().mapToLong(x -> StockUtil.parse(x.getRefundQuantity())).sum();
        PrintActiveProductDto product = new PrintActiveProductDto();
        product.setItems(skus);
        product.setProductTotal(StockUtil.parseBigDecimal(productTotal).toPlainString());
        product.setRealAmount(refund.getRealityAmount().toPlainString());

        PrintActiveDto printDto = new PrintActiveDto();
        printDto.setMerchantName(merchant.getMerchantName());
        printDto.setStoreName(storeVo.getStoreName());
        printDto.setBuyerRemark(refund.getRemark());
        printDto.setRealAmount(refund.getRealityAmount().toPlainString());
        printDto.setPayTypeName(payName);
        if (refund.getMember() != null) {
            printDto.setMemberName(refund.getMember().getName());
            printDto.setMemberPhone(DesensitizedUtil.mobilePhone(refund.getMember().getPhone()));
            printDto.setMemberIntegral(String.valueOf(refund.getMember().getNowIntegral()));
            printDto.setMemberRecharge(String.valueOf(refund.getMember().getNowRecharge()));
        }
        printDto.setCreateOrderTime(DateUtil.format(refund.getCreateTime()));
        if (refund.getOperationAccount() != null) {
            printDto.setOperationName(refund.getOperationAccount().getName());
        }
        List<String> address = new ArrayList<>();
        address.add(storeVo.getProvinceName());
        address.add(storeVo.getCityName());
        address.add(storeVo.getAreaName());
        address.add(storeVo.getAddress());
        String shopAddress = address.stream().filter(Objects::nonNull).collect(Collectors.joining(","));
        printDto.setShopAddress(shopAddress);
        printDto.setOrderNo(refund.getRefundNo());
        printDto.setProduct(product);

        PrintRecordSendDto dto = new PrintRecordSendDto();
        dto.setOrderNo(refund.getRefundNo());
        dto.setType(PrintTemplateType.CASHIER_REFUND_TICKET.getCode());
        dto.setContents(printDto);
        dto.setContents(printDto);
        printRecordService.send(refund.getMerchantNo(), refund.getStoreNo(), accountId, dto);
    }

    public void save(CashierRefundEntity entity) {
        cashierRefundDao.save(entity);
    }

    public void updateRefundPayType(String id, int refundMode, String payType) {
        cashierRefundDao.updateRefundPayType(id, refundMode, payType);
    }

    public void refundSubmit(String id) {
        cashierRefundDao.updateRefundStatus(id, CashierRefundStatus.REFUND_SUBMIT.getCode(), null);
    }

    public void refundFinish(String id) {
        cashierRefundDao.updateRefundStatus(id, CashierRefundStatus.REFUND_FINISH.getCode(), null);
    }

    public void refundCancel(String id, String message) {
        cashierRefundDao.updateRefundStatus(id, CashierRefundStatus.REFUND_CANCEL.getCode(), message);
    }

    public void updateRefundAmount(long merchantNo, String id, long realityAmount) {
        cashierRefundDao.updateRefundAmount(merchantNo, id, realityAmount);
    }

    public List<CashierRefundEntity> findAllByOrderId(long merchantNo, long storeNo, String orderId) {
        return cashierRefundDao.findAllByOrderId(merchantNo, storeNo, orderId);
    }

    public PageVo<CashierRefundListVo> getPage(long merchantNo, long storeNo, String accountId,
                                               CashierRefundQuery dto) {
        int total = cashierRefundDao.countPage(merchantNo, null, null, dto);
        if (total <= 0) {
            return PageResult.of();
        }
        List<CashierRefundEntity> refunds = cashierRefundDao.findPage(merchantNo, null, null, dto);
        return PageResult.of(dto.getPageIndex(), dto.getPageSize(), total, convertList(refunds));
    }

    private List<CashierRefundListVo> convertList(List<CashierRefundEntity> refunds) {
        List<String> refundIds = refunds.stream().map(CashierRefundEntity::getId).collect(Collectors.toList());
        List<String> orderIds = refunds.stream().map(CashierRefundEntity::getOrderId).distinct().collect(Collectors.toList());
        List<CashierRefundProductEntity> refundProducts = cashierRefundProductService.findAllByRefundIds(refundIds);
        List<CashierRefundPayEntity> refundPays = cashierRefundPayService.findAllByRefundIds(refundIds);
        List<CashierOrderVo> orders = cashierOrderService.findAllByIds(orderIds);

        return refunds.stream().map(refund -> {
            CashierRefundListVo vo = new CashierRefundListVo();
            BeanUtils.copyProperties(refund, vo);
            vo.setOriginalAmount(AmountUtil.parseBigDecimal(refund.getOriginalAmount()));
            vo.setAdjustAmount(AmountUtil.parseBigDecimal(refund.getAdjustAmount()));
            vo.setPayableAmount(AmountUtil.parseBigDecimal(refund.getPayableAmount()));
            vo.setRealityAmount(AmountUtil.parseBigDecimal(refund.getRealityAmount()));
            vo.setSkus(convertProducts(refund.getId(), refundProducts));
            vo.setPays(convertPays(refund.getId(), refundPays));
            vo.setMember(convertMember(refund.getMemberId()));
            vo.setOperationAccount(convertOperationAccount(refund.getOperationId()));
            orders.stream().filter(order -> order.getId().equals(refund.getOrderId()))
                    .findFirst().ifPresent(order -> {
                        vo.setOrderNo(order.getOrderNo());
                        vo.setType(order.getType());
                    });
            return vo;
        }).collect(Collectors.toList());
    }

    public CashierRefundEntity findBaseById(String id) {
        return cashierRefundDao.findById(id);
    }

    public CashierRefundDetailVo findById(String id) {
        CashierRefundEntity entity = cashierRefundDao.findById(id);
        if (entity == null) {
            return null;
        }
        List<CashierRefundProductEntity> refundProducts = cashierRefundProductService.findAllByRefundId(id);
        List<CashierRefundPayEntity> refundPays = cashierRefundPayService.findAllByRefundId(id);
        CashierOrderVo order = cashierOrderService.findById(entity.getOrderId());

        CashierRefundDetailVo vo = new CashierRefundDetailVo();
        BeanUtils.copyProperties(entity, vo);
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(entity.getMerchantNo(), entity.getStoreNo());
        if (storeVo != null)
            vo.setStoreName(storeVo.getStoreName());
        vo.setOriginalAmount(AmountUtil.parseBigDecimal(entity.getOriginalAmount()));
        vo.setAdjustAmount(AmountUtil.parseBigDecimal(entity.getAdjustAmount()));
        vo.setPayableAmount(AmountUtil.parseBigDecimal(entity.getPayableAmount()));
        vo.setRealityAmount(AmountUtil.parseBigDecimal(entity.getRealityAmount()));
        vo.setSkus(convertProducts(entity.getId(), refundProducts));
        vo.setPays(convertPays(entity.getId(), refundPays));
        vo.setMember(convertMember(entity.getMemberId()));
        vo.setOperationAccount(convertOperationAccount(entity.getOperationId()));
        if (order != null) {
            vo.setOrderNo(order.getOrderNo());
        }
        return vo;
    }

    public List<CashierRefundEntity> summaryList(CashierRefundSummaryQuery query) {
        if (query.getEndDate() != null) {
            query.setEndDate(query.getEndDate().plusDays(1));
        }
        return cashierRefundDao.summary(query);
    }

    private List<CashierRefundProductVo> convertProducts(String id, List<CashierRefundProductEntity> products) {
        return products.stream().filter(y -> y.getRefundId().equals(id))
                .map(y -> {
                    CashierRefundProductVo productVo = new CashierRefundProductVo();
                    BeanUtils.copyProperties(y, productVo);
                    productVo.setRefundQuantity(StockUtil.parseBigDecimal(y.getRefundQuantity()));
                    productVo.setReferenceAmount(AmountUtil.parseBigDecimal(y.getReferenceAmount()));
                    productVo.setAdjustAmount(AmountUtil.parseBigDecimal(y.getAdjustAmount()));
                    productVo.setPayableAmount(AmountUtil.parseBigDecimal(y.getPayableAmount()));
                    return productVo;
                }).collect(Collectors.toList());
    }

    private List<CashierRefundPayVo> convertPays(String id, List<CashierRefundPayEntity> pays) {
        return pays.stream()
                .filter(y -> y.getRefundId().equals(id))
                .map(y -> {
                    CashierRefundPayVo payVo = new CashierRefundPayVo();
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
}
