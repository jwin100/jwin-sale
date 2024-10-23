package com.mammon.office.order.service;

import com.mammon.common.Generate;
import com.mammon.common.PageVo;
import com.mammon.common.PageResult;
import com.mammon.enums.IEnum;
import com.mammon.leaf.enums.DocketType;
import com.mammon.exception.CustomException;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.office.edition.domain.enums.IndustryType;
import com.mammon.office.order.dao.OfficeRefundDao;
import com.mammon.office.order.domain.dto.OfficeOrderRefundDto;
import com.mammon.office.order.domain.entity.OfficeRefundEntity;
import com.mammon.office.order.domain.entity.OfficePayChannelEntity;
import com.mammon.office.order.domain.enums.OfficeOrderPayType;
import com.mammon.office.order.domain.enums.OfficeOrderRefundStatus;
import com.mammon.office.order.domain.enums.OfficeOrderStatus;
import com.mammon.office.order.domain.query.OfficeOrderRefundQuery;
import com.mammon.office.order.domain.vo.*;
import com.mammon.sms.domain.entity.SmsEntity;
import com.mammon.sms.service.SmsService;
import com.mammon.utils.AmountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2023-03-06 14:05:33
 */
@Service
@Slf4j
public class OfficeRefundService {

    @Resource
    private OfficeRefundDao officeRefundDao;

    @Resource
    private OfficeOrderService officeOrderService;

    @Resource
    private OfficeOrderItemService officeOrderItemService;

    @Resource
    private SmsService smsService;

    @Resource
    private OfficePayChannelService officePayChannelService;

    @Resource
    private OfficeOrderActiveService officeOrderActiveService;

    @Resource
    private LeafCodeService leafCodeService;

