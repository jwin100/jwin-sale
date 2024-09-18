package com.mammon.office.order.channel.alipay;

import cn.hutool.json.JSONUtil;
import com.mammon.config.JsonMapper;
import com.mammon.office.order.channel.factory.BaseTradeNotifyChannel;
import com.mammon.office.order.channel.factory.model.RefundNotifyVo;
import com.mammon.office.order.channel.factory.model.TradePayVo;
import com.mammon.office.order.common.ConvertServletParam;
import com.mammon.office.order.domain.enums.OfficeOrderStatus;
import com.mammon.office.order.service.OfficeTradeLogService;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author dcl
 * @date 2023-03-13 22:37:27
 */
@Service
@Slf4j
public class AlipayNotifyChannel implements BaseTradeNotifyChannel {

    @Resource
    private OfficeTradeLogService officeTradeLogService;

    @Override
    public TradePayVo payNotify(String channelCode, String configStr, HttpServletRequest request) {
        Map<String, String> params = ConvertServletParam.convertParam(request);
        String orderNo = params.get("out_trade_no");
        String tradeNo = params.get("trade_no");
        officeTradeLogService.save(orderNo, "payNotify", JsonUtil.toJSONString(params), "", "");
        TradePayVo resp = new TradePayVo();
        if (StringUtils.isBlank(orderNo) && StringUtils.isBlank(tradeNo)) {
            resp.setStatus(OfficeOrderStatus.waitPay.getCode());
            resp.setMessage("回调请求参数错误");
            return resp;
        }

        resp.setOrderNo(orderNo);
        resp.setTradeNo(tradeNo);
        String tradeStatus = params.get("trade_status");
        String receiptAmount = params.get("receipt_amount");
        String payTimeStr = params.get("gmt_payment");
        LocalDateTime payTime = LocalDateTime.parse(payTimeStr, DateTimeFormatter.ofPattern(JsonMapper.DATE_TIME_FORMATTER));
        resp.setStatus(convertQueryStatus(tradeStatus));
        resp.setAmount(AmountUtil.parseBigDecimal(receiptAmount));
        resp.setPayTime(payTime);
        return resp;
    }

    @Override
    public RefundNotifyVo refundNotify(Map<String, String> paramMap) {
        return null;
    }

    private int convertQueryStatus(String status) {
        switch (status) {
            case "TRADE_SUCCESS":
            case "TRADE_FINISHED":
                return OfficeOrderStatus.paySuccess.getCode();
            case "TRADE_CLOSED":
                return OfficeOrderStatus.payCancel.getCode();
            case "WAIT_BUYER_PAY":
            default:
                return OfficeOrderStatus.waitPay.getCode();

        }
    }
}
