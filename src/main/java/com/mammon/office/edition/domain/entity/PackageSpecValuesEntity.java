package com.mammon.office.edition.domain.entity;

import lombok.Data;

/**
 * @author dcl
 * @since 2023/8/28 10:42
 */
@Data
public class PackageSpecValuesEntity {

    private String id;

    private String specId;

    private String name;

    private int sort;

    private String discountDesc;

    private String unitAmountDesc;
}
