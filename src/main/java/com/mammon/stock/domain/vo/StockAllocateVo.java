package com.mammon.stock.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.stock.domain.enums.AllocateStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StockAllocateVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String storeName;

    private String allocateNo;

    /**
     * 调入方
     */
    private long inStoreNo;

    private String inStoreName;

    /**
     * 调出方
     */
    private long outStoreNo;

    private String outStoreName;

    /**
     * 调拨状态
     */
    private int status;

    private String statusName;

    private String errDesc;

    private String remark;

    private String operationId;

    private String operationName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<StockAllocateSkuVo> skus = new ArrayList<>();

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), AllocateStatus.class);
    }
}
