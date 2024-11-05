package com.mammon.sms.service;

import com.mammon.cashier.domain.enums.CashierOrderCategory;
import com.mammon.cashier.domain.enums.CashierOrderStatus;
import com.mammon.cashier.domain.vo.CashierOrderDetailVo;
import com.mammon.cashier.domain.vo.CashierRefundDetailVo;
import com.mammon.cashier.service.CashierOrderService;
import com.mammon.cashier.service.CashierRefundService;
import com.mammon.clerk.service.AccountService;
import com.mammon.enums.IEnum;
import com.mammon.exception.CustomException;
import com.mammon.member.domain.entity.MemberEntity;
import com.mammon.member.domain.entity.MemberTimeCardEntity;
import com.mammon.member.domain.vo.MemberInfoVo;
import com.mammon.member.service.MemberService;
import com.mammon.member.service.MemberTimeCardService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.sms.domain.dto.MemberCountedNoticeDto;
import com.mammon.sms.domain.dto.MemberRechargeNoticeDto;
import com.mammon.sms.domain.dto.SmsSendNoticeDto;
import com.mammon.sms.domain.dto.SmsSendUserDto;
import com.mammon.sms.enums.SmsTempTypeEnum;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信异步通知
 * <p>
 * 商户通知类短信都走这儿发
 * <p>
 * 1.会员注册
 * <p>
 * 2.会员储值
 * <p>
 * 3.储值变动
 * <p>
 * 4.会员计次卡
 * <p>
 * 5.计次卡余额变动
 * <p>
 * 6.销售通知
 * <p>
 * 7.退货通知
 * <p>
 * 8.会员预约
 * <p>
 * 9.生日祝福
 *
 * @author dcl
 * @since 2024/6/18 16:48
 */
@Service
@Slf4j
public class SmsSendNoticeService {

    @Resource
    private MemberService memberService;

    @Resource
    private SmsTemplateSettingService smsTemplateSettingService;

    @Resource
    private SmsSendService smsSendService;

    @Resource
    @Lazy
    private MemberTimeCardService memberTimeCardService;

    @Resource
    @Lazy
    private CashierOrderService cashierOrderService;

    @Resource
    @Lazy
    private CashierRefundService cashierRefundService;

    public void memberRegisterSend(String memberId) {
        MemberInfoVo member = getMember(memberId, SmsTempTypeEnum.MEMBER_REGISTER);
        if (member == null) {
            return;
        }
        Map<String, String> tempParams = new HashMap<>();
        tempParams.put("memberName", member.getName());
        tempParams.put("memberSex", member.getSex() == 1 ? "先生" : "女士");
        tempParams.put("shopName", member.getStoreName());

        List<SmsSendUserDto> users = Collections.singletonList(new SmsSendUserDto(member.getId(), member.getPhone()));
        SmsSendNoticeDto dto = new SmsSendNoticeDto();
        dto.setMerchantNo(member.getMerchantNo());
        dto.setStoreNo(member.getStoreNo());
        dto.setAccountId(member.getAccountId());
        dto.setTempType(SmsTempTypeEnum.MEMBER_REGISTER.getCode());
        dto.setUsers(users);
        dto.setTempParams(tempParams);
        smsSend(dto);
    }

    /**
     * 会员储值短信通知
     *
     * @param noticeDto
     */
    public void memberRechargeSend(MemberRechargeNoticeDto noticeDto) {
        MemberInfoVo member = getMember(noticeDto.getMemberId(), SmsTempTypeEnum.MEMBER_RECHARGE);
        if (member == null) {
            return;
        }
        // 检查储值变更是否发送短信，积分变更是否发送短信，并发送
        Map<String, String> tempParams = new HashMap<>();
        tempParams.put("shopName", member.getStoreName());
        tempParams.put("realAmount", AmountUtil.parseYuan(noticeDto.getChangeRecharge()));
        tempParams.put("surplusAmount", AmountUtil.parseYuan(noticeDto.getAfterRecharge()));

        List<SmsSendUserDto> users = Collections.singletonList(new SmsSendUserDto(member.getId(), member.getPhone()));
        SmsSendNoticeDto dto = new SmsSendNoticeDto();
        dto.setMerchantNo(member.getMerchantNo());
        dto.setStoreNo(member.getStoreNo());
        dto.setAccountId(null);
        dto.setTempType(SmsTempTypeEnum.MEMBER_RECHARGE.getCode());
        dto.setUsers(users);
        dto.setTempParams(tempParams);
        smsSend(dto);
    }

