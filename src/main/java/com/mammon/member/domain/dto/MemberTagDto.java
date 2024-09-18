package com.mammon.member.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MemberTagDto {

    @NotBlank(message = "标签名称不能为空")
    private String name;

    private String color;

    private int status;
}
