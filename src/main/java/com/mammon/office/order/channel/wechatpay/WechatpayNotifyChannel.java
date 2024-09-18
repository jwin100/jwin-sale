package com.mammon.office.order.channel.wechatpay;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mammon.config.JsonMapper;
import com.mammon.exception.CustomException;
import com.mammon.office.order.channel.factory.BaseTradeNotifyChannel;
import com.mammon.office.order.channel.factory.model.RefundNotifyVo;
import com.mammon.office.order.channel.factory.model.TradePayVo;
import com.mammon.office.order.channel.wechatpay.model.WechatpayConfigModel;
import com.mammon.office.order.channel.wechatpay.util.WechatpayAesUtil;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author dcl
 * @since 2023/12/15 10:03
 */
@Service
@Slf4j
public class WechatpayNotifyChannel implements BaseTradeNotifyChannel {

    @Resource
    private OfficeTradeLogService officeTradeLogService;

    @Override
    public TradePayVo payNotify(String channelCode, String configStr, HttpServletRequest request) {
        WechatpayConfigModel configModel = JsonUtil.toObject(configStr, WechatpayConfigModel.class);
        if (configModel == null) {
            throw new CustomException("商户信息错误");
        }

        JSONObject object = ConvertServletParam.getRequestBody(request);
        if (object == null) {
            throw new CustomException("微信支付获取回调信息错误");
        }
        log.info("微信支付回调解析参数：{}", object);
        JSONObject resource = object.getJSONObject("resource");
        String associated_data = resource.get("associated_data", String.class);
        String ciphertext = resource.get("ciphertext", String.class);
        String nonce = resource.get("nonce", String.class);
        String notifyStr = WechatpayAesUtil.decryptToString(configModel.getApiV3Key(), associated_data, nonce, ciphertext);
        if (StringUtils.isBlank(notifyStr)) {
            throw new CustomException("微信支付回调信息异常");
        }
        JSONObject notifyObject = JSONUtil.parseObj(notifyStr);
        String tradeNo = notifyObject.getStr("transaction_id");
        String orderNo = notifyObject.getStr("out_trade_no");
        String tradeState = notifyObject.getStr("trade_state");
        String tradeStateDesc = notifyObject.getStr("trade_state_desc");
        JSONObject amount = notifyObject.get("amount", JSONObject.class);
        int payAmount = 0;
        if (amount != null) {
            payAmount = amount.get("payer_total", Integer.class);
        }
//        LocalDateTime payTime = LocalDateTime.parse(notifyObject.getStr("success_time"));
        // 2023-12-15T14:50:38+08:00
        LocalDateTime payTime = LocalDateTime.now();

        officeTradeLogService.save(orderNo, "payNotify", JsonUtil.toJSONString(notifyObject), "", "");
        TradePayVo resp = new TradePayVo();
        if (StringUtils.isBlank(orderNo) && StringUtils.isBlank(tradeNo)) {
            resp.setStatus(OfficeOrderStatus.waitPay.getCode());
            resp.setMessage("回调请求参数错误");
            return resp;
        }
        resp.setTradeNo(tradeNo);
        resp.setOrderNo(orderNo);
        resp.setStatus(convertQueryStatus(tradeState));
        resp.setMessage(tradeStateDesc);
        resp.setAmount(AmountUtil.parseBigDecimal(payAmount));
        resp.setPayTime(payTime);
        return resp;
    }

    @Override
    public RefundNotifyVo refundNotify(Map<String, String> paramMap) {
        return null;
    }

    @Override
    public Object responseSuccess() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "SUCCESS");
        return map;
    }

    @Override
    public Object responseFail() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "FAIL");
        return map;
    }

    private int convertQueryStatus(String status) {
        switch (status) {
            case "SUCCESS":
                return OfficeOrderStatus.paySuccess.getCode();
            case "REFUND":
            case "CLOSED":
            case "REVOKED":
            case "PAYERROR":
                return OfficeOrderStatus.payCancel.getCode();
            case "NOTPAY":
            case "USERPAYING":
            default:
                return OfficeOrderStatus.waitPay.getCode();

        }
    }
}
