package com.mammon.office.order.domain.entity;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-02-02 16:00:55
 */
@Data
public class OfficePayChannelEntity {

    private String id;

    /**
     * 支付方式(alipay,wechatPay)
     */
    private String code;

    private String name;

    private String configStr;
    
}
