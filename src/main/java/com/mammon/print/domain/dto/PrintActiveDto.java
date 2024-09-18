package com.mammon.print.domain.dto;

import lombok.Data;

@Data
public class PrintActiveDto {

    private String merchantName;

    private String storeName;

    /**
     * 买家备注
     */
    private String buyerRemark;

    private PrintActiveProductDto product = new PrintActiveProductDto();

    /**
     * 整单折扣
     */
    private String discount;

    /**
     * 抹零金额
     */
    private String ignoreAmount;

    /**
     * 总优惠金额
     */
    private String totalDiscountAmount;

    /**
     * 实收金额、退款金额
     */
    private String realAmount;

    /**
     * 支付方式、退款方式
     */
    private String payTypeName;

    /**
     * 第三方订单号
     */
    private String tradeNo;

    /**
     * 会员姓名
     */
    private String memberName;

    /**
     * 会员电话
     */
    private String memberPhone;

    /**
     * 会员积分
     */
    private String memberIntegral;

    /**
     * 储值金额
     */
    private String memberRecharge;

    /**
     * 下单时间、退款时间
     */
    private String createOrderTime;

    /**
     * 操作员
     */
    private String operationName;

    /**
     * 服务人员
     */
    private String serviceName;

    /**
     * 门店地址
     */
    private String shopAddress;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 公告
     */
    private String footerNotice;
}
