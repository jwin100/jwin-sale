package com.mammon.office.edition.domain.enums;

import com.mammon.office.edition.domain.vo.AbilityCodeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-06 14:31:46
 */
public class AbilityCodeConst {

    // 功能
    public static final int 商品分类 = 1;
    public static final int 商品规格 = 2;
    public static final int 商品标签 = 3;
    public static final int 商品单位 = 4;
    public static final int 商品资料 = 5;
    public static final int 门店商品 = 6;
    public static final int 收银 = 7;
    public static final int 挂单 = 8;
    public static final int 订单信息 = 9;
    public static final int 会员管理 = 10;
    public static final int 会员等级设置 = 11;
    public static final int 会员标签 = 12;
    public static final int 会员设置 = 13;
    public static final int 采购商品 = 14;
    public static final int 调拨商品 = 15;
    public static final int 库存管理 = 16;
    public static final int 出入库原因 = 17;
    public static final int 会员储值 = 18;
    public static final int 会员计次 = 19;
    public static final int 会员储值规则查看 = 20;
    public static final int 会员计次规则查看 = 21;
    public static final int 短信发送 = 22;
    public static final int 短信签名 = 23;
    public static final int 商品模板 = 24;
    public static final int 短信设置 = 25;
    public static final int 门店管理 = 26;
    public static final int 店员管理 = 27;
    public static final int 岗位信息 = 28;
    public static final int 岗位设置 = 29;
    public static final int 账号中心 = 30;

    /**
     * 获取所有功能
     *
     * @return
     */
    public static List<AbilityCodeModel> allAbility() {
        List<String> emptyPaths = new ArrayList<>();
        return new ArrayList<AbilityCodeModel>() {{
            add(new AbilityCodeModel(商品分类, "商品分类", emptyPaths));
            add(new AbilityCodeModel(商品规格, "商品规格", emptyPaths));
            add(new AbilityCodeModel(商品标签, "商品标签", emptyPaths));
            add(new AbilityCodeModel(商品单位, "商品单位", emptyPaths));
            add(new AbilityCodeModel(商品资料, "商品资料", emptyPaths));
            add(new AbilityCodeModel(门店商品, "门店商品", emptyPaths));
            add(new AbilityCodeModel(收银, "收银", emptyPaths));
            add(new AbilityCodeModel(挂单, "挂单", emptyPaths));
            add(new AbilityCodeModel(订单信息, "订单信息", emptyPaths));
            add(new AbilityCodeModel(会员管理, "会员管理", emptyPaths));
            add(new AbilityCodeModel(会员等级设置, "会员等级设置", emptyPaths));
            add(new AbilityCodeModel(会员标签, "会员标签", emptyPaths));
            add(new AbilityCodeModel(会员设置, "会员设置", emptyPaths));
            add(new AbilityCodeModel(采购商品, "采购商品", emptyPaths));
            add(new AbilityCodeModel(调拨商品, "调拨商品", emptyPaths));
            add(new AbilityCodeModel(库存管理, "库存管理", emptyPaths));
            add(new AbilityCodeModel(出入库原因, "出入库原因", emptyPaths));
            add(new AbilityCodeModel(会员储值, "会员储值", emptyPaths));
            add(new AbilityCodeModel(会员计次, "会员计次", emptyPaths));
            add(new AbilityCodeModel(会员储值规则查看, "会员储值规则查看", emptyPaths));
            add(new AbilityCodeModel(会员计次规则查看, "会员计次规则查看", emptyPaths));
            add(new AbilityCodeModel(短信发送, "短信发送", emptyPaths));
            add(new AbilityCodeModel(短信签名, "短信签名", emptyPaths));
            add(new AbilityCodeModel(商品模板, "商品模板", emptyPaths));
            add(new AbilityCodeModel(短信设置, "短信设置", emptyPaths));
            add(new AbilityCodeModel(门店管理, "门店管理", emptyPaths));
            add(new AbilityCodeModel(店员管理, "店员管理", emptyPaths));
            add(new AbilityCodeModel(岗位信息, "岗位信息", emptyPaths));
            add(new AbilityCodeModel(岗位设置, "岗位设置", emptyPaths));
            add(new AbilityCodeModel(账号中心, "账号中心", emptyPaths));
        }};
    }

    /**
     * 收费功能
     *
     * @return
     */
    public static List<AbilityCodeModel> chargeAbility() {
        return new ArrayList<AbilityCodeModel>() {{
            List<String> hangList = new ArrayList<String>() {{
                add("order-hang:hang");
            }};
            List<String> memberLevelList = new ArrayList<String>() {{
                add("member-level:edit");
            }};
            List<String> memberRechargeList = new ArrayList<String>() {{
                add("market:recharge-rule:create");
                add("market:recharge-rule:edit");
            }};

            List<String> memberTimeCardList = new ArrayList<String>() {{
                add("market:time-card-rule:create");
                add("market:time-card-rule:edit");
            }};

            List<String> roleList = new ArrayList<String>() {{
                add("security:role:create");
                add("security:role:edit");
                add("security:role:delete");
            }};

            add(new AbilityCodeModel(挂单, "挂单", hangList));
            add(new AbilityCodeModel(会员等级设置, "会员等级设置", memberLevelList));
            add(new AbilityCodeModel(会员储值, "会员储值", memberRechargeList));
            add(new AbilityCodeModel(会员计次, "会员计次", memberTimeCardList));
            add(new AbilityCodeModel(岗位设置, "岗位设置", roleList));
        }};
    }
}
