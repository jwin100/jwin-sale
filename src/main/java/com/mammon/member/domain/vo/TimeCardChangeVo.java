package com.mammon.member.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author dcl
 * @date 2023-04-21 11:19:37
 */
@Data
@Builder
public class TimeCardChangeVo {
    /**
     * 1:成功,0:失败
     */
    private int code;

    private String tradeNo;

    /**
     * 结果描述
     */
    private String message;
}
