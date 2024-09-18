package com.mammon.member.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberTimeCardConsumeDto {

    private String memberId;

    private int changeType;

    private String orderNo;

    private String remark;

    private String countedId;

    private long countedTotal;
}
