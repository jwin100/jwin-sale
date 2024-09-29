package com.mammon.clerk.gate;

import com.mammon.clerk.domain.vo.AccountDetailVo;
import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.clerk.service.AccountService;
import com.mammon.enums.CommonDeleted;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-21 10:46:33
 */
@RestController
@RequestMapping("/gate/clerk/account")
public class GateAccountController {

    @Resource
    private AccountService accountService;

    @PutMapping("/cash-mode/{cashMode}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable int cashMode) {
        accountService.editMobileCashMode(accountId, cashMode);
        return ResultJson.ok();
    }

    /**
     * 商户信息
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @GetMapping("/detail")
    public ResultJson<AccountDetailVo> merchantDetail(@RequestHeader long merchantNo,
                                                      @RequestHeader long storeNo,
                                                      @RequestHeader String accountId) {
        return ResultJson.ok(accountService.detail(merchantNo, storeNo, accountId));
    }

    @GetMapping("/list")
    public ResultJson<List<UserVo>> list(@RequestHeader long merchantNo,
                                         @RequestHeader long storeNo,
                                         @RequestHeader String accountId) {
        List<UserVo> vos = accountService.findAllByStoreNo(merchantNo, storeNo, CommonDeleted.NOT_DELETED.getCode());
        return ResultJson.ok(vos);
    }

    @GetMapping("/all-list")
    public ResultJson<List<UserVo>> allList(@RequestHeader long merchantNo,
                                            @RequestHeader long storeNo,
                                            @RequestHeader String accountId) {
        List<UserVo> vos = accountService.findAll(merchantNo, CommonDeleted.NOT_DELETED.getCode());
        return ResultJson.ok(vos);
    }

    @GetMapping("/search/{searchKey}")
    public ResultJson<List<UserVo>> accountSearch(@RequestHeader long merchantNo,
                                                  @RequestHeader long storeNo,
                                                  @RequestHeader String accountId,
                                                  @PathVariable("searchKey") String searchKey) {
        List<UserVo> vos = accountService.accountSearch(merchantNo, searchKey);
        return ResultJson.ok(vos);
    }
}
