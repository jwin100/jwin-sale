package com.mammon.payment.gate;

import com.mammon.common.ResultJson;
import com.mammon.payment.domain.vo.PayModeModel;
import com.mammon.payment.service.PayModeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/gate/payment/pay-mode")
public class GatePayModeController {

    @Resource
    private PayModeService payModeService;

    @GetMapping("/list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        return ResultJson.ok(payModeService.getAll());
    }

    @GetMapping("/cashier")
    public ResultJson cashierPayMode(@RequestHeader long merchantNo,
                                     @RequestHeader long storeNo,
                                     @RequestHeader String accountId) {
        List<PayModeModel> vos = payModeService.cashierPayMode(merchantNo, storeNo, accountId);
        return ResultJson.ok(vos);
    }

    @GetMapping("/refund")
    public ResultJson refundPayMode(@RequestHeader long merchantNo,
                                    @RequestHeader long storeNo,
                                    @RequestHeader String accountId) {
        List<PayModeModel> vos = payModeService.refundPayMode(merchantNo, storeNo, accountId);
        return ResultJson.ok(vos);
    }

    /**
     * 储值支付方式
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @GetMapping("/asset")
    public ResultJson assetPayMode(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId) {
        List<PayModeModel> vos = payModeService.assetPayMode(merchantNo, storeNo, accountId);
        return ResultJson.ok(vos);
    }
}
