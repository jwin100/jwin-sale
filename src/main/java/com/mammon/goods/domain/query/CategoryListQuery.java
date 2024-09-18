package com.mammon.goods.domain.query;

import lombok.Data;

/**
 * @author dcl
 * @since 2023/10/27 13:16
 */
@Data
public class CategoryListQuery {

    private String pid;

    private String searchKey;

    private String name;

    private Integer level;

    private Integer deleted;
}
