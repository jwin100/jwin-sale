package com.mammon.goods.channel.wanwei.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/4/11 18:34
 */
@Data
public class WanWeiBarcodeDetailVo {

    private boolean flag;

    private String remark;

    /**
     * 条形码
     */
    private String code;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 厂商
     */
    private String manuName;

    /**
     * 规格
     */
    private String spec;

    /**
     * 参考价格(单位:元)
     */
    private String price;

    /**
     * 商标/品牌名称
     */
    private String trademark;

    /**
     * 商品图片
     */
    private String img;

    @JsonProperty("ret_code")
    private String retCode;

    /**
     * 商品分类
     */
    private String goodsType;

    /**
     * 条码图片
     */
    private String sptmImg;

    /**
     * 原产地
     */
    private String ycg;

    private String engName;

    /**
     * 备注信息
     */
    private String note;
}
