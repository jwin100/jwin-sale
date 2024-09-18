package com.mammon.clerk.domain.vo;

import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.clerk.domain.enums.CommissionRuleMode;
import com.mammon.clerk.domain.enums.CommissionRuleUnit;
import com.mammon.clerk.domain.enums.CommissionRuleType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/4/7 17:02
 */
@Data
public class CommissionRuleVo {

    private String id;

    private long merchantNo;

    /**
     * 类型
     */
    private int type;

    private String typeName;

    /**
     * 计算模式
     */
    private int mode;

    private String modeName;

    /**
     * 计算单位
     */
    private int unit;

    private String unitName;

    /**
     * 提成值
     */
    private double rate;

    private int status;

    private String statusName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public String getTypeName() {
        return IEnum.getNameByCode(this.getType(), CommissionRuleType.class);
    }

    public String getModeName() {
        return IEnum.getNameByCode(this.getMode(), CommissionRuleMode.class);
    }

    public String getUnitName() {
        return IEnum.getNameByCode(this.getUnit(), CommissionRuleUnit.class);
    }

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }
}
