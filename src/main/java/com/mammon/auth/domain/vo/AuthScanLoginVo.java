package com.mammon.auth.domain.vo;

import com.mammon.auth.domain.enums.ScanLoginStatus;
import com.mammon.enums.IEnum;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/5/23 17:05
 */
@Data
public class AuthScanLoginVo {

    /**
     * 扫码登录状态
     */
    private int status;

    private String statusName;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), ScanLoginStatus.class);
    }
}
