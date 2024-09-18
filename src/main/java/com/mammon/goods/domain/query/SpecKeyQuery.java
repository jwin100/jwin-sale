package com.mammon.goods.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2023/7/17 16:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpecKeyQuery extends PageQuery {

    private String pid;

    private String searchKey;
}
