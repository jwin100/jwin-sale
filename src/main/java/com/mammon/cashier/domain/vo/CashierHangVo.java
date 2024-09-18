package com.mammon.cashier.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CashierHangVo {

    private String id;

    /**
     * 挂单编号
     */
    private String hangNo;

    /**
     * 挂单名
     */
    private String name;

    /**
     * 挂单备注
     */
    private String remark;

    /**
     * 收银信息
     */
    private CashierHangDetailVo detail;

    /**
     * 商品总数
     */
    private long total;

    private LocalDateTime updateTime;
}
