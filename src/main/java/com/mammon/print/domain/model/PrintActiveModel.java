package com.mammon.print.domain.model;

import lombok.Data;

@Data
public class PrintActiveModel {

    /**
     * 打印类型， 0:文本，1:图片，2:换行,3:table
     */
    private int type;

    /**
     * 对其方式(0:左对齐，1:居中对齐，2:右对齐
     */
    private int textAlign;

    /**
     * 字体加大(0：正常不加大，1：加大一倍，2：加大二倍
     */
    private int textSize;

    /**
     * 加粗倍数，0：正常不加宽，1：加宽一倍，2：加宽2倍
     */
    private int textBold;

    /**
     * 打印内容
     */
    private String content;
}
