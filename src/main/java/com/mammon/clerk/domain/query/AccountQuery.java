package com.mammon.clerk.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2023/7/17 15:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountQuery extends PageQuery {

    /**
     * 只看我的
     */
    private boolean lookMy;

    private String id;

    private Long storeNo;

    private String searchKey;

    private Integer status;
}
