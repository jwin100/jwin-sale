package com.mammon.goods.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2023/7/17 15:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryQuery extends PageQuery {

    private String searchKey;

    private String pid;

    private Integer level;
}
