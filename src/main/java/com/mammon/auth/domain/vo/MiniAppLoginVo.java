package com.mammon.auth.domain.vo;

import com.mammon.auth.domain.enums.WechatLoginStatus;
import com.mammon.enums.IEnum;
import lombok.Data;

/**
 * @author dcl
 * @since 2023/12/19 19:11
 */
@Data
public class MiniAppLoginVo {

    /**
     * 1：待绑定手机号，2：登录成功
     */
    private int code;

    private String codeName;

    private String openId;

    private String accessToken;

    private String refreshToken;

    public String getCodeName() {
        return IEnum.getNameByCode(this.getCode(), WechatLoginStatus.class);
    }
}
