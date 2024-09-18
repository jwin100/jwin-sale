package com.mammon.merchant.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2023/7/17 16:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MerchantStoreQuery extends PageQuery {
    private String searchKey;
}