    /**
     * 会员储值金额变更短信通知
     *
     * @param noticeDto
     */
    public void memberRechargeChangeSend(MemberRechargeNoticeDto noticeDto) {
        MemberInfoVo member = getMember(noticeDto.getMemberId(), SmsTempTypeEnum.MEMBER_RECHARGE_CHANGE);
        if (member == null) {
            return;
        }
        // 检查储值变更是否发送短信，积分变更是否发送短信，并发送
        Map<String, String> tempParams = new HashMap<>();
        tempParams.put("shopName", member.getStoreName());
        tempParams.put("realAmount", AmountUtil.parseYuan(noticeDto.getChangeRecharge()));
        tempParams.put("surplusAmount", AmountUtil.parseYuan(noticeDto.getAfterRecharge()));

        List<SmsSendUserDto> users = Collections.singletonList(new SmsSendUserDto(member.getId(), member.getPhone()));
        SmsSendNoticeDto dto = new SmsSendNoticeDto();
        dto.setMerchantNo(member.getMerchantNo());
        dto.setStoreNo(member.getStoreNo());
        dto.setAccountId(null);
        dto.setTempType(SmsTempTypeEnum.MEMBER_RECHARGE_CHANGE.getCode());
        dto.setUsers(users);
        dto.setTempParams(tempParams);
        smsSend(dto);
    }

    public void memberCountedCreateSend(MemberCountedNoticeDto noticeDto) {
        MemberInfoVo member = getMember(noticeDto.getMemberId(), SmsTempTypeEnum.MEMBER_COUNTED);
        if (member == null) {
            return;
        }
        //发送订单成功短信
        Map<String, String> tempParams = new HashMap<>();
        tempParams.put("memberName", member.getName());
        tempParams.put("memberSex", member.getSex() == 0 ? "女士" : "先生");
        tempParams.put("shopName", member.getStoreName());
        tempParams.put("timeCardName", noticeDto.getTimeCardName());
        tempParams.put("purchaseTimeCard", String.valueOf(noticeDto.getAddTimeCard()));
        tempParams.put("memberTimeTotal", String.valueOf(noticeDto.getNowTimeCard()));

        List<SmsSendUserDto> users = Collections.singletonList(new SmsSendUserDto(member.getId(), member.getPhone()));
        SmsSendNoticeDto dto = new SmsSendNoticeDto();
        dto.setMerchantNo(member.getMerchantNo());
        dto.setStoreNo(member.getStoreNo());
        dto.setAccountId(null);
        dto.setTempType(SmsTempTypeEnum.MEMBER_COUNTED.getCode());
        dto.setUsers(users);
        dto.setTempParams(tempParams);
        smsSend(dto);
    }

    /**
     * 计次卡变更短信通知
     *
     * @param countedId
     */
    public void timeCardConsumeSms(String countedId) {
        MemberTimeCardEntity counted = memberTimeCardService.findById(countedId);
        if (counted == null) {
            log.info("计次卡信息获取错误.memberId:{}", counted);
            return;
        }
        MemberInfoVo member = getMember(counted.getMemberId(), SmsTempTypeEnum.MEMBER_COUNTED_CHANGE);
        if (member == null) {
            return;
        }
        Map<String, String> tempParams = new HashMap<>();
        tempParams.put("memberName", member.getName());
        tempParams.put("memberSex", member.getSex() == 1 ? "先生" : "女士");
        tempParams.put("shopName", member.getStoreName());
        tempParams.put("surplusTotal", String.valueOf(counted.getNowTimeCard()));

        List<SmsSendUserDto> users = Collections.singletonList(new SmsSendUserDto(member.getId(), member.getPhone()));
        SmsSendNoticeDto dto = new SmsSendNoticeDto();
        dto.setMerchantNo(member.getMerchantNo());
        dto.setStoreNo(member.getStoreNo());
        dto.setAccountId(null);
        dto.setTempType(SmsTempTypeEnum.MEMBER_COUNTED_CHANGE.getCode());
        dto.setUsers(users);
        dto.setTempParams(tempParams);
        smsSend(dto);
    }

