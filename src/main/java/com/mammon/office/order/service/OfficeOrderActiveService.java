package com.mammon.office.order.service;

import com.mammon.exception.CustomException;
import com.mammon.merchant.service.MerchantIndustryService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.office.edition.domain.dto.IndustryMerchantCallbackDto;
import com.mammon.office.edition.domain.enums.IndustryType;
import com.mammon.office.edition.domain.enums.IndustryUnit;
import com.mammon.office.edition.domain.vo.PackageSkuListVo;
import com.mammon.office.edition.service.*;
import com.mammon.office.order.domain.enums.OfficeOrderItemStatus;
import com.mammon.office.order.domain.enums.OfficeOrderStatus;
import com.mammon.office.order.domain.vo.OfficeOrderItemVo;
import com.mammon.office.order.domain.vo.OfficeOrderVo;
import com.mammon.sms.enums.SmsRechargeLogTypeConst;
import com.mammon.sms.service.SmsService;
import com.mammon.office.edition.domain.dto.IndustryMerchantActiveDto;
import com.mammon.office.edition.domain.entity.IndustryEntity;
import com.mammon.office.edition.domain.vo.IndustryActiveVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单业务生效、回滚
 *
 * @author dcl
 * @date 2023-02-02 15:17:36
 */
@Service
@Slf4j
public class OfficeOrderActiveService {

    @Resource
    private OfficeOrderService officeOrderService;

    @Resource
    private OfficeOrderItemService officeOrderItemService;

    @Resource
    private PackageSpuService packageSpuService;

    @Resource
    private PackageSkuService packageSkuService;

    @Resource
    private IndustryService industryService;

    @Resource
    private MerchantIndustryService merchantIndustryService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private SmsService smsService;

