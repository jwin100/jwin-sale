package com.mammon.sms.domain.dto;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmsTempQuery extends PageQuery {

    private Integer tempGroup;

    /**
     * 模板分组
     */
    private Integer smsType;

    /**
     * 短信类型
     */
    private Integer tempType;

    private Integer status;
}
