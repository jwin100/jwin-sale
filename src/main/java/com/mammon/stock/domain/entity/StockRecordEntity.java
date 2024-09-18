package com.mammon.stock.domain.entity;

import com.mammon.stock.domain.enums.StockRecordType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存记录
 */
@Data
public class StockRecordEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 记录编号
     */
    private String recordNo;

    private String joinNo;

    /**
     * 出入库日期
     */
    private LocalDateTime operationTime;

    /**
     * @see StockRecordType
     * 单据类型，采购，调拨、销售、其他等
     */
    private int type;

    /**
     * 原因(原因不为空，则单据类型为其他)
     */
    private String reasonId;

    /**
     * 出入库类型
     */
    private int ioType;

    private int status;

    /**
     * 原因(jsonb)
     */
    private String reasonRecord;

    private String operationId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
