package com.mammon.document.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @since 2024/8/2 13:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DocumentHelpQuery extends PageQuery {

    private String pid;

    private String searchKey;
}
