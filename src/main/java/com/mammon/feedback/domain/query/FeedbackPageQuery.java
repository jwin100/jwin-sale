package com.mammon.feedback.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @date 2022-11-02 14:47:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FeedbackPageQuery extends PageQuery {

    /**
     * 只看本人
     */
    private boolean justMe;
}
