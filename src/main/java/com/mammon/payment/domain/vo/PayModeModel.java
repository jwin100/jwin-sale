package com.mammon.payment.domain.vo;

import lombok.Data;

@Data
public class PayModeModel {

    private int code;

    private String name;

    private String desc;

    private String icon;

    private int sort;

    /**
     * 是否第三方支付
     */
    private boolean thirdParty;

    /**
     * 是否开通支付
     */
    private boolean thirdAuth;

    /**
     * 是否仅会员使用
     */
    private boolean memberOnly;

    /**
     * 是否验证密码
     */
    private boolean validPassword;

    /**
     * 同组互斥
     */
    private String groupCate;
}
