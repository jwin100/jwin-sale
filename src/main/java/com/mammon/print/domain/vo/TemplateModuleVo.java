package com.mammon.print.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TemplateModuleVo {

    private String moduleKey;
    private String moduleName;
    private int moduleSort;

    private List<TemplateItemVo> items = new ArrayList<>();
}
