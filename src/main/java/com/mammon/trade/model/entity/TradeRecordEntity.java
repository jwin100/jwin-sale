package com.mammon.trade.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 付款记录
 *
 * @author dcl
 * @since 2024/3/4 15:44
 */
@Data
public class TradeRecordEntity {

    // 流水id, 付款请求信息、付款结果

    private String id;

    private String tradeNo;

    /**
     * 操作类型(1:支付，2：查单，3：关单，4：撤单)
     */
    private int type;

    /**
     * 请求信息
     */
    private String reqContent;

    /**
     * 返回结果信息
     */
    private String respContent;

    /**
     * 返回结果码
     */
    private String respCode;

    /**
     * 返回结果描述
     */
    private String respDescribe;

    /**
     * 请求时间
     */
    private LocalDateTime reqTime;

    /**
     * 返回结果时间
     */
    private LocalDateTime respTime;

    private LocalDateTime createTime;
}
