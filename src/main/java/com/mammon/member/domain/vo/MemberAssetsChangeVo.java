package com.mammon.member.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author dcl
 * @date 2023-04-04 13:50:46
 */
@Data
@AllArgsConstructor
@Builder
public class MemberAssetsChangeVo {

    /**
     * 变更结果，1：成功，0:失败
     */
    private int code;

    /**
     * 结果描述
     */
    private String message;
}
