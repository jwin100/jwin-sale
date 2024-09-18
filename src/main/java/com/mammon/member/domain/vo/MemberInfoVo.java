package com.mammon.member.domain.vo;

import cn.hutool.core.util.DesensitizedUtil;
import com.mammon.enums.CommonSource;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.member.domain.enums.MemberChannel;
import com.mammon.member.domain.enums.MemberSex;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class MemberInfoVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String storeName;

    private String phone;

    private String name;

    /**
     * 性别
     */
    private int sex;

    private String sexName;

    /**
     * 生日
     */
    private LocalDate birthDay;

    private int status;

    private String statusName;

    /**
     * 会员等级
     */
    private String levelId;

    /**
     * 会员等级
     */
    private String levelName;

    /**
     * 注册来源
     */
    private int source;

    private String sourceName;

    /**
     * 来源渠道
     */
    private int channel;

    private String channelName;

    /**
     * 推荐人
     */
    private String referenceId;

    /**
     * 推荐人
     */
    private String referenceName;

    /**
     * 最后购买时间
     */
    private LocalDateTime lastCashierTime;

    private long lastCashierStoreNo;

    /**
     * 最后购买门店
     */
    private String lastCashierStoreName;

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
     * 积分
     */
    private long nowIntegral;

    /**
     * 累计积分
     */
    private long totalIntegral;

    /**
     * 计次卡数量
     */
    private long counted;

    /**
     * 储值余额
     */
    private BigDecimal nowRecharge;

    /**
     * 累计储值
     */
    private BigDecimal totalRecharge;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<MemberTagMapVo> tags = new ArrayList<>();

    public String getSexName() {
        return IEnum.getNameByCode(this.getSex(), MemberSex.class);
    }

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }

    public void setPhone(String phone) {
        this.phone = DesensitizedUtil.mobilePhone(phone);
    }

    public String getSourceName() {
        return IEnum.getNameByCode(this.getSource(), CommonSource.class);
    }

    public String getChannelName() {
        return IEnum.getNameByCode(this.getChannel(), MemberChannel.class);
    }
}
