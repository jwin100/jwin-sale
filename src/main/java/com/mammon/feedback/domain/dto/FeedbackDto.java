package com.mammon.feedback.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author dcl
 * @date 2022-11-02 14:53:04
 */
@Data
public class FeedbackDto {

    private int type;

    private String title;

    private String content;

    private List<String> images;

    /**
     * 联系方式(微信，qq,电话)
     */
    private int contactType;

    /**
     * 联系号码
     */
    private String contactNo;
}
