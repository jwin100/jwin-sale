package com.mammon.stock.domain.dto;

import lombok.Data;

@Data
public class StockRecordReasonDto {

    private String reasonName;

    /**
     * 0:入库，1:出库
     */
    private int ioType;

    private String remark;
}
