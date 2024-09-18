package com.mammon.sms.channel.yunji.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @date 2022-10-13 11:22:50
 */
@Data
public class YunJiBusinessDataVo {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 号码总数
     */
    private long allNum;

    /**
     * 成功总数
     */
    private long successNum;

    /**
     * 失败总数
     */
    private long failNum;

    private List<YunJiBusinessDataItemVo> list = new ArrayList<>();
}
