package com.mammon.sms.channel.fish.enums;

public class FishReceiptErrConst {
    public static final String 超频 = "CONTROL";
    public static final String 黑名单 = "FY:BLAC";
    public static final String 携号转网 = "MUSTNOT";
    public static final String 被拦截 = "DELIVRD";
    public static final String 屏蔽关键词 = "sgip001";
    public static final String 通道返回的拦截 = "BADSIGN";
    public static final String 关键字拦截 = "WX-FAIL";

    public static String getName(String review) {
        switch (review) {
            case 超频:
                return "发送频率超频";
            case 黑名单:
                return "黑名单";
            case 携号转网:
                return "已携号转网";
            case 被拦截:
                return "被拦截";
            case 屏蔽关键词:
                return "屏蔽关键词";
            case 通道返回的拦截:
                return "通道返回的拦截";
            case 关键字拦截:
                return "关键字拦截";
            default:
                return review;
        }
    }
}
