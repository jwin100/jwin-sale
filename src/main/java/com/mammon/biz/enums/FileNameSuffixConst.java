package com.mammon.biz.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2024/3/14 10:51
 */
public class FileNameSuffixConst {

    private static final String JPG_LOWER = ".jpg";
    private static final String JPEG = ".jpeg";
    private static final String PNG = ".png";
    private static final String JPG_UPPER = ".JPG";

    public static List<String> fileNameSuffixList;

    static {
        fileNameSuffixList = new ArrayList<>();
        fileNameSuffixList.add(JPG_LOWER);
        fileNameSuffixList.add(JPEG);
        fileNameSuffixList.add(PNG);
        fileNameSuffixList.add(JPG_UPPER);
    }
}
