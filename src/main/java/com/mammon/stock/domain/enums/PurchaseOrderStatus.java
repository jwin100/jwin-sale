package com.mammon.stock.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2023/7/26 16:00
 */
@Getter
@AllArgsConstructor
public enum PurchaseOrderStatus implements IEnum<PurchaseOrderStatus> {
    WAIT_EXAMINE(1, "待审核"),
    REJECT_EXAMINE(2, "审核驳回"),
    WAIT_WARE_HOUSE(3, "待入库"),
    SOME_WARE_HOUSE(4, "部分入库"),
    WARE_HOUSED(5, "已入库"),
    CLOSE(6, "已关闭"),
    ;

    private final int code;
    private final String name;

    // 状态对应操作
    // 待审核-可修改，可审核，可关闭
    // 审核驳回-可修改，可关闭
    // 待入库-可入库
    // 部分入库-可入库，可退货
    // 已入库-可退货
    // 已关闭-只能看详情
}
