package com.mammon.stock.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StockRecordDto {

    /**
     * 出入库日期
     */
    private LocalDateTime operationTime;

    private int type;

    /**
     * 出入库类型
     */
    private int ioType;

    /**
     * 原因(原因不为空，则单据类型为其他)
     */
    private String reasonId;

    private int status;

    private List<StockRecordSkuDto> products = new ArrayList<>();
}
