package com.mammon.auth.controller;

import com.mammon.auth.service.AuthService;
import com.mammon.common.ResultJson;
import com.mammon.clerk.service.HandoverService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2023/9/28 16:15
 */
@RestController
@RequestMapping("/oauth/logout")
public class LogoutController {

    @Resource
    private AuthService authService;

    @Resource
    private HandoverService handoverService;

    /**
     * 退出登录
     *
     * @param token
     * @return
     */
    @PostMapping(value = "/{token}")
    public ResultJson<Void> logout(@PathVariable(value = "token", required = false) String token) {
        return ResultJson.ok();
    }
}
