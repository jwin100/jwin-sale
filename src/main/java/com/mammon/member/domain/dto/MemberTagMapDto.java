package com.mammon.member.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class MemberTagMapDto {

    @NotBlank(message = "会员信息不能为空")
    private String memberId;

    private List<String> tagIds = new ArrayList<>();
}
