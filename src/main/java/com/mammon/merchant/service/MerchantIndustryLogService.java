package com.mammon.merchant.service;

import com.mammon.common.Generate;
import com.mammon.merchant.dao.MerchantIndustryLogDao;
import com.mammon.merchant.domain.entity.MerchantIndustryLogEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-02-02 11:41:05
 */
@Service
public class MerchantIndustryLogService {

    @Resource
    private MerchantIndustryLogDao merchantIndustryLogDao;

    public int save(long merchantNo, String orderNo, String industryId, int industryType, long addMonth) {
        MerchantIndustryLogEntity entity = new MerchantIndustryLogEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setIndustryId(industryId);
        entity.setAddMonth(addMonth);
        entity.setType(industryType);
        entity.setOrderNo(orderNo);
        entity.setCreateTime(LocalDateTime.now());
        return merchantIndustryLogDao.save(entity);
    }
}
