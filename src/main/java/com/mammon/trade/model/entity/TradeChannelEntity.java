package com.mammon.trade.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/3/4 15:20
 */
@Data
public class TradeChannelEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String channelCode;

    private String channelName;

    /**
     * 支付渠道配置信息(json)
     */
    private String channelConfig;

    private int status;

    private LocalDateTime createTime;

    private int deleted;
}
