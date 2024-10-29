package com.mammon.cashier.service;

import com.mammon.cashier.domain.entity.CashierOrderPayEntity;
import com.mammon.exception.CustomException;
import com.mammon.member.service.MemberService;
import com.mammon.payment.domain.enums.PayModeConst;
import com.mammon.sms.service.SmsSendService;
import com.mammon.sms.service.SmsTemplateSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dcl
 * @since 2024/6/4 11:45
 */
@Service
@Slf4j
public class CashierOrderTradeAsyncService {

    private final CashierOrderService cashierOrderService;

    public CashierOrderTradeAsyncService(CashierOrderService cashierOrderService) {
        this.cashierOrderService = cashierOrderService;
    }

    @Async("taskExecutor")
    public void payFinishPrint(String orderId) {
        try {
            cashierOrderService.orderPrint(orderId);
        } catch (CustomException e) {
            log.error("打印失败：{}", e.getResultJson());
        }
    }
}
