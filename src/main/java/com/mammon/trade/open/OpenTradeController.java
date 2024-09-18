package com.mammon.trade.open;

import com.mammon.trade.service.TradeRefundService;
import com.mammon.trade.service.TradeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dcl
 * @since 2024/3/6 14:08
 */
@RestController
@RequestMapping("/open/trade")
public class OpenTradeController {

    @Resource
    private TradeService tradeService;

    @Resource
    private TradeRefundService tradeRefundService;

    /**
     * 订单回调
     *
     * @return
     */
    @RequestMapping("/notify/{channelCode}")
    public Object orderAlipayNotify(@PathVariable("channelCode") String channelCode,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        return tradeService.tradeNotify(channelCode, request, response);
    }

    @RequestMapping("/refund-notify/{channelCode}")
    public Object orderRefundNotify(@PathVariable("channelCode") String channelCode,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        return tradeRefundService.tradeRefundNotify(channelCode, request, response);
    }
}
