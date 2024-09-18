package com.mammon.merchant.domain.entity;

import lombok.Data;

/**
 * 地区
 */
@Data
public class RegionEntity {

    private String id;

    private String pid;

    private String name;

    private String code;

    private int level;

    private double longitude;

    private double latitude;

    private String idPath;

    private String namePath;
}
