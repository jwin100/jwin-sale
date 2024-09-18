package com.mammon.auth.channel.wechat;

import cn.hutool.core.util.StrUtil;
import com.mammon.auth.channel.factory.TradeOpenChannel;
import com.mammon.auth.channel.wechat.model.TradeWechatPhoneVo;
import com.mammon.auth.channel.wechat.model.TradeWechatSessionVo;
import com.mammon.auth.channel.wechat.model.TradeWechatTokenVo;
import com.mammon.exception.CustomException;
import com.mammon.leaf.domain.entity.LeafConfigEntity;
import com.mammon.leaf.service.LeafConfigService;
import com.mammon.service.RedisService;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author dcl
 * @since 2024/8/6 10:16
 */
@Service
@Slf4j
public class TradeWechatOpenService implements TradeOpenChannel {

    private static final String ACCESS_CACHE_PREFIX = "ACCESS_CACHE_";

    private final TradeWechatFeign tradeWechatFeign;
    private final RedisService redisService;
    private final LeafConfigService leafConfigService;

    public TradeWechatOpenService(TradeWechatFeign tradeWechatFeign,
                                  RedisService redisService, LeafConfigService leafConfigService) {
        this.tradeWechatFeign = tradeWechatFeign;
        this.redisService = redisService;
        this.leafConfigService = leafConfigService;
    }

    @Override
    public String getOpenId(String code) {
        LeafConfigEntity config = leafConfigService.getInfo();
        if (config == null) {
            throw new CustomException("微信小程序配置信息错误");
        }

        Map<String, String> map = new HashMap<>();
        map.put("appid", config.getWechatAppId());
        map.put("secret", config.getWechatAppSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String result = tradeWechatFeign.getOpenId(map);
        if (StrUtil.isBlank(result)) {
            throw new CustomException("微信小程序服务请求异常");
        }
        TradeWechatSessionVo vo = JsonUtil.toObject(result, TradeWechatSessionVo.class);
        if (vo == null) {
            throw new CustomException("微信小程序服务请求异常");
        }
        if (vo.getErrCode() != 0) {
            log.error("小程序登录获取OpenId异常，code:{},result:{}", code, JsonUtil.toJSONString(vo));
            throw new CustomException("微信小程序授权登录异常");
        }
        return vo.getOpenId();
    }

    /**
     * 获取接口调用凭证
     *
     * @return
     */
    public TradeWechatTokenVo getAccessToken() {
        LeafConfigEntity config = leafConfigService.getInfo();
        if (config == null) {
            throw new CustomException("微信小程序配置信息错误");
        }

        Map<String, String> map = new HashMap<>();
        map.put("appid", config.wechatAppId);
        map.put("secret", config.getWechatAppSecret());
        map.put("grant_type", "client_credential");
        String result = tradeWechatFeign.getAccessToken(map);
        if (StrUtil.isBlank(result)) {
            throw new CustomException("微信小程序服务请求异常");
        }
        TradeWechatTokenVo vo = JsonUtil.toObject(result, TradeWechatTokenVo.class);
        if (vo == null) {
            throw new CustomException("微信小程序服务请求异常");
        }
        if (vo.getErrCode() != 0) {
            log.error("小程序获取接口调用凭证异常，result:{}", JsonUtil.toJSONString(vo));
            throw new CustomException("微信小程序获取服务凭证异常");
        }
        return vo;
    }

    /**
     * 获取缓存accessToken
     *
     * @return accessToken
     */
    public String getCacheAccessToken() {
        LeafConfigEntity config = leafConfigService.getInfo();
        if (config == null) {
            throw new CustomException("微信小程序配置信息错误");
        }

        String ACCESS_CACHE_KEY = ACCESS_CACHE_PREFIX + config.getWechatAppId();
        String accessToken = redisService.get(ACCESS_CACHE_KEY);
        if (StrUtil.isNotBlank(accessToken)) {
            return accessToken;
        }
        // 缓存无数据，调用接口获取并缓存
        TradeWechatTokenVo tokenVo = getAccessToken();
        redisService.set(ACCESS_CACHE_KEY, tokenVo.getAccessToken(), tokenVo.getExpiresIn(), TimeUnit.SECONDS);
        return tokenVo.getAccessToken();
    }

    @Override
    public String getPhoneNumber(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        String accessToken = getCacheAccessToken();
        log.info("accessToken:{}", accessToken);
        String result = tradeWechatFeign.getPhoneNumber(accessToken, map);
        log.info("result:{}", result);
        if (StrUtil.isBlank(result)) {
            throw new CustomException("微信小程序服务请求异常");
        }
        TradeWechatPhoneVo vo = JsonUtil.toObject(result, TradeWechatPhoneVo.class);
        if (vo == null) {
            throw new CustomException("微信小程序服务请求异常");
        }
        if (vo.getErrCode() != 0 || vo.getPhoneInfo() == null) {
            log.error("小程序获取授权手机号信息异常，code:{},result:{}", code, JsonUtil.toJSONString(vo));
            throw new CustomException("获取授权信息异常");
        }
        return vo.getPhoneInfo().getPurePhoneNumber();
    }
}
