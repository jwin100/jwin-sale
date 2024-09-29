package com.mammon.cashier.domain.vo;

import com.mammon.clerk.domain.enums.AccountCashMode;
import com.mammon.enums.IEnum;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/9/28 23:19
 */
@Data
public class CashierSettingVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String accountId;

    /**
     * 移动端收银模式
     */
    private int mobileCashMode;

    private String mobileCashModeName;

    public String getMobileCashModeName() {
        return IEnum.getNameByCode(this.getMobileCashMode(), AccountCashMode.class);
    }
}
