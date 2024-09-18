package com.mammon.trade.model.vo;

import com.mammon.enums.IEnum;
import com.mammon.trade.model.enums.TradePayMode;
import com.mammon.trade.model.enums.TradePayWay;
import com.mammon.trade.model.enums.TradeStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/3/5 10:12
 */
@Data
public class TradeVo {

    /**
     * 商户订单号
     */
    private String orderNo;

    /**
     * 支付中心流水号
     */
    private String tradeNo;

    /**
     * 交易状态
     */
    private int status;

    private String statusName;

    /**
     * 订单交易模式
     */
    private int payMode;

    private String payModeName;

    /**
     * 支付方式
     */
    private int payWay;

    private String payWayName;

    /**
     * 订单总金额
     */
    private long orderAmount;

    /**
     * 成功退款金额
     */
    private long refundAmount;

    /**
     * 交易成功时间
     */
    private LocalDateTime successTime;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), TradeStatus.class);
    }

    public String getPayModeName() {
        return IEnum.getNameByCode(this.getPayMode(), TradePayMode.class);
    }

    public String getPayWayName() {
        return IEnum.getNameByCode(this.getPayWay(), TradePayWay.class);
    }
}
