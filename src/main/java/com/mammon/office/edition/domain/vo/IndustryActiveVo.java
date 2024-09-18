package com.mammon.office.edition.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-02-08 14:07:36
 */
@Data
public class IndustryActiveVo {

    /**
     * 1：开通成功，2：失败
     */
    private int status;

    /**
     * 开通结果描述
     */
    private String message;

    private LocalDateTime activeTime;
}
