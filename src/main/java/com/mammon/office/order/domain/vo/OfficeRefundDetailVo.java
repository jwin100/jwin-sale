package com.mammon.office.order.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.office.order.domain.enums.OfficeOrderRefundStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dcl
 * @date 2023-03-06 15:10:40
 */
@Data
public class OfficeRefundDetailVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String accountId;

    private String orderId;

    private String orderNo;

    private String refundNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款方式。（原单退回）
     */
    private int refundType;

    private int status;

    private String statusName;

    private int refundMessage;

    /**
     * 退款日期
     */
    private LocalDateTime refundTime;

    private String remark;

    /**
     * 订单创建日期
     */
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    private LocalDateTime updateTime;

    private List<OfficeOrderItemVo> items;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), OfficeOrderRefundStatus.class);
    }
}
