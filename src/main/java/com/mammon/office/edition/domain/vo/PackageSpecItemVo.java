package com.mammon.office.edition.domain.vo;

import lombok.Data;

/**
 * @author dcl
 * @since 2023/8/28 10:45
 */
@Data
public class PackageSpecItemVo {

    private String id;

    private String spaceId;

    private String name;

    private int sort;

    private String discountDesc;

    private String unitAmountDesc;
}