    /**
     * 销售订单短信通知
     *
     * @param orderId
     */
    public void cashierOrderSend(String orderId) {
        CashierOrderDetailVo order = cashierOrderService.findDetailById(orderId);
        if (order == null || StringUtils.isBlank(order.getMemberId())) {
            log.info("{}订单信息或会员信息为空，销售订单短信通知不发送", orderId);
            return;
        }
        if (order.getStatus() != CashierOrderStatus.FINISH.getCode()) {
            log.info("{}订单信息未完成，销售订单短信通知不发送.{}", orderId, JsonUtil.toJSONString(order));
            return;
        }
        if (order.getCategory() != CashierOrderCategory.GOODS.getCode()) {
            log.info("{}订单不是商品销售订单，销售订单短信不发送", order.getId());
            return;
        }
        MemberInfoVo member = getMember(order.getMemberId(), SmsTempTypeEnum.ORDER_CASHIER);
        if (member == null) {
            // 会员信息不存在，不发送短信
            return;
        }
        Map<String, String> tempParams = new HashMap<>();
        tempParams.put("memberName", member.getName());
        tempParams.put("memberSex", member.getSex() == 0 ? "女士" : "先生");
        tempParams.put("memberRealAmount", order.getPayableAmount().toPlainString());
        tempParams.put("shopName", order.getStoreName());

        List<SmsSendUserDto> users = Collections.singletonList(new SmsSendUserDto(member.getId(), member.getPhone()));
        SmsSendNoticeDto dto = new SmsSendNoticeDto();
        dto.setMerchantNo(order.getMerchantNo());
        dto.setStoreNo(order.getStoreNo());
        dto.setAccountId(order.getOperationId());
        dto.setTempType(SmsTempTypeEnum.ORDER_CASHIER.getCode());
        dto.setUsers(users);
        dto.setTempParams(tempParams);
        smsSend(dto);
    }

    /**
     * 销售退款通知
     *
     * @param refundId
     */
    public void cashierRefundSend(String refundId) {
        CashierRefundDetailVo refund = cashierRefundService.findById(refundId);
        if (refund == null || StringUtils.isBlank(refund.getMemberId())) {
            log.info("{}退单信息或会员信息为空，销售退单短信通知不发送", refundId);
            return;
        }
        CashierOrderDetailVo order = cashierOrderService.findDetailById(refund.getOrderId());
        if (order == null) {
            log.info("{}退单信息为空，销售退单短信通知不发送", refundId);
            return;
        }
        if (order.getCategory() != CashierOrderCategory.GOODS.getCode()) {
            log.info("不是商品销售订单，销售短信不发送");
            return;
        }
        MemberInfoVo member = getMember(refund.getMemberId(), SmsTempTypeEnum.ORDER_REFUND);
        if (member == null) {
            return;
        }
        //发送订单成功短信
        long payableAmount = refund.getPays().stream().mapToLong(x -> AmountUtil.parse(x.getPayableAmount())).sum();
        Map<String, String> tempParams = new HashMap<>();
        tempParams.put("memberName", member.getName());
        tempParams.put("memberSex", member.getSex() == 0 ? "女士" : "先生");
        tempParams.put("memberRealAmount", String.format("%s", AmountUtil.parseBigDecimal(payableAmount)));
        tempParams.put("shopName", refund.getStoreName());

        List<SmsSendUserDto> users = Collections.singletonList(new SmsSendUserDto(member.getId(), member.getPhone()));
        SmsSendNoticeDto dto = new SmsSendNoticeDto();
        dto.setMerchantNo(refund.getMerchantNo());
        dto.setStoreNo(refund.getStoreNo());
        dto.setAccountId(refund.getOperationId());
        dto.setTempType(SmsTempTypeEnum.ORDER_REFUND.getCode());
        dto.setUsers(users);
        dto.setTempParams(tempParams);
        smsSend(dto);
    }

    private MemberInfoVo getMember(String memberId, SmsTempTypeEnum tempType) {
        MemberInfoVo member = memberService.findById(memberId);
        if (member == null) {
            log.info("{}会员不存在，短信发送失败", memberId);
            return null;
        }
        if (!validSmsSend(member.getMerchantNo(), tempType.getCode())) {
            log.info("{}未开启，{}会员短信发送失败", tempType.getName(), memberId);
            return null;
        }
        return member;
    }

    /**
     * 验证是否设置了发送
     *
     * @param merchantNo
     * @param tempType
     * @return
     */
    private boolean validSmsSend(long merchantNo, int tempType) {
        return smsTemplateSettingService.isSmsSend(merchantNo, tempType);
    }

    private void smsSend(SmsSendNoticeDto dto) {
        try {
            log.info("{}短信通知发送", IEnum.getNameByCode(dto.getTempType(), SmsTempTypeEnum.class));
            smsSendService.asyncSend(dto);
        } catch (CustomException e) {
            log.error("短信发送失败：{}", e.getResultJson());
        }
    }
}
