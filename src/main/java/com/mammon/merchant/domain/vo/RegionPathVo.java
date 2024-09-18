package com.mammon.merchant.domain.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2023/8/9 13:41
 */
@Data
public class RegionPathVo {
    private String id;

    private String province;
    private String provinceName;

    private String city;
    private String cityName;

    private String area;
    private String areaName;
}
