package com.mammon.cashier.controller;

import com.mammon.cashier.domain.dto.CashierOrderDto;
import com.mammon.cashier.domain.dto.CashierOrderPayDto;
import com.mammon.cashier.domain.vo.CashierOrderComputeVo;
import com.mammon.cashier.domain.vo.CashierOrderListVo;
import com.mammon.cashier.domain.vo.CashierOrderPayResultVo;
import com.mammon.cashier.service.CashierOrderPayService;
import com.mammon.cashier.service.CashierOrderTradeService;
import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.cashier.domain.query.CashierOrderPageQuery;
import com.mammon.cashier.domain.vo.CashierOrderDetailVo;
import com.mammon.cashier.service.CashierOrderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/cashier/order")
public class CashierOrderController {

    @Resource
    private CashierOrderTradeService cashierOrderTradeService;

    @Resource
    private CashierOrderService cashierOrderService;

    @Resource
    private CashierOrderPayService cashierOrderPayService;

    /**
     * 计算购物车金额
     *
     * @param merchantNo
     * @param storeNo
     * @param dto
     * @return
     */
    @PostMapping("/compute")
    public ResultJson<CashierOrderComputeVo> compute(@RequestHeader long merchantNo,
                                                     @RequestHeader long storeNo,
                                                     @RequestBody CashierOrderDto dto) {
        return ResultJson.ok(cashierOrderTradeService.compute(merchantNo, storeNo, dto));
    }

    /**
     * 创建订单
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
                                      @RequestBody CashierOrderDto dto) {
        String orderId = cashierOrderTradeService.createOrder(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok(orderId);
    }

    /**
     * 订单支付
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PostMapping("/pay")
    public ResultJson<CashierOrderPayResultVo> pay(@RequestHeader long merchantNo,
                                                   @RequestHeader long storeNo,
                                                   @RequestHeader String accountId,
                                                   @Validated @RequestBody CashierOrderPayDto dto) {
        return ResultJson.ok(cashierOrderTradeService.orderPay(merchantNo, storeNo, accountId, dto));
    }

    /**
     * 订单支付详情
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @return
     */
    @GetMapping("/pay/{id}")
    public ResultJson<CashierOrderPayResultVo> payResult(@RequestHeader long merchantNo,
                                                         @RequestHeader long storeNo,
                                                         @RequestHeader String accountId,
                                                         @PathVariable("id") String id) {
        return ResultJson.ok(cashierOrderPayService.getPayResult(id));
    }

    /**
     * 订单取消
     *
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    public ResultJson<Void> orderCancel(@PathVariable("id") String id) {
        cashierOrderService.orderCancel(id);
        return ResultJson.ok();
    }

    /**
     * 删除订单
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultJson<Void> orderDelete(@RequestHeader long merchantNo,
                                        @RequestHeader long storeNo,
                                        @RequestHeader String accountId,
                                        @PathVariable("id") String id) {
        cashierOrderService.orderDelete(merchantNo, id);
        return ResultJson.ok();
    }

    /**
     * 订单打印
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @return
     */
    @PostMapping("/print/{id}")
    public ResultJson<Void> orderPrint(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("id") String id) {
        cashierOrderService.orderPrint(id);
        return ResultJson.ok();
    }

    /**
     * 订单详情
     *
     * @return
     */
    @GetMapping("/{id}")
    public ResultJson<CashierOrderDetailVo> orderInfo(@RequestHeader long merchantNo,
                                                      @RequestHeader long storeNo,
                                                      @RequestHeader String accountId,
                                                      @PathVariable("id") String id) {
        CashierOrderDetailVo result = cashierOrderService.findDetailById(id);
        return ResultJson.ok(result);
    }

    /**
     * 订单列表
     *
     * @return
     */
    @GetMapping("/page")
    public ResultJson<PageVo<CashierOrderListVo>> orderPage(@RequestHeader long merchantNo,
                                                            @RequestHeader long storeNo,
                                                            @RequestHeader String accountId,
                                                            CashierOrderPageQuery dto) {
        PageVo<CashierOrderListVo> result = cashierOrderService.getPage(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok(result);
    }
}
