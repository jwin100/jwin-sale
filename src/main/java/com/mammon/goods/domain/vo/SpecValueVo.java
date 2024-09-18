package com.mammon.goods.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpecValueVo {

    private String id;

    private long merchantNo;

    /**
     * 规格名
     */
    private String name;

    /**
     * null为规格名，不为null为对应规格名的值
     */
    private String pid;

    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
