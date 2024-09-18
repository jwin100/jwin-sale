package com.mammon.clerk.domain.enums;

import java.util.ArrayList;
import java.util.List;

public class AccountLoginPlatformConst {

    public static final int 电脑网页版 = 1;

    public static final List<Integer> platformList = new ArrayList<>();

    static {
        platformList.add(电脑网页版);
    }

    public static String getPlatformDesc(int platform) {
        switch (platform) {
            case 电脑网页版:
                return "电脑网页版";
            default:
                return "未知";
        }
    }
}
