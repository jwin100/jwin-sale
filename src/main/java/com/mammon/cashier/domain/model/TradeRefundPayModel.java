package com.mammon.cashier.domain.model;

import com.mammon.cashier.domain.enums.TradeRefundPayStatus;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/8 14:42
 */
@Data
public class TradeRefundPayModel {

    /**
     * 支付结果
     */
    private int status;

    /**
     * 结果描述
     */
    private String describe;

    /**
     * 第三方流水号
     */
    private String refundTradeNo;

    public static TradeRefundPayModel submit(String refundTradeNo) {
        return new TradeRefundPayModel(refundTradeNo, TradeRefundPayStatus.SUBMIT, TradeRefundPayStatus.SUBMIT.getName());
    }

    public static TradeRefundPayModel success(String refundTradeNo) {
        return new TradeRefundPayModel(refundTradeNo, TradeRefundPayStatus.SUCCESS, TradeRefundPayStatus.SUCCESS.getName());
    }

    public static TradeRefundPayModel failed(String refundTradeNo, String message) {
        return new TradeRefundPayModel(refundTradeNo, TradeRefundPayStatus.FAILED, message);
    }

    public TradeRefundPayModel(String refundTradeNo, TradeRefundPayStatus status, String describe) {
        this.status = status.getCode();
        this.describe = describe;
        this.refundTradeNo = refundTradeNo;
    }
}
