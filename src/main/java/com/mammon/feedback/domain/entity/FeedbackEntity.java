package com.mammon.feedback.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String accountId;

    /**
     * 反馈类型
     */
    private int type;

    private String title;

    private String content;

    private String images;

    /**
     * 联系方式(微信，qq,电话)
     */
    private int contactType;

    /**
     * 联系号码
     */
    private String contactNo;

    private int status;

    private String ip;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
