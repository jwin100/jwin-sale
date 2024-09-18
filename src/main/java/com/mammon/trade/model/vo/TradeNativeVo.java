package com.mammon.trade.model.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/4 14:47
 */
@Data
public class TradeNativeVo {

    private int status;

    /**
     * 状态描述
     */
    private String describe;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 收款码Url
     */
    private String codeUrl;

    /**
     * 流水号
     */
    private String tradeNo;
}
