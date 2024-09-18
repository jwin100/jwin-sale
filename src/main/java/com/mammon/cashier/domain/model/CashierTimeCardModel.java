package com.mammon.cashier.domain.model;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-05-30 15:01:29
 */
@Data
public class CashierTimeCardModel {

    private String productId;

    private String timeCardId;

    private long timeCardTotal;
}
