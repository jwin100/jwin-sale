package com.mammon.goods.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryTreeVo {

    private String value;

    private String label;

    private String pid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CategoryTreeVo> children;
}
