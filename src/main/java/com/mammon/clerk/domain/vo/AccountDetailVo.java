package com.mammon.clerk.domain.vo;

import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.domain.vo.MerchantVo;
import com.mammon.office.edition.domain.entity.IndustryAttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author dcl
 * @date 2023-02-21 10:49:10
 */
@Data
public class AccountDetailVo {

    private MerchantVo merchantVo;

    private MerchantStoreVo storeVo;

    private UserVo userVo;

    private List<ResourceVo> resources;

    private List<IndustryAttrEntity> industryAttrs;
}
