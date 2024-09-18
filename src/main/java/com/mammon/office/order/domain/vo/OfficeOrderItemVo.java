package com.mammon.office.order.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.office.edition.domain.enums.IndustryType;
import com.mammon.office.edition.domain.enums.IndustryUnit;
import com.mammon.office.order.domain.enums.OfficeOrderItemStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-03-06 09:50:16
 */
@Data
public class OfficeOrderItemVo {

    private String id;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 套餐包id
     */
    private String spuId;

    private String skuId;

    private String skuName;

    /**
     * 数量
     */
    private long quantity;

    /**
     * 付款金额
     */
    private BigDecimal payableAmount;

    /**
     * 单位
     */
    private int unit;

    private String unitName;

    /**
     * 业务类型(版本，短信，门店额度)
     */
    private int type;

    private String typeName;

    /**
     * 状态(1：未生效:2：已生效，3：生效失败)
     */
    private int status;

    private String statusName;

    /**
     * 生效描述
     */
    private String activeMessage;

    private String remark;

    /**
     * 生效日期
     */
    private LocalDateTime activeTime;

    /**
     * 绑定到门店
     */
    private long bindStoreNo;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    private LocalDateTime updateTime;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), OfficeOrderItemStatus.class);
    }

    public String getTypeName() {
        return IEnum.getNameByCode(this.getType(), IndustryType.class);
    }

    public String getUnitName() {
        return IEnum.getNameByCode(this.getUnit(), IndustryUnit.class);
    }
}
