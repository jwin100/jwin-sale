package com.mammon.cashier.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-04-05 22:40:20
 */
@Data
public class CashierTimeCardDto {

    /**
     * 计次卡id
     */
    private String timeCardId;

    /**
     * 消费次数
     */
    private long timeCardNum;

    /**
     * 计次卡消费对应商品id
     */
    private String skuId;
}
