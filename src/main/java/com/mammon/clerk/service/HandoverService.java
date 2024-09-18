package com.mammon.clerk.service;

import com.mammon.common.Generate;
import com.mammon.clerk.dao.HandoverDao;
import com.mammon.clerk.domain.entity.HandoverEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2023/9/28 13:59
 */
@Service
public class HandoverService {

    @Resource
    private HandoverDao handoverDao;


    public void save(long merchantNo, long storeNo, String accountId, int type) {
        HandoverEntity entity = new HandoverEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setAccountId(accountId);
        entity.setType(type);
        entity.setHandoverTime(LocalDateTime.now());
        handoverDao.save(entity);
    }
}
