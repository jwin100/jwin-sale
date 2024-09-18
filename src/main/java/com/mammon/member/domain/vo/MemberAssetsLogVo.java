package com.mammon.member.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.member.domain.enums.MemberAssetsCategory;
import com.mammon.member.domain.enums.MemberAssetsLogType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberAssetsLogVo {

    private String id;

    private String memberId;

    private String memberName;

    /**
     * 变更订单号
     */
    private String orderNo;

    /**
     * 财产类型(积分变更，储值金额变更)
     */
    private int type;

    private String typeName;

    /**
     * 变更种类(会员储值，储值退款，会员消费，消费退款)
     */
    private int category;

    private String categoryName;

    /**
     * 变更前余额
     */
    private long beforeAssets;

    /**
     * 变更余额
     */
    private long changeAssets;

    /**
     * 变更后余额
     */
    private long afterAssets;

    private String remark;

    private LocalDateTime createTime;

    public String getTypeName() {
        return IEnum.getNameByCode(this.getType(), MemberAssetsLogType.class);
    }

    public String getCategoryName() {
        return IEnum.getNameByCode(this.getCategory(), MemberAssetsCategory.class);
    }
}
