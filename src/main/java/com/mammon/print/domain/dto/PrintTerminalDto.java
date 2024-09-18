package com.mammon.print.domain.dto;

import com.mammon.print.domain.enums.PrintTemplateClassify;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PrintTerminalDto {
    /**
     * 打印机类型，1:小票，2:标签
     *
     * @see PrintTemplateClassify
     */
    private int classify;

    /**
     * 所属渠道
     */
    private String channelId;

    /**
     * 打印机名
     */
    private String name;

    private int width;

    private String formConfig;

    private List<Integer> bindTypes = new ArrayList<>();
}
