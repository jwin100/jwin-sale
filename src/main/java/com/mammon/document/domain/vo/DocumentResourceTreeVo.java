package com.mammon.document.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author dcl
 * @since 2024/8/2 14:02
 */
@Data
public class DocumentResourceTreeVo {

    private String id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DocumentResourceTreeVo> children;
}
