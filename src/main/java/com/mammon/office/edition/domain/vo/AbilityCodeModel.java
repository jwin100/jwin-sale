package com.mammon.office.edition.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author dcl
 * @date 2023-02-06 14:31:01
 */
@Data
@AllArgsConstructor
public class AbilityCodeModel {

    /**
     * 功能编码
     */
    private int code;

    /**
     * 功能描述
     */
    private String desc;

    private List<String> path;
}
