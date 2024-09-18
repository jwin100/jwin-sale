package com.mammon.clerk.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PowerVo {

    private String id;

    private int index;

    private String name;

    private String title;

    /**
     * 1:菜单，2：目录，3：按钮
     */
    private int type;

    private List<PowerVo> children = new ArrayList<>();
}
