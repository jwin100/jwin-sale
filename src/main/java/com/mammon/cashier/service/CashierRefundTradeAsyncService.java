package com.mammon.cashier.service;

import com.mammon.cashier.channel.factory.TradeMemberChannel;
import com.mammon.cashier.channel.factory.TradeMemberFactory;
import com.mammon.cashier.channel.factory.dto.TradeMemberRefundDto;
import com.mammon.cashier.channel.factory.enums.TradeMemberStatus;
import com.mammon.cashier.channel.factory.vo.TradeMemberRefundVo;
import com.mammon.cashier.domain.entity.CashierRefundEntity;
import com.mammon.cashier.domain.entity.CashierRefundPayEntity;
import com.mammon.cashier.domain.enums.CashierRefundPayStatus;
import com.mammon.cashier.domain.vo.CashierRefundDetailVo;
import com.mammon.exception.CustomException;
import com.mammon.member.service.MemberService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.payment.domain.enums.PayModeConst;
import com.mammon.payment.domain.vo.PayModeModel;
import com.mammon.sms.service.SmsSendService;
import com.mammon.sms.service.SmsTemplateSettingService;
import com.mammon.trade.model.dto.TradeRefundDto;
import com.mammon.trade.model.enums.TradeRefundStatus;
import com.mammon.trade.model.vo.TradeRefundVo;
import com.mammon.trade.service.TradeRefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dcl
 * @since 2024/6/4 11:04
 */
@Service
@Slf4j
public class CashierRefundTradeAsyncService {

    private final CashierRefundPayService cashierRefundPayService;
    private final TradeRefundService tradeRefundService;
    private final CashierRefundService cashierRefundService;
    private final MemberService memberService;
    private final MerchantStoreService merchantStoreService;
    private final SmsTemplateSettingService smsTemplateSettingService;
    private final SmsSendService smsSendService;

    public CashierRefundTradeAsyncService(CashierRefundPayService cashierRefundPayService,
                                          TradeRefundService tradeRefundService, CashierRefundService cashierRefundService, MemberService memberService, MerchantStoreService merchantStoreService, SmsTemplateSettingService smsTemplateSettingService, SmsSendService smsSendService) {
        this.cashierRefundPayService = cashierRefundPayService;
        this.tradeRefundService = tradeRefundService;
        this.cashierRefundService = cashierRefundService;
        this.memberService = memberService;
        this.merchantStoreService = merchantStoreService;
        this.smsTemplateSettingService = smsTemplateSettingService;
        this.smsSendService = smsSendService;
    }

    /**
     * 异步触发退款
     * 这里只做请求，不做结果处理
     *
     * @param accountId
     * @param refundPays
     * @return
     */
    @Async("taskExecutor")
    public void tradeRefund(String accountId, CashierRefundEntity refund,
                            List<CashierRefundPayEntity> refundPays) {
        for (CashierRefundPayEntity refundPay : refundPays) {
            PayModeModel payModeModel = PayModeConst.payModeModels.stream()
                    .filter(x -> x.getCode() == refundPay.getPayCode())
                    .findFirst().orElse(null);
            if (payModeModel == null) {
                throw new CustomException("退款方式错误");
            }
            if (payModeModel.isThirdParty()) {
                // 调用第三方支付渠道退款
                tradeRefundPay(refundPay, refund);
            } else if (payModeModel.isMemberOnly()) {
                // 调用会员支付退款
                memberRefundPay(accountId, refundPay, refund);
            } else {
                // 其他如现金、标记退款等
                cashierRefundPayService.payFinish(refundPay.getId(), null);
            }
        }
        payFinish(refund.getId());
    }

    /**
     * 交易退款支付
     *
     * @param refundPay 退款支付实体
     * @param refund    退款实体
     */
    public void tradeRefundPay(CashierRefundPayEntity refundPay, CashierRefundEntity refund) {
        TradeRefundDto refundDto = new TradeRefundDto();
        refundDto.setTradeNo(refund.getOrderTradeNo());
        refundDto.setRefundAmount(refundPay.getPayableAmount());

        TradeRefundVo refundVo = tradeRefundService.tradeRefund(refundDto);
        if (refundVo.getStatus() == TradeRefundStatus.FAILED.getCode()) {
            cashierRefundPayService.payError(refundPay.getId(), refundVo.getRefundTradeNo(), refundVo.getDescribe());
        } else if (refundVo.getStatus() == TradeRefundStatus.SUBMIT.getCode()) {
            cashierRefundPayService.paySubmit(refundPay.getId(), refundVo.getRefundTradeNo());
        } else {
            cashierRefundPayService.payFinish(refundPay.getId(), refundVo.getRefundTradeNo());
        }
    }

    /**
     * 会员退款支付
     *
     * @param accountId 会员账号ID
     * @param refundPay 退款支付实体
     * @param refund    退款实体
     * @throws CustomException 当支付方式错误时抛出
     */
    public void memberRefundPay(String accountId, CashierRefundPayEntity refundPay,
                                CashierRefundEntity refund) {
        TradeMemberChannel factory = TradeMemberFactory.get(refundPay.getPayCode());
        if (factory == null) {
            throw new CustomException("支付方式错误");
        }
        TradeMemberRefundDto refundDto = new TradeMemberRefundDto();
        refundDto.setMerchantNo(refund.getMerchantNo());
        refundDto.setStoreNo(refund.getStoreNo());
        refundDto.setAccountId(accountId);
        refundDto.setRefundNo(refund.getRefundNo());
        refundDto.setOrderNo(refund.getOrderNo());
        refundDto.setMemberId(refund.getMemberId());
        refundDto.setRefundAmount(refund.getPayableAmount());
        refundDto.setCountedId(refundPay.getCountedId());
        refundDto.setCountedTotal(refundPay.getCountedTotal());
        TradeMemberRefundVo refundVo = factory.refund(refundDto);

        if (refundVo.getStatus() == TradeMemberStatus.FAILED.getCode()) {
            cashierRefundPayService.payError(refundPay.getId(), refundVo.getRefundTradeNo(), refundVo.getDescribe());
        } else {
            cashierRefundPayService.payFinish(refundPay.getId(), refundVo.getRefundTradeNo());
        }
    }

    public void payFinish(String refundId) {
        List<CashierRefundPayEntity> list = cashierRefundPayService.findAllByRefundId(refundId);
        if (list.stream().allMatch(x -> x.getStatus() == CashierRefundPayStatus.REFUND_FINISH.getCode())) {
            cashierRefundService.refundFinish(refundId);
        }
    }

    @Async("taskExecutor")
    public void refundSuccessPrint(long merchantNo, long storeNo, String accountId, String refundId) {
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
            cashierRefundService.refundPrint(merchantNo, accountId, refundId);
        } catch (CustomException e) {
            log.error("打印失败:{}", e.getResultJson());
        }
    }
}
