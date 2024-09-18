package com.mammon.stock.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.stock.domain.enums.PurchaseRefundStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StockPurchaseRefundVo {

    private String id;

    private long merchantNo;

    //采购门店
    private long storeNo;

    private String storeName;

    private String refundNo;

    /**
     * 采购单号
     */
    private String purchaseId;

    private String purchaseNo;

    private long refundTotal;

    /**
     * 退货状态
     */
    private int status;

    private String statusName;

    /**
     * 关闭原因
     */
    private String errDesc;

    private String reasonId;

    private String reasonName;

    /**
     * 操作人
     */
    private String operationId;

    private String operationName;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<StockPurchaseRefundSkuVo> skus = new ArrayList<>();

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), PurchaseRefundStatus.class);
    }
}
