package com.mammon.merchant.domain.vo;

import com.mammon.merchant.domain.entity.MerchantIndustryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dcl
 * @date 2023-02-06 14:23:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MerchantIndustryInfoVo extends MerchantIndustryEntity {

    private String industryName;
}
