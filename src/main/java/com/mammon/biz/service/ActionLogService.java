package com.mammon.biz.service;

import com.mammon.biz.dao.ActionLogDao;
import com.mammon.biz.domain.dto.ActionLogDto;
import com.mammon.biz.domain.entity.ActionLogEntity;
import com.mammon.common.Generate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/10/16 19:26
 */
@Service
public class ActionLogService {

    @Resource
    private ActionLogDao actionLogDao;

    public void save(long merchantNo, long storeNo, String accountId, ActionLogDto dto) {
        ActionLogEntity entity = new ActionLogEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setAccountId(accountId);
        entity.setPosition(dto.getPosition());
        entity.setEvent(dto.getEvent());
        entity.setSource(dto.getSource());
        entity.setCreateTime(LocalDateTime.now());
        actionLogDao.insert(entity);
    }
}
