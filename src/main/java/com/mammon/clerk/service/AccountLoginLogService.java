package com.mammon.clerk.service;

import com.mammon.common.Generate;
import com.mammon.clerk.dao.AccountLoginLogDao;
import com.mammon.clerk.domain.entity.AccountLoginLogEntity;
import com.mammon.utils.IpUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class AccountLoginLogService {

    @Resource
    private AccountLoginLogDao accountLoginLogDao;

    @Async("taskExecutor")
    public void save(HttpServletRequest request, String accountId, int type, LocalDateTime loginTime, int platform) {
        String ip = IpUtil.getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        AccountLoginLogEntity entity = new AccountLoginLogEntity();
        entity.setId(Generate.generateUUID());
        entity.setAccountId(accountId);
        entity.setType(type);
        entity.setLoginTime(loginTime);
        entity.setIp(ip);
        entity.setPlatform(platform);
        entity.setUserAgent(userAgent);
        entity.setCreateTime(LocalDateTime.now());
        accountLoginLogDao.save(entity);
    }
}
