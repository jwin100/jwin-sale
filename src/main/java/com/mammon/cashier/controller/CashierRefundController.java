package com.mammon.cashier.controller;

import com.mammon.cashier.domain.query.CashierRefundQuery;
import com.mammon.cashier.domain.vo.CashierRefundComputeVo;
import com.mammon.cashier.domain.vo.CashierRefundDetailVo;
import com.mammon.cashier.domain.vo.CashierRefundListVo;
import com.mammon.cashier.service.CashierRefundPayService;
import com.mammon.cashier.service.CashierRefundService;
import com.mammon.cashier.service.CashierRefundTradeService;
import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.cashier.domain.dto.CashierRefundPayDto;
import com.mammon.cashier.domain.dto.CashierRefundDto;
import com.mammon.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @author dcl
 * @date 2023-02-22 13:59:01
 */
@Slf4j
@RestController
@RequestMapping("/cashier/refund")
public class CashierRefundController {

    @Resource
    private CashierRefundService cashierRefundService;

    @Resource
    private CashierRefundTradeService cashierRefundTradeService;

    /**
     * 计算退货金额
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PostMapping("/compute")
    public ResultJson<CashierRefundComputeVo> compute(@RequestHeader long merchantNo,
                                                      @RequestHeader long storeNo,
                                                      @RequestHeader String accountId,
                                                      @RequestBody CashierRefundDto dto) {
        //创建退换货，关联上销售单
        //计算退换货金额，小于0退，大于0补差价
        return ResultJson.ok(cashierRefundTradeService.compute(dto));
    }

    /**
     * 退换货
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PostMapping("/confirm")
    public ResultJson<String> confirm(@RequestHeader long merchantNo,
                                      @RequestHeader long storeNo,
                                      @RequestHeader String accountId,
                                      @RequestBody CashierRefundDto dto) {
        //创建退换货，关联上销售单
        //计算退换货金额，小于0退，大于0补差价
        String refundId = cashierRefundTradeService.createRefund(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok(refundId);
    }

    /**
     * 退款支付
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PostMapping("/pay")
    public ResultJson<Void> pay(@RequestHeader long merchantNo,
                                @RequestHeader long storeNo,
                                @RequestHeader String accountId,
                                @RequestBody CashierRefundPayDto dto) {
        cashierRefundTradeService.refundPay(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    /**
     * 退单打印
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @return
     */
    @PostMapping("/print/{id}")
    public ResultJson<Void> print(@RequestHeader long merchantNo,
                                  @RequestHeader long storeNo,
                                  @RequestHeader String accountId,
                                  @PathVariable String id) {
        try {
            cashierRefundService.refundPrint(merchantNo, accountId, id);
        } catch (CustomException e) {
            log.error("退单打印打印失败:{}", e.getResultJson());
        }
        return ResultJson.ok();
    }

    /**
     * 订单详情
     *
     * @return
     */
    @GetMapping("/{id}")
    public ResultJson<CashierRefundDetailVo> refundInfo(@RequestHeader long merchantNo,
                                                        @RequestHeader long storeNo,
                                                        @RequestHeader String accountId,
                                                        @PathVariable("id") String id) {
        CashierRefundDetailVo result = cashierRefundService.findById(id);
        return ResultJson.ok(result);
    }

    /**
     * 订单列表
     *
     * @return
     */
    @GetMapping("/page")
    public ResultJson<PageVo<CashierRefundListVo>> refundPage(@RequestHeader long merchantNo,
                                                              @RequestHeader long storeNo,
                                                              @RequestHeader String accountId,
                                                              CashierRefundQuery dto) {
        if (dto.getStartDate() == null) {
            dto.setStartDate(LocalDate.now());
        }
        if (dto.getEndDate() == null) {
            dto.setEndDate(LocalDate.now());
        }
        PageVo<CashierRefundListVo> result = cashierRefundService.getPage(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok(result);
    }
}
