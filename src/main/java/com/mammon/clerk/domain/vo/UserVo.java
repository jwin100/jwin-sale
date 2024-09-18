package com.mammon.clerk.domain.vo;

import com.mammon.enums.CommonDeleted;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVo {
    private String id;
    private long merchantNo;
    private String merchantName;
    private long storeNo;
    private String storeName;
    private boolean storeMain;
    private String name;
    private String phone;
    private String email;
    private int status;
    private String statusName;
    private int deleted;
    private String deletedName;
    private String roleId;
    private RoleVo role;
    private LocalDateTime createTime;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }

    public String getDeletedName() {
        return IEnum.getNameByCode(this.getDeleted(), CommonDeleted.class);
    }
}