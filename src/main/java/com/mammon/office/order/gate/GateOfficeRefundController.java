package com.mammon.office.order.gate;

import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.office.order.domain.dto.OfficeOrderRefundDto;
import com.mammon.office.order.domain.query.OfficeOrderRefundQuery;
import com.mammon.office.order.domain.vo.*;
import com.mammon.office.order.service.OfficeRefundService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @author dcl
 * @date 2023-03-06 14:10:51
 */
@RestController
@RequestMapping("/gate/office/order-refund")
public class GateOfficeRefundController {

    @Resource
    private OfficeRefundService officeRefundService;

    // 订单超过15天无法发起退款申请
    // 未超过15天全额退款，短信退款（扣除短信使用量费用）
    @PostMapping("/compute/{orderId}")
    public ResultJson<OfficeRefundComputeVo> compute(@RequestHeader long merchantNo,
                                                     @RequestHeader long storeNo,
                                                     @RequestHeader String accountId,
                                                     @PathVariable("orderId") String orderId) {
        return ResultJson.ok(officeRefundService.compute(merchantNo, storeNo, accountId, orderId));
    }

    /**
     * 申请退款
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param orderId
     * @param dto
     * @return
     */
    @PostMapping("/{orderId}")
    public ResultJson create(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @PathVariable("orderId") String orderId,
                             @RequestBody OfficeOrderRefundDto dto) {
        officeRefundService.create(merchantNo, storeNo, accountId, orderId, dto);
        return ResultJson.ok();
    }

    /**
     * 退货列表
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/page")
    public ResultJson<PageVo<OfficeRefundVo>> refundPage(@RequestHeader long merchantNo,
                                                         @RequestHeader long storeNo,
                                                         @RequestHeader String accountId,
                                                         OfficeOrderRefundQuery query) {
        if (query.getStartCreateTime() == null) {
            query.setStartCreateTime(LocalDate.now());
        }
        if (query.getEndCreateTime() == null) {
            query.setEndCreateTime(LocalDate.now());
        }
        PageVo<OfficeRefundVo> page = officeRefundService.page(merchantNo, query);
        return ResultJson.ok(page);
    }

    /**
     * 退货详情
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultJson<OfficeRefundDetailVo> refundDetail(@RequestHeader long merchantNo,
                                                         @RequestHeader long storeNo,
                                                         @RequestHeader String accountId,
                                                         @PathVariable("id") String id) {
        return ResultJson.ok(officeRefundService.findDetailById(merchantNo, id));
    }
}
