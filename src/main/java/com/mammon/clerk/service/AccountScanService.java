package com.mammon.clerk.service;

import cn.hutool.core.util.StrUtil;
import com.mammon.auth.domain.vo.LoginVo;
import com.mammon.clerk.dao.AccountDao;
import com.mammon.clerk.dao.AccountScanDao;
import com.mammon.clerk.domain.dto.AccountScanDto;
import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.domain.entity.AccountScanEntity;
import com.mammon.clerk.domain.enums.AccountScanStatus;
import com.mammon.clerk.domain.vo.AccountScanStatusVo;
import com.mammon.common.Generate;
import com.mammon.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author dcl
 * @since 2024/5/23 11:36
 */
@Service
public class AccountScanService {

    @Resource
    private AccountScanDao accountScanDao;

    public String save(int source) {
        AccountScanEntity entity = new AccountScanEntity();
        entity.setId(Generate.generateUUID());
        entity.setSource(source);
        entity.setStatus(AccountScanStatus.CREATED.getCode());
        entity.setExpireTime(getExpireTime());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        accountScanDao.save(entity);
        return entity.getId();
    }

    public void editStatus(String id, int status) {
        accountScanDao.editStatus(id, status);
    }

    public void editOpenId(String id, String openId) {
        accountScanDao.editOpenId(id, AccountScanStatus.SCANNED.getCode(), openId);
    }

    public void editLoginInfo(String id, String loginInfo) {
        accountScanDao.editLoginInfo(id, AccountScanStatus.LOGIN.getCode(), loginInfo);
    }

    @NotNull
    public AccountScanStatusVo findStatus(String id) {
        AccountScanEntity entity = findById(id);
        AccountScanStatusVo vo = validScanStatus(entity);
        if (vo != null) {
            return vo;
        }
        vo = new AccountScanStatusVo();
        vo.setStatus(entity.getStatus());
        if (StrUtil.isNotBlank(entity.getLoginInfo())) {
            vo.setLoginVo(JsonUtil.toObject(entity.getLoginInfo(), LoginVo.class));
        }
        return vo;
    }

    public AccountScanEntity findById(String id) {
        return accountScanDao.findById(id);
    }

    /**
     * 验证是否过期和取消
     *
     * @param entity
     * @return
     */
    public AccountScanStatusVo validScanStatus(AccountScanEntity entity) {
        AccountScanStatusVo vo = new AccountScanStatusVo();
        long timestamp = getTimestamp();
        if (entity == null || entity.getStatus() == AccountScanStatus.EXPIRED.getCode()) {
            vo.setStatus(AccountScanStatus.EXPIRED.getCode());
            return vo;
        }
        if (entity.getStatus() == AccountScanStatus.CANCEL.getCode()) {
            vo.setStatus(AccountScanStatus.CANCEL.getCode());
            return vo;
        }
        if (entity.getExpireTime() < timestamp) {
            editStatus(entity.getId(), AccountScanStatus.EXPIRED.getCode());
            vo.setStatus(AccountScanStatus.EXPIRED.getCode());
            return vo;
        }
        return null;
    }

    private long getTimestamp() {
        return LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    private long getExpireTime() {
        LocalDateTime now = LocalDateTime.now().plusMinutes(2);
        return now.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
