package com.mammon.member.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MemberDto {

    private String phone;

    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private int sex;

    /**
     * 生日
     */
    private LocalDate birthDay;

    /**
     * 备注
     */
    private String remark;

    /**
     * 注册来源
     */
    private int source;

    /**
     * 来源渠道
     */
    private int channel;

    /**
     * 推荐人
     */
    private String referenceId;
}
