package com.mammon.auth.inner;

import com.mammon.auth.domain.UserDetail;
import com.mammon.auth.service.AuthService;
import com.mammon.common.ResultCode;
import com.mammon.common.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/inner/oauth")
@Slf4j
public class InnerOAuthController {

    @Resource
    private AuthService authService;

    /**
     * 验证登录令牌
     *
     * @param token
     * @return
     */
    @PostMapping(value = "/valid/{token}")
    public ResultJson checkToken(@PathVariable("token") String token) {
        UserDetail userDetail = authService.validateToken(token);
        return ResultJson.ok(userDetail);
    }
}
