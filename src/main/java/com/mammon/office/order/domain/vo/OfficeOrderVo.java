package com.mammon.office.order.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.office.order.domain.enums.OfficeOrderPayType;
import com.mammon.office.order.domain.enums.OfficeOrderSource;
import com.mammon.office.order.domain.enums.OfficeOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author dcl
 * @date 2023-03-05 20:38:14
 */
@Data
public class OfficeOrderVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 操作人
     */
    private String accountId;

    private String orderNo;

    private String subject;

    /**
     * 购买数量
     */
    private long quantity;

    /**
     * 支付配置信息
     */
    private String configId;

    /**
     * 应收
     */
    private BigDecimal payableAmount;

    /**
     * 支付方式
     */
    private int payType;

    private String payTypeName;

    /**
     * 订单状态(1:待支付，2：支付成功，3：支付失败，4：有退款，8：已关闭)
     */
    private int status;

    private String statusName;

    private String payMessage;

    /**
     * 订单来源
     */
    private int source;

    private String sourceName;

    private String remark;

    /**
     * 支付日期
     */
    private LocalDateTime payTime;

    /**
     * 订单创建日期
     */
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    private LocalDateTime updateTime;

    private boolean showRefund;

    public String getSourceName() {
        return IEnum.getNameByCode(this.getSource(), OfficeOrderSource.class);
    }

    public String getPayTypeName() {
        return IEnum.getNameByCode(this.getPayType(), OfficeOrderPayType.class);
    }

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), OfficeOrderStatus.class);
    }

    public boolean isShowRefund() {
        LocalDate now = LocalDate.now();
        if (this.getPayTime()!=null && this.getPayTime().toLocalDate().until(now, ChronoUnit.DAYS) <= 15) {
            if (this.getStatus() == OfficeOrderStatus.paySuccess.getCode()
                    || this.getStatus() == OfficeOrderStatus.payFinish.getCode()) {
                return true;
            }
        }
        return false;
    }
}
