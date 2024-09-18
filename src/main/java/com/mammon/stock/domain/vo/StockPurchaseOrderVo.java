package com.mammon.stock.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.stock.domain.enums.PurchaseOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StockPurchaseOrderVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String storeName;

    /**
     * 采购单号
     */
    private String purchaseNo;

    /**
     * 采购门店
     */
    private long purchaseStoreNo;

    private String purchaseStoreName;

    /**
     * 采购总数
     */
    private long purchaseTotal;

    /**
     * 采购件数
     */
    private long purchaseSkuTotal;

    /**
     * 采购状态
     */
    private int status;

    private String statusName;

    /**
     * 关闭原因
     */
    private String errDesc;

    /**
     * 有退
     */
    private int refundMark;

    /**
     * 操作人
     */
    private String operationId;

    private String operationName;

    /**
     * 送达日期
     */
    private LocalDateTime deliveryTime;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<StockPurchaseOrderSkuVo> skus = new ArrayList<>();

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), PurchaseOrderStatus.class);
    }
}
