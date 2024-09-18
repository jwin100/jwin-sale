package com.mammon.stock.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.stock.domain.enums.StockIoType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 出入库原因
 */
@Data
public class StockRecordReasonVo {

    private String id;

    private long merchantNo;

    private String reasonName;

    /**
     * 0:入库，1:出库
     */
    private int ioType;

    private String ioTypeName;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    public String getIoTypeName() {
        return IEnum.getNameByCode(this.getIoType(), StockIoType.class);
    }
}
