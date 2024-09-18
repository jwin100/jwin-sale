package com.mammon.stock.domain.vo;

import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.stock.domain.enums.StockIoType;
import com.mammon.stock.domain.enums.StockRecordType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StockRecordVo {
    private String id;

    private long merchantNo;

    private long storeNo;

    private String storeName;

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

    private String typeName;

    /**
     * 原因(原因不为空，则单据类型为其他)
     */
    private String reasonId;

    private String reasonName;

    /**
     * 出入库类型()0:入库,1:出库
     */
    private int ioType;

    private String ioTypeName;

    private int status;

    private String statusName;

    private String operationId;

    private String operationName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<StockRecordSkuVo> products = new ArrayList<>();


    public String getTypeName() {
        return IEnum.getNameByCode(this.getType(), StockRecordType.class);
    }

    public String getIoTypeName() {
        return IEnum.getNameByCode(this.getIoType(), StockIoType.class);
    }

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }
}
