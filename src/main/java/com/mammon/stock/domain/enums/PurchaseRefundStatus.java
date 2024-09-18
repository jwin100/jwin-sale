package com.mammon.stock.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2023/7/26 16:05
 */
@Getter
@AllArgsConstructor
public enum PurchaseRefundStatus implements IEnum<PurchaseRefundStatus> {
    WAIT_EXAMINE(1, "待审核"),
    REJECT_EXAMINE(2, "审核驳回"),
    WAIT_EX_WARE_HOUSE(3, "待出库"),
    EX_WARE_HOUSED(4, "已出库"),
    CLOSE(5, "已关闭"),
    ;
    
    private final int code;
    private final String name;
}
