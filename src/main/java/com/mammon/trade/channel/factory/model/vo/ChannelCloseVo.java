package com.mammon.trade.channel.factory.model.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/6 10:20
 */
@Data
public class ChannelCloseVo {

    private String tradeNo;

    /**
     * TradeStatus
     * <p>
     * 交易状态
     */
    private int status;

    /**
     * 状态描述
     */
    private String describe;
}
