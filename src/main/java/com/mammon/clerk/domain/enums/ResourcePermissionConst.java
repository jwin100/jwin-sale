package com.mammon.clerk.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dcl
 * @date 2023-03-09 11:51:49
 */
public class ResourcePermissionConst {

    /**
     * 按钮权限编码常量
     */
    private static Map<String, String> map = new HashMap<>();

    static {
        map.put("good:classify:edit", "编辑");
        map.put("good:classify:import", "导入");
        map.put("good:spec:edit", "编辑");
        map.put("good:tag:edit", "编辑");
        map.put("good:unit:edit", "编辑");

        map.put("good:spu:edit", "编辑");
        map.put("good:spu:import", "导入");
        map.put("good:spu:detail", "查看");

        map.put("stock:store:detail", "查看");

        map.put("stock:purchase-order:edit", "编辑采购订单");
        map.put("stock:purchase-order:examine", "审核采购订单");
        map.put("stock:purchase-order:replenish", "入库采购订单");
        map.put("stock:purchase-order:detail", "查看采购订单");
        map.put("stock:purchase-refund:edit", "编辑采购退货");
        map.put("stock:purchase-refund:examine", "审核采购退货");
        map.put("stock:purchase-refund:expend", "采购退货出库");
        map.put("stock:purchase-refund:detail", "查看采购退货");

        map.put("stock:allocate:edit", "新增");
        map.put("stock:allocate:examine", "审核");
        map.put("stock:allocate:expend", "出库");
        map.put("stock:allocate:replenish", "入库");
        map.put("stock:allocate:detail", "查看");

        map.put("stock:record:edit", "编辑");
        map.put("stock:record:detail", "查看");

        map.put("stock:record-reason:edit", "编辑");

        map.put("sale:cashier:time-card", "计次卡收银");
        map.put("sale:cashier:hang", "挂单");

        map.put("sale:flow:edit", "号牌设置");
        map.put("sale:ignore:edit", "抹零设置");
        map.put("sale:discount:edit", "折扣设置");

        map.put("order:sale:refund", "退货");

        map.put("customer:member:edit", "编辑");
        map.put("customer:member:import", "导入");
        map.put("customer:member:export", "导出");
        map.put("customer:member:detail", "查看");

        map.put("member-attr:new-reward:edit", "新会员奖励");
        map.put("member-attr:convert-integral:edit", "积分换算");
        map.put("member-attr:level:edit", "会员等级设置");
        map.put("member-attr:tag:edit", "会员标签设置");

        map.put("market:recharge-rule:edit", "编辑");
        map.put("market:time-card-rule:edit", "编辑");

        map.put("sms:record:send", "短信发送");
        map.put("sms:record:detail", "查看");

        map.put("sms:sign:edit", "编辑");
        map.put("sms:temp:edit", "编辑");

        map.put("sms-attr:sign:edit", "编辑");
        map.put("sms-attr:temp:edit", "编辑");
        map.put("sms-attr:send:edit", "编辑");

        map.put("print:template:edit", "编辑");
        map.put("print:terminal:edit", "编辑");

        map.put("shop-mall:pay", "下单购买");
        map.put("shop-mall:order:refund", "申请退款");
        map.put("shop-mall:order:detail", "查看");
        map.put("shop-mall:refund:detail", "查看");

        map.put("security:manage:edit", "编辑");
        map.put("security:manage:detail", "查看");

        map.put("security:staff:edit", "编辑");
        map.put("security:staff:detail", "查看");

        map.put("security:role:edit", "编辑");
        map.put("security:role:detail", "查看");

        map.put("security:resource:edit", "编辑");
        map.put("security:resource:detail", "查看");
    }
}
