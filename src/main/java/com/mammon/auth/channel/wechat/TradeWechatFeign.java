package com.mammon.auth.channel.wechat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author dcl
 * @since 2023/12/19 18:22
 */
@FeignClient(name = "wechat", url = "https://api.weixin.qq.com")
public interface TradeWechatFeign {

    @GetMapping(value = "/sns/jscode2session")
    String getOpenId(@SpringQueryMap Map<String, String> map);

    @GetMapping("/cgi-bin/token")
    String getAccessToken(@SpringQueryMap Map<String, String> map);

    @PostMapping("/wxa/business/getuserphonenumber")
    String getPhoneNumber(@RequestParam("access_token") String accessToken,
                          @RequestBody Map<String, String> map);
}
