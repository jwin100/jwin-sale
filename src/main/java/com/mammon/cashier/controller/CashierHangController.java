package com.mammon.cashier.controller;

import com.mammon.cashier.domain.query.CashierHangQuery;
import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.cashier.domain.dto.CashierHangDto;
import com.mammon.cashier.domain.vo.CashierHangVo;
import com.mammon.cashier.service.CashierHangService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 收银挂单
 */
@RestController
@RequestMapping("/cashier/hang")
public class CashierHangController {

    @Resource
    private CashierHangService cashierHangService;

    /**
     * 挂单
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody CashierHangDto dto) {
        cashierHangService.create(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    /**
     * 取单
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @PostMapping("/take/{id}")
    public ResultJson<CashierHangVo> hangTake(@RequestHeader long merchantNo,
                                              @RequestHeader long storeNo,
                                              @RequestHeader String accountId,
                                              @PathVariable("id") String id) {
        CashierHangVo hang = cashierHangService.hangTake(merchantNo, storeNo, accountId, id);
        return ResultJson.ok(hang);
    }

    @DeleteMapping("/{id}")
    public ResultJson<Void> hangDelete(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("id") String id) {
        cashierHangService.hangDelete(id);
        return ResultJson.ok();
    }

    /**
     * 收银-挂单列表
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @GetMapping("/list")
    public ResultJson<List<CashierHangVo>> hangList(@RequestHeader long merchantNo,
                                                    @RequestHeader long storeNo,
                                                    @RequestHeader String accountId) {
        List<CashierHangVo> hangs = cashierHangService.hangList(merchantNo, storeNo, accountId);
        return ResultJson.ok(hangs);
    }

    /**
     * 销售管理-挂单列表
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @GetMapping("/page")
    public ResultJson<PageVo<CashierHangVo>> hangPage(@RequestHeader long merchantNo,
                                                      @RequestHeader long storeNo,
                                                      @RequestHeader String accountId,
                                                      CashierHangQuery query) {
        PageVo<CashierHangVo> result = cashierHangService.hangPage(merchantNo, storeNo, null, query);
        return ResultJson.ok(result);
    }
}
