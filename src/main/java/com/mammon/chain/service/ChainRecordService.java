package com.mammon.chain.service;

import com.mammon.chain.dao.ChainRecordDao;
import com.mammon.chain.domain.entity.ChainRecordEntity;
import com.mammon.common.Generate;
import com.mammon.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2022-10-17 16:21:17
 */
@Service
@Slf4j
public class ChainRecordService {
    private static final String DEFAULT_REDIRECT_URL = "https://jwin100.cn";

    @Resource
    private ChainRecordDao chainRecordDao;

    @Resource
    private ChainService chainService;

    /**
     * 保存点击记录
     *
     * @param code
     * @param request
     * @param response
     */
    public void redirect(String code, HttpServletRequest request, HttpServletResponse response) {
        try {
            String longUrl = chainService.findLinkByCode(code);
            if (StringUtils.isBlank(longUrl)) {
                response.sendRedirect(DEFAULT_REDIRECT_URL);
            }
            longUrl = String.format("%s?code=%s", longUrl, code);
            save(code, longUrl, request);
            response.sendRedirect(longUrl);
        } catch (Exception e) {
            log.error("连接跳转异常,{}", e.getMessage(), e);
        }
    }

    public void save(String code, String longUrl, HttpServletRequest request) {
        String domain = request.getServerName();
        String ip = getIp(request);
        String refer = getRefer(request);
        String useragent = getUseragent(request);

        ChainRecordEntity entity = new ChainRecordEntity();
        entity.setId(Generate.generateUUID());
        entity.setCode(code);
        entity.setUrl(longUrl);
        entity.setDomain(domain);
        entity.setIp(ip);
        entity.setRefer(refer);
        entity.setUseragent(useragent);
        entity.setCreateTime(LocalDateTime.now());
        chainRecordDao.save(entity);
    }

    private String getRefer(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.REFERER);
    }

    private String getUseragent(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_AGENT);
    }

    private String getIp(HttpServletRequest request) {
        return IpUtil.getIpAddress(request);
    }

}
