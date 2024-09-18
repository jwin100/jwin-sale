package com.mammon.member.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberAssetsLogQuery extends PageQuery {

    private String memberId;

    private Integer type;

    private LocalDate startDate;

    private LocalDate endDate;
}
