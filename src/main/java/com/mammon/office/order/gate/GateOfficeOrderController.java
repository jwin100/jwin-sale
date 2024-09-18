package com.mammon.office.order.gate;

import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.office.order.domain.dto.OfficeOrderCreateDto;
import com.mammon.office.order.domain.dto.OfficeOrderPayDto;
import com.mammon.office.order.domain.query.OfficeOrderQuery;
import com.mammon.office.order.domain.vo.*;
import com.mammon.office.order.service.OfficeOrderActiveService;
import com.mammon.office.order.service.OfficeOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @author dcl
 * @date 2023-02-02 11:58:39
 */
@RestController
@RequestMapping("/gate/office/order")
public class GateOfficeOrderController {

    @Resource
    private OfficeOrderService officeOrderService;

    @Resource
    private OfficeOrderActiveService officeOrderActiveService;

    /**
     * 创建订单
     *
     * @param dto
     * @return
     */
    @PostMapping("/create")
    public ResultJson<OfficeOrderCreateVo> create(@RequestHeader long merchantNo,
                                                  @RequestHeader long storeNo,
                                                  @RequestHeader String accountId,
                                                  @RequestBody OfficeOrderCreateDto dto) {
        return ResultJson.ok(officeOrderService.create(merchantNo, storeNo, accountId, dto));
    }

    /**
     * 预支付
     *
     * @param orderId
     * @return
     */
    @PostMapping("/prepay/{orderId}")
    public ResultJson<String> pay(@RequestHeader long merchantNo,
                                  @RequestHeader long storeNo,
                                  @RequestHeader String accountId,
                                  @PathVariable("orderId") String orderId,
                                  @RequestBody OfficeOrderPayDto dto) {
        return ResultJson.ok(officeOrderService.tradePrePay(merchantNo, orderId, dto));
    }

    @GetMapping("/query/{orderId}")
    public ResultJson<OfficeOrderVo> query(@RequestHeader long merchantNo,
                                           @RequestHeader long storeNo,
                                           @RequestHeader String accountId,
                                           @PathVariable("orderId") String orderId) {
        return ResultJson.ok(officeOrderService.findById(merchantNo, orderId));
    }

    @PostMapping("/active/{orderId}")
    public ResultJson active(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @PathVariable("orderId") String orderId) {
        officeOrderActiveService.active(merchantNo, storeNo, orderId);
        return ResultJson.ok();
    }

    @GetMapping("/page")
    public ResultJson<PageVo<OfficeOrderVo>> page(@RequestHeader long merchantNo,
                                                  @RequestHeader long storeNo,
                                                  @RequestHeader String accountId,
                                                  OfficeOrderQuery query) {
        if (query.getStartCreateTime() == null) {
            query.setStartCreateTime(LocalDate.now());
        }
        if (query.getEndCreateTime() == null) {
            query.setEndCreateTime(LocalDate.now());
        }
        PageVo<OfficeOrderVo> page = officeOrderService.page(merchantNo, query);
        return ResultJson.ok(page);
    }

    @GetMapping("/{id}")
    public ResultJson<OfficeOrderDetailVo> findDetailById(@RequestHeader long merchantNo,
                                                          @RequestHeader long storeNo,
                                                          @RequestHeader String accountId,
                                                          @PathVariable("id") String id) {
        return ResultJson.ok(officeOrderService.findDetailById(merchantNo, id));
    }
}
