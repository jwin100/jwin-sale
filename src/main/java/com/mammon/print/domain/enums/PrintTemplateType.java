package com.mammon.print.domain.enums;

import com.mammon.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dcl
 * @since 2023/8/24 10:25
 */
@Getter
@AllArgsConstructor
public enum PrintTemplateType implements IEnum<PrintTemplateType> {
    // 购物小票，拣货小票，退款小票，交班小票，挂单小票，日结小票
    CASHIER_ORDER_TICKET(1001, PrintTemplateClassify.TICKET.getCode(), "收银小票", "收银台给顾客的小票，根据实际小票机类型，选择相应宽度的小票模板"),
    CASHIER_PICK_TICKET(1002, PrintTemplateClassify.TICKET.getCode(), "拣货小票", "用户订单拣货打包人员和后厨人员的小票"),
    CASHIER_REFUND_TICKET(1003, PrintTemplateClassify.TICKET.getCode(), "退款小票", "退款成功后打印退款小票"),
    //    HANDOVER_TICKET(1002, PrintClassifyEnum.TICKET.getCode(), "交班小票"),
//    DEPOSIT_TICKET(1003, PrintClassifyEnum.TICKET.getCode(), "寄存小票"),
//    HANG_TICKET(1004, PrintClassifyEnum.TICKET.getCode(), "挂单小票"),
    GOODS_PRICE_TAG(2001, PrintTemplateClassify.PRICE_TAG.getCode(), "商品价签", "适用于贴在商品的货架上展示价格"),
//    GOODS_BAR_CODE(3001, PrintClassify.BAR_CODE.getCode(), "商品条码"),
    ;

    private final int code;
    private final int classify;
    private final String name;
    private final String remark;
}