    public OfficeRefundComputeVo compute(long merchantNo, long storeNo, String accountId, String orderId) {
        OfficeRefundComputeVo vo = new OfficeRefundComputeVo();
        // 获取订单，
        OfficeOrderVo orderVo = officeOrderService.findById(merchantNo, orderId);
        if (orderVo == null) {
            throw new CustomException("订单不存在");
        }
        if ((orderVo.getStatus() != OfficeOrderStatus.paySuccess.getCode()
                && orderVo.getStatus() != OfficeOrderStatus.payFinish.getCode())
                || orderVo.getPayTime() == null) {
            throw new CustomException("订单支付信息错误");
        }
        LocalDate now = LocalDate.now();
        long diffDays = orderVo.getPayTime().toLocalDate().until(now, ChronoUnit.DAYS);
        if (diffDays > 15) {
            throw new CustomException("订单购买已超过15天，无法退款");
        }
        vo.setOrderId(orderId);
        vo.setOrderNo(orderVo.getOrderNo());
        // 如果订单中有短信，查看是否扣除短信使用量
        List<OfficeOrderItemVo> orderItems = officeOrderItemService.findAllByOrderId(orderId);
        List<OfficeRefundComputeItemVo> itemVos = orderItems.stream().map(x -> {
            OfficeRefundComputeItemVo itemVo = new OfficeRefundComputeItemVo();
            BeanUtils.copyProperties(x, itemVo);
            itemVo.setRefundQuantity(x.getQuantity());
            itemVo.setDeductAmount(BigDecimal.ZERO);
            itemVo.setRefundAmount(x.getPayableAmount());
            if (x.getType() == IndustryType.sms.getCode()) {
                SmsEntity sms = smsService.smsInfo(merchantNo);
                if (sms != null && sms.getRecharge() < x.getQuantity()) {
                    long useRecharge = x.getQuantity() - sms.getRecharge();
                    BigDecimal unitAmount = AmountUtil.divide(x.getPayableAmount(), BigDecimal.valueOf(x.getQuantity()));
                    BigDecimal deductAmount = AmountUtil.multiply(unitAmount, BigDecimal.valueOf(useRecharge));
                    BigDecimal refundAmount = x.getPayableAmount().subtract(deductAmount);
                    itemVo.setRefundQuantity(sms.getRecharge());
                    itemVo.setDeductAmount(deductAmount);
                    itemVo.setDeductMessage("短信已使用：" + useRecharge + "条");
                    itemVo.setRefundAmount(refundAmount);
                }
            }
            return itemVo;
        }).collect(Collectors.toList());
        long refundAmount = itemVos.stream().mapToLong(x -> AmountUtil.parse(x.getRefundAmount())).sum();
        vo.setItems(itemVos);
        vo.setRefundAmount(AmountUtil.parseBigDecimal(refundAmount));
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(long merchantNo, long storeNo, String accountId, String orderId, OfficeOrderRefundDto dto) {
        OfficeRefundComputeVo compute = compute(merchantNo, storeNo, accountId, orderId);
        OfficeOrderVo orderVo = officeOrderService.findById(merchantNo, orderId);
        OfficeOrderPayType payType = IEnum.getByCode(orderVo.getPayType(), OfficeOrderPayType.class);
        if (payType == null || !payType.isStatus()) {
            throw new CustomException("订单支付方式错误");
        }
        OfficePayChannelEntity payChannel = officePayChannelService.findById(orderVo.getConfigId());
        if (payChannel == null) {
            throw new CustomException("支付信息错误");
        }
//        BaseTradePayChannel factory = TradePayChannelFactory.get(payType.getChannelCode());
//        if (factory == null) {
//            throw new CustomException("支付通道信息错误");
//        }

        String refundNo = leafCodeService.generateDocketNo(DocketType.CASHIER_REFUND);
        OfficeRefundEntity refund = new OfficeRefundEntity();
        refund.setId(Generate.generateUUID());
        refund.setMerchantNo(merchantNo);
        refund.setStoreNo(storeNo);
        refund.setAccountId(accountId);
        refund.setOrderId(orderId);
        refund.setRefundNo(refundNo);
        refund.setRefundAmount(AmountUtil.parse(compute.getRefundAmount()));
        refund.setStatus(OfficeOrderRefundStatus.refunding.getCode());
        refund.setConfigId(payChannel.getId());
        refund.setRefundType(payType.getCode());
        refund.setRemark(dto.getRemark());
        refund.setCreateTime(LocalDateTime.now());
        refund.setUpdateTime(LocalDateTime.now());
        officeRefundDao.save(refund);
//        TradeRefundVo refundVo = factory.tradeRefund(payChannel.getConfigStr(), orderVo.getOrderNo(), refundNo, compute.getRefundAmount());
        officeOrderActiveService.callback(merchantNo, storeNo, orderVo.getId());
        officeOrderService.editStatusById(orderId, orderVo.getStatus(), OfficeOrderStatus.refunding.getCode());
    }

    public PageVo<OfficeRefundVo> page(long merchantNo, OfficeOrderRefundQuery query) {
        //仅仅返回目录列表
        int total = officeRefundDao.countPage(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<OfficeRefundEntity> list = officeRefundDao.findPage(merchantNo, query);

        List<OfficeRefundVo> vos = list.stream().map(x -> {
            OfficeOrderVo order = officeOrderService.findById(merchantNo, x.getOrderId());

            OfficeRefundVo vo = new OfficeRefundVo();
            BeanUtils.copyProperties(x, vo);
            vo.setRefundAmount(AmountUtil.parseBigDecimal(x.getRefundAmount()));
            if (order != null) {
                vo.setSubject(order.getSubject());
                vo.setOrderNo(order.getOrderNo());
            }
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public OfficeRefundDetailVo findDetailById(long merchantNo, String refundId) {
        OfficeRefundEntity refund = officeRefundDao.findById(refundId);
        if (refund == null) {
            throw new CustomException("订单不存在");
        }
        OfficeRefundDetailVo vo = new OfficeRefundDetailVo();
        BeanUtils.copyProperties(refund, vo);
        vo.setRefundAmount(AmountUtil.parseBigDecimal(refund.getRefundAmount()));
        List<OfficeOrderItemVo> items = officeOrderItemService.findAllByOrderId(refund.getOrderId());
        vo.setItems(items);
        return vo;
    }
}
