package com.mammon.clerk.domain.vo;

import com.mammon.auth.domain.vo.LoginVo;
import com.mammon.clerk.domain.enums.AccountScanStatus;
import com.mammon.enums.IEnum;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/5/23 13:55
 */
@Data
public class AccountScanStatusVo {

    private int status;

    private String statusName;

    private LoginVo loginVo;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), AccountScanStatus.class);
    }
}
