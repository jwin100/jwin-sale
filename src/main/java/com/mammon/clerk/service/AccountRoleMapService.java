package com.mammon.clerk.service;

import com.mammon.common.Generate;
import com.mammon.clerk.dao.AccountRoleMapDao;
import com.mammon.clerk.domain.entity.AccountRoleMapEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountRoleMapService {

    @Resource
    private AccountRoleMapDao accountRoleMapDao;

    public void save(AccountRoleMapEntity entity) {
        entity.setId(Generate.generateUUID());
        entity.setCreateTime(LocalDateTime.now());
        accountRoleMapDao.save(entity);
    }

    public AccountRoleMapEntity findByAccountId(String accountId) {
        return accountRoleMapDao.findByUserId(accountId);
    }

    public List<AccountRoleMapEntity> findAllByRoleId(String roleId) {
        return accountRoleMapDao.findAllByRoleId(roleId);
    }

    public void deleteByAccountId(String accountId) {
        accountRoleMapDao.deleteByAccountId(accountId);
    }
}
