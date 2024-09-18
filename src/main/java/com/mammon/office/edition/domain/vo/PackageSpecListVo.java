package com.mammon.office.edition.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author dcl
 * @since 2023/8/28 10:45
 */
@Data
public class PackageSpecListVo {

    private String id;

    private String spuId;

    private String name;

    private int sort;

    private List<PackageSpecItemVo> items;
}
