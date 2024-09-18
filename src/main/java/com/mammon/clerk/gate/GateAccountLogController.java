package com.mammon.clerk.gate;

import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.dto.AccountLogDto;
import com.mammon.clerk.service.AccountLoginLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/4/1 11:13
 */
@RestController
@RequestMapping("/gate/clerk/account-log")
public class GateAccountLogController {

    @Resource
    private AccountLoginLogService accountLoginLogService;

    @PostMapping
    public ResultJson<Void> save(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @RequestBody AccountLogDto dto,
                                 HttpServletRequest request) {
        accountLoginLogService.save(request, accountId, dto.getType(), LocalDateTime.now(), dto.getPlatform());
        return ResultJson.ok();
    }
}
