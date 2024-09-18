package com.mammon.goods.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SpecKeyVo {

    private String id;

    private long merchantNo;

    /**
     * 规格名
     */
    private String name;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<SpecValueVo> values = new ArrayList<>();
}
