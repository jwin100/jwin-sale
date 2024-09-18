package com.mammon.stock.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author dcl
 * @since 2023/8/22 11:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StockSearchQuery extends PageQuery {

    private String keyword;

    private String categoryId;

    /**
     * 状态，1：上架，2：下架
     */
    private Integer status;

    /**
     * 计次核销
     */
    private Integer countedType;
}
