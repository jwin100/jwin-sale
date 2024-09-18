package com.mammon.cashier.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CashierHangDto {

    /**
     * 挂单名（为空则自动生成）
     */
    private String name;

    /**
     * 挂单备注
     */
    private String remark;

    /**
     * 购物车内容
     */
    private CashierHangDetailDto detail;
}
