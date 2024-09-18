package com.mammon.member.domain.entity;

import com.mammon.member.domain.enums.MemberChannel;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会员信息
 */
@Data
public class MemberEntity {

    private String id;

    private long merchantNo;

    /**
     * 注册门店
     */
    private long storeNo;

    private String phone;

    private String name;

    /**
     * 性别
     */
    private int sex;

    /**
     * 生日
     */
    private LocalDate birthDay;

    private String levelId;

    private int status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String accountId;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 注册来源
     */
    private int source;

    /**
     * 来源渠道
     *
     * @see MemberChannel
     */
    private int channel;

    /**
     * 推荐人
     */
    private String referenceId;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private int deleted;
}
