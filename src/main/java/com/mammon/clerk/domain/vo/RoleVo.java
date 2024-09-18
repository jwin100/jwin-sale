package com.mammon.clerk.domain.vo;

import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.clerk.domain.enums.RoleType;
import lombok.Data;

@Data
public class RoleVo {
    private String id;
    private long merchantNo;
    private String name;
    private String enName;
    private String remark;
    private int defaultStatus;
    private int type;
    private int status;
    private String typeName;
    private String statusName;

    public String getTypeName() {
        return IEnum.getNameByCode(this.getType(), RoleType.class);
    }

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }
}
