package com.mammon.merchant.domain.vo;

import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MerchantStoreVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String storeName;

    private String storePhone;

    private String accountId;

    private String accountName;

    private String accountPhone;

    private String roleId;

    private String roleName;

    private boolean main;

    /**
     * 额度到期日期
     */
    private LocalDate endDate;

    /**
     * 门店地址
     */
    private String province;

    private String provinceName;

    private String city;

    private String cityName;

    private String area;

    private String areaName;

    private String address;

    /**
     * 行业分类
     */
    private Long industryOne;

    private Long industryTwo;

    private Long industryThree;

    private LocalDateTime createTime;

    private int status;

    private String statusName;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }
}
