package com.mammon.print.channel.elind.model;

import lombok.Data;

@Data
public class ElindStatusVo {

    /**
     * 0离线 1在线 2缺纸
     */
    private String state;
}
