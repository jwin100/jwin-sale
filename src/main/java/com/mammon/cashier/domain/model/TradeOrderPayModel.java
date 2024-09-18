package com.mammon.cashier.domain.model;

import com.mammon.cashier.domain.enums.TradeOrderPayStatus;
import lombok.Data;

/**
 * @author dcl
 * @date 2023-04-04 15:25:45
 */
@Data
public class TradeOrderPayModel {
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
    private String tradeNo;

    /**
     * 预支付返回信息
     */
    private String credential;

    public static TradeOrderPayModel success(String tradeNo, String credential) {
        return new TradeOrderPayModel(TradeOrderPayStatus.SUCCESS, tradeNo, credential, TradeOrderPayStatus.SUCCESS.getName());
    }

    public static TradeOrderPayModel waitPayment(String tradeNo, String credential, String message) {
        return new TradeOrderPayModel(TradeOrderPayStatus.WAITING, tradeNo, credential, message);
    }

    public static TradeOrderPayModel failed(String tradeNo, String message) {
        return new TradeOrderPayModel(TradeOrderPayStatus.FAILED, tradeNo, null, message);
    }

    public TradeOrderPayModel(TradeOrderPayStatus status, String tradeNo, String credential, String describe) {
        this.status = status.getCode();
        this.describe = describe;
        this.tradeNo = tradeNo;
        this.credential = credential;
    }
}
