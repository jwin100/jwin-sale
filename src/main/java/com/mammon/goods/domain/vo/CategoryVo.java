package com.mammon.goods.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryVo {

    private String id;

    private String name;

    private int level;

    private String pid;

    private int status;

    private int sort;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<CategoryVo> children = new ArrayList<>();
}
