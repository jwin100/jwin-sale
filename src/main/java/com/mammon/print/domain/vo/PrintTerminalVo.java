package com.mammon.print.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.print.domain.enums.PrintTemplateClassify;
import com.mammon.print.domain.enums.PrintTerminalStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PrintTerminalVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 打印机类型，1:小票，2:标签
     *
     * @see PrintTemplateClassify
     */
    private int classify;

    private String classifyName;

    /**
     * 所属渠道
     */
    private String channelId;

    private String channelName;

    /**
     * 打印机名
     */
    private String name;

    private String formConfig;

    /**
     * 打印宽度
     */
    private int width;

    private List<PrintTerminalBindTypeVo> bindTypes = new ArrayList<>();

    /**
     * 状态(启用几个打印几个
     */
    private int status;

    private String statusName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), PrintTerminalStatus.class);
    }

    public String getClassifyName() {
        return IEnum.getNameByCode(this.getClassify(), PrintTemplateClassify.class);
    }
}
