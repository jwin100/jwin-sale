package com.mammon.merchant.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegionTreeVo {

    private String value;

    private String label;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RegionTreeVo> children;
}
