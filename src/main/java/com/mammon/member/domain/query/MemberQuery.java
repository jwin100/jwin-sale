package com.mammon.member.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberQuery extends PageQuery {

    private String name;

    private String phone;

    private Long storeNo;

    private List<String> tagIds = new ArrayList<>();
}
