package com.mammon.payment.domain.enums;

import com.mammon.payment.domain.vo.PayModeModel;

import java.util.ArrayList;
import java.util.List;

public class PayModeConst {
    public static PayModeModel payModeCash;
    public static PayModeModel payModeBankCard;
    public static PayModeModel payModeAuthCode;
    public static PayModeModel payModeNative;
    public static PayModeModel payModeStamp;
    public static PayModeModel payModeRecharge;
    public static PayModeModel payModeTimeCard;

    public static List<PayModeModel> payModeModels;
    public static List<PayModeModel> cashierOrderPayModeModels;
    public static List<PayModeModel> cashierRefundPayModeModels;
    public static List<PayModeModel> cashierMemberModeWayModels;

    static {
        payModeCash = new PayModeModel();
        payModeCash.setCode(1);
        payModeCash.setName("cash");
        payModeCash.setDesc("现金");
        payModeCash.setIcon("icon-cash");
        payModeCash.setSort(1);
        payModeCash.setThirdParty(false);
        payModeCash.setMemberOnly(false);
        payModeCash.setValidPassword(false);
        payModeCash.setGroupCate("cash");

        payModeBankCard = new PayModeModel();
        payModeBankCard.setCode(2);
        payModeBankCard.setName("bank");
        payModeBankCard.setDesc("银行卡");
        payModeBankCard.setIcon("icon-bank");
        payModeBankCard.setSort(2);
        payModeBankCard.setThirdParty(false);
        payModeBankCard.setMemberOnly(false);
        payModeBankCard.setValidPassword(false);
        payModeBankCard.setGroupCate("bankCard");

        payModeAuthCode = new PayModeModel();
        payModeAuthCode.setCode(3);
        payModeAuthCode.setName("auth");
        payModeAuthCode.setDesc("扫码付");
        payModeAuthCode.setIcon("icon-auth");
        payModeAuthCode.setSort(3);
        payModeAuthCode.setThirdParty(true);
        payModeAuthCode.setMemberOnly(false);
        payModeAuthCode.setValidPassword(false);
        payModeAuthCode.setGroupCate("thirdParty");

        payModeNative = new PayModeModel();
        payModeNative.setCode(4);
        payModeNative.setName("native");
        payModeNative.setDesc("收款码");
        payModeNative.setIcon("icon-native");
        payModeNative.setSort(4);
        payModeNative.setThirdParty(true);
        payModeNative.setMemberOnly(false);
        payModeNative.setValidPassword(false);
        payModeNative.setGroupCate("thirdParty");

        payModeStamp = new PayModeModel();
        payModeStamp.setCode(5);
        payModeStamp.setName("stamp");
        payModeStamp.setDesc("标记支付");
        payModeStamp.setIcon("icon-stamp");
        payModeStamp.setSort(5);
        payModeStamp.setThirdParty(false);
        payModeStamp.setMemberOnly(false);
        payModeStamp.setValidPassword(false);
        payModeStamp.setGroupCate("stamp");

        payModeRecharge = new PayModeModel();
        payModeRecharge.setCode(6);
        payModeRecharge.setName("stored");
        payModeRecharge.setDesc("储值卡");
        payModeRecharge.setIcon("icon-stored");
        payModeRecharge.setSort(6);
        payModeRecharge.setThirdParty(false);
        payModeRecharge.setMemberOnly(true);
        payModeRecharge.setValidPassword(true);
        payModeRecharge.setGroupCate("storedCard");

        payModeTimeCard = new PayModeModel();
        payModeTimeCard.setCode(7);
        payModeTimeCard.setName("counted");
        payModeTimeCard.setDesc("计次卡");
        payModeTimeCard.setIcon("icon-counted");
        payModeTimeCard.setSort(7);
        payModeTimeCard.setThirdParty(false);
        payModeTimeCard.setMemberOnly(true);
        payModeTimeCard.setValidPassword(true);
        payModeTimeCard.setGroupCate("timeCard");

        payModeModels = new ArrayList<PayModeModel>() {{
            add(payModeCash);
            add(payModeBankCard);
            add(payModeStamp);
            add(payModeAuthCode);
            add(payModeNative);
            add(payModeRecharge);
            add(payModeTimeCard);
        }};

        cashierOrderPayModeModels = new ArrayList<PayModeModel>() {{
            add(payModeCash);
            add(payModeBankCard);
            add(payModeStamp);
            add(payModeNative);
            add(payModeRecharge);
        }};

        cashierRefundPayModeModels = new ArrayList<PayModeModel>() {{
            add(payModeCash);
            add(payModeBankCard);
            add(payModeStamp);
            add(payModeNative);
            add(payModeRecharge);
            add(payModeTimeCard);
        }};

        cashierMemberModeWayModels = new ArrayList<PayModeModel>() {{
            add(payModeCash);
            add(payModeBankCard);
            add(payModeStamp);
            add(payModeAuthCode);
            add(payModeNative);
        }};
    }
}
