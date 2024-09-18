package com.mammon.member.domain.dto;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberTimeCardLogQuery extends PageQuery {

    private String memberId;

    private LocalDate startDate;

    private LocalDate endDate;
}
