package com.mammon.clerk.controller;

import com.mammon.clerk.domain.dto.FirstSetPasswordDto;
import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.dto.AccountDto;
import com.mammon.clerk.domain.query.AccountQuery;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.clerk.service.AccountService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/clerk/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody AccountDto dto) {
        accountService.save(merchantNo, dto);
        return ResultJson.ok();
    }

    @PutMapping(value = "/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody AccountDto dto) {
        accountService.edit(id, dto);
        return ResultJson.ok();
    }

    @PutMapping(value = "/status/{id}")
    public ResultJson<Void> editStatus(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("id") String id,
                                       @RequestParam int status) {
        accountService.editStatus(id, status);
        return ResultJson.ok();
    }

    @PutMapping("/edit-password/{id}")
    public ResultJson<Void> editPassword(@RequestHeader long merchantNo,
                                         @RequestHeader long storeNo,
                                         @RequestHeader String accountId,
                                         @PathVariable("id") String id,
                                         @RequestParam String password) {
        accountService.editPassword(id, password);
        return ResultJson.ok();
    }

    @PostMapping("/first-set-password")
    public ResultJson<Void> setPassword(@RequestHeader long merchantNo,
                                        @RequestHeader long storeNo,
                                        @RequestHeader String accountId,
                                        @Validated @RequestBody FirstSetPasswordDto dto) {
        accountService.editPassword(accountId, dto);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson<Void> delete(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        accountService.delete(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping(value = "/{id}")
    public ResultJson<UserVo> info(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        UserVo userVo = accountService.info(id);
        return ResultJson.ok(userVo);
    }

    @GetMapping("/page")
    public ResultJson<PageVo<UserVo>> page(@RequestHeader long merchantNo,
                                           @RequestHeader long storeNo,
                                           @RequestHeader String accountId,
                                           AccountQuery query) {
        PageVo<UserVo> vo = accountService.page(merchantNo, storeNo, accountId, query);
        return ResultJson.ok(vo);
    }
}