    @Transactional(rollbackFor = Exception.class)
    public void active(long merchantNo, long storeNo, String orderId) {
        OfficeOrderVo orderVo = officeOrderService.findById(merchantNo, orderId);
        validPaySuccess(orderVo);
        List<OfficeOrderItemVo> orderItems = officeOrderItemService.findAllByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderItems)) {
            throw new CustomException("订单信息错误");
        }
        List<String> skuIds = orderItems.stream()
                .map(OfficeOrderItemVo::getSkuId)
                .distinct().collect(Collectors.toList());
        List<PackageSkuListVo> skus = packageSkuService.findAllByIds(skuIds);
        // 获取到packageSku 获取到industry 如果是版本，加版本，如果是短信，加短信
        List<String> industryIds = skus.stream().map(PackageSkuListVo::getIndustryId).distinct().collect(Collectors.toList());
        List<IndustryEntity> industries = industryService.findAllByIds(industryIds);

        orderItems.forEach(x -> {
            if (x.getStatus() == OfficeOrderItemStatus.waitActive.getCode()) {
                skus.stream()
                        .filter(sku -> sku.getId().equals(x.getSkuId()))
                        .findFirst()
                        .flatMap(sku ->
                                industries.stream()
                                        .filter(industry -> industry.getId().equals(sku.getIndustryId()))
                                        .findFirst())
                        .ifPresent(industry -> orderActive(merchantNo, storeNo, x, industry));
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void callback(long merchantNo, long storeNo, String orderId) {
        OfficeOrderVo orderVo = officeOrderService.findById(merchantNo, orderId);
        validPaySuccess(orderVo);
        List<OfficeOrderItemVo> orderItems = officeOrderItemService.findAllByOrderId(orderVo.getId());
        if (CollectionUtils.isEmpty(orderItems)) {
            throw new CustomException("订单信息错误");
        }
        List<String> skuIds = orderItems.stream()
                .map(OfficeOrderItemVo::getSkuId)
                .distinct().collect(Collectors.toList());
        List<PackageSkuListVo> skus = packageSkuService.findAllByIds(skuIds);
        // 获取到packageSku 获取到industry 如果是版本，加版本，如果是短信，加短信
        List<String> industryIds = skus.stream()
                .map(PackageSkuListVo::getIndustryId).distinct()
                .collect(Collectors.toList());
        List<IndustryEntity> industries = industryService.findAllByIds(industryIds);

        orderItems.forEach(x -> {
            if (x.getStatus() == OfficeOrderItemStatus.activeSuccess.getCode()) {
                skus.stream()
                        .filter(y -> y.getId().equals(x.getSkuId()))
                        .findFirst().flatMap(skuVo ->
                                industries.stream()
                                        .filter(y -> y.getId().equals(skuVo.getIndustryId()))
                                        .findFirst())
                        .ifPresent(industry -> orderCallback(x, merchantNo, storeNo, industry));
            }
        });
    }

    private void validPaySuccess(OfficeOrderVo orderVo) {
        if (orderVo == null) {
            throw new CustomException("订单不存在");
        }
        if (orderVo.getStatus() == OfficeOrderStatus.waitPay.getCode()) {
            throw new CustomException("订单未支付");
        }
        if (orderVo.getStatus() == OfficeOrderStatus.refunding.getCode()) {
            throw new CustomException("订单正在退款中");
        }
        if (orderVo.getStatus() == OfficeOrderStatus.refunded.getCode()) {
            throw new CustomException("订单已退款");
        }
        if (orderVo.getStatus() == OfficeOrderStatus.payCancel.getCode()) {
            throw new CustomException("订单支付失败无法退款");
        }
    }

    public void orderActive(long merchantNo, long storeNo,
                            OfficeOrderItemVo orderItem, IndustryEntity industry) {
        long addQuantity = orderItem.getQuantity();
        if (orderItem.getUnit() == IndustryUnit.year.getCode()) {
            addQuantity = orderItem.getQuantity() * 12;
        }

        if (orderItem.getType() == IndustryType.major_edition.getCode() ||
                orderItem.getType() == IndustryType.flagship_edition.getCode()) {
            industryActive(merchantNo, industry.getId(), addQuantity, orderItem);
        }
        if (orderItem.getType() == IndustryType.sms.getCode()) {
            smsActive(merchantNo, storeNo, addQuantity, orderItem);
        }
        if (orderItem.getType() == IndustryType.storeQuota.getCode()) {
            storeQuotaActive(merchantNo, addQuantity, orderItem);
        }
    }

    public void orderCallback(OfficeOrderItemVo orderItem, long merchantNo, long storeNo, IndustryEntity industry) {
        long addMonth = orderItem.getQuantity();
        if (orderItem.getUnit() == IndustryUnit.year.getCode()) {
            addMonth = orderItem.getQuantity() * 12;
        }

        if (orderItem.getType() == IndustryType.major_edition.getCode()) {
            industryCallback(merchantNo, industry.getId(), addMonth, orderItem);
        }
        if (orderItem.getType() == IndustryType.sms.getCode()) {
            smsCallback(merchantNo, storeNo, addMonth, orderItem);
        }
        if (orderItem.getType() == IndustryType.storeQuota.getCode()) {
            storeQuotaCallback(merchantNo, addMonth, orderItem);
        }
    }

    private void industryActive(long merchantNo, String industryId, long addMonth, OfficeOrderItemVo orderItem) {
        IndustryMerchantActiveDto activeDto = new IndustryMerchantActiveDto();
        activeDto.setMerchantNo(merchantNo);
        activeDto.setIndustryId(industryId);
        activeDto.setOrderId(orderItem.getOrderId());
        activeDto.setAddMonth(addMonth);
        activeDto.setIndustryType(orderItem.getType());
        IndustryActiveVo vo = merchantIndustryService.industryActive(activeDto);
        // 更新订单状态
        int status = vo.getStatus() == 1
                ? OfficeOrderItemStatus.activeSuccess.getCode()
                : OfficeOrderItemStatus.activeFail.getCode();
        officeOrderItemService.editStatusById(orderItem.getId(), status, vo.getMessage(), vo.getActiveTime());
    }

    private void industryCallback(long merchantNo, String industryId, long callbackMonth, OfficeOrderItemVo orderItem) {
        IndustryMerchantCallbackDto activeDto = new IndustryMerchantCallbackDto();
        activeDto.setMerchantNo(merchantNo);
        activeDto.setIndustryId(industryId);
        activeDto.setOrderId(orderItem.getOrderId());
        activeDto.setCallbackMonth(-callbackMonth);
        IndustryActiveVo vo = merchantIndustryService.industryCallback(activeDto);
        // 更新订单状态
        int status = vo.getStatus() == 1
                ? OfficeOrderItemStatus.callbackSuccess.getCode()
                : OfficeOrderItemStatus.callbackFail.getCode();
        officeOrderItemService.editStatusById(orderItem.getId(), status, vo.getMessage(), vo.getActiveTime());
    }

    private void smsActive(long merchantNo, long storeNo, long addQuantity, OfficeOrderItemVo orderItem) {
        log.info("短信开通：{},{}", merchantNo, addQuantity);
        int result = smsService.smsRechargeChange(merchantNo, storeNo, SmsRechargeLogTypeConst.充值,
                null, orderItem.getOrderId(), addQuantity, "充值");
        int smsActiveStatus = result == 1
                ? OfficeOrderItemStatus.activeSuccess.getCode()
                : OfficeOrderItemStatus.activeFail.getCode();
        officeOrderItemService.editStatusById(orderItem.getId(), smsActiveStatus, "", LocalDateTime.now());
    }

    private void smsCallback(long merchantNo, long storeNo, long callbackQuantity, OfficeOrderItemVo orderItem) {
        int result = smsService.smsRechargeChange(merchantNo, storeNo, SmsRechargeLogTypeConst.退货,
                null, orderItem.getOrderId(), -callbackQuantity, "退货");
        int smsActiveStatus = result == 1
                ? OfficeOrderItemStatus.callbackSuccess.getCode()
                : OfficeOrderItemStatus.callbackFail.getCode();
        officeOrderItemService.editStatusById(orderItem.getId(), smsActiveStatus, "", LocalDateTime.now());
    }

    private void storeQuotaActive(long merchantNo, long addMonth, OfficeOrderItemVo orderItem) {
        merchantStoreService.bindQuota(merchantNo, addMonth, orderItem.getBindStoreNo());
        officeOrderItemService.editStatusById(orderItem.getId(), OfficeOrderItemStatus.activeSuccess.getCode(),
                "", LocalDateTime.now());
    }

    private void storeQuotaCallback(long merchantNo, long addMonth, OfficeOrderItemVo orderItem) {
        merchantStoreService.bindQuota(merchantNo, -addMonth, orderItem.getBindStoreNo());
        officeOrderItemService.editStatusById(orderItem.getId(), OfficeOrderItemStatus.callbackSuccess.getCode(),
                "", LocalDateTime.now());
    }
}
