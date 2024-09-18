package com.mammon.biz.enums;

/**
 * @author dcl
 * @date 2022-11-08 11:28:14
 */
public class FileTypeConst {

    public static final int FILE_TYPE_GOODS = 1;

    public static final int FILE_TYPE_FEEDBACK = 2;

    public static final int FILE_TYPE_LOGO = 3;

    public static final String GOODS_PACKAGE = "/goods";
    public static final String FEEDBACK_PACKAGE = "/feedback";
    public static final String LOGO_PACKAGE = "/logo";
    public static final String DEFAULT_PACKAGE = "/default";

    public static String getFileName(long merchantNo, int type) {
        if (type == FILE_TYPE_GOODS) {
            return String.format("%s/%s", GOODS_PACKAGE, merchantNo);
        }
        if (type == FILE_TYPE_FEEDBACK) {
            return FEEDBACK_PACKAGE;
        }
        if (type == FILE_TYPE_LOGO) {
            return LOGO_PACKAGE;
        }
        return DEFAULT_PACKAGE;
    }
}
