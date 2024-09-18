package com.mammon.office.order.open;

import com.mammon.office.order.service.OfficeRefundService;
import com.mammon.office.order.service.OfficeOrderService;
import com.mammon.office.order.service.OfficeTradeNotifyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dcl
 * @date 2023-03-02 14:27:28
 */
@RestController
@RequestMapping("/open/office-order")
public class OpenOfficeOrderController {

    @Resource
    private OfficeTradeNotifyService officeTradeNotifyService;

    @Resource
    private OfficeOrderService officeOrderService;

    @Resource
    private OfficeRefundService officeRefundService;

    /**
     * 订单回调
     *
     * @return
     */
    @RequestMapping("/notify/pay/{channelCode}")
    public Object orderAlipayNotify(@PathVariable("channelCode") String channelCode,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        return officeTradeNotifyService.payNotify(channelCode, request, response);
    }

    @RequestMapping("/notify/refund/{channelCode}")
    public Object orderRefundNotify(@PathVariable("channelCode") String channelCode,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        return officeTradeNotifyService.refundNotify(channelCode, request, response);
    }
}
