package com.mammon.sms.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/8/28 10:04
 * <p>
 * 短信审核记录
 */
@Data
public class SmsExamineEntity {

    private String id;

    /**
     * 目标Id
     */
    private String targetId;

    /**
     * 审核目标类型
     */
    private int targetType;

    /**
     * 审核人
     */
    private String userId;

    /**
     * 审核状态
     */
    private String status;

    /**
     * 审核时间
     */
    private LocalDateTime examineTime;

    /**
     * 审核描述
     */
    private String examineRemark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
