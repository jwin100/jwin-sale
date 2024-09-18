package com.mammon.office.order.channel.wechatpay;

import cn.hutool.json.JSONUtil;
import com.mammon.config.JsonMapper;
import com.mammon.exception.CustomException;
import com.mammon.office.order.channel.factory.BaseTradePayChannel;
import com.mammon.office.order.channel.factory.model.TradePayVo;
import com.mammon.office.order.channel.factory.model.TradeRefundQueryVo;
import com.mammon.office.order.channel.factory.model.TradeRefundVo;
import com.mammon.office.order.channel.wechatpay.model.WechatpayConfigModel;
import com.mammon.office.order.domain.entity.OfficeOrderEntity;
import com.mammon.utils.JsonUtil;
import com.mammon.utils.QrCodeUtil;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author dcl
 * @since 2023/12/15 10:03
 */
@Service
public class WechatpayChannel implements BaseTradePayChannel {

    @Override
    public String tradeNative(String channelCode, String configStr, OfficeOrderEntity order) {
        String notifyUrl = getNotifyUrl(channelCode);

        WechatpayConfigModel configModel = JsonUtil.toObject(configStr, WechatpayConfigModel.class);
        if (configModel == null) {
            throw new CustomException("商户信息错误");
        }
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(configModel.getMchId())
                .privateKey(configModel.getPrivateKey())
                .merchantSerialNumber(configModel.getSerialNumber())
                .apiV3Key(configModel.getApiV3Key())
                .build();
        NativePayService service = new NativePayService.Builder().config(config).build();
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal((int) order.getPayableAmount());
        request.setAmount(amount);
        request.setAppid(configModel.getAppId());
        request.setMchid(configModel.getMchId());
        request.setDescription(order.getSubject());
        request.setNotifyUrl(notifyUrl);
        request.setOutTradeNo(order.getOrderNo());
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        System.out.println(response.getCodeUrl());
        return QrCodeUtil.getBase64QRCode(response.getCodeUrl());
    }

    @Override
    public TradePayVo tradeQuery(String configStr, String orderNo) {
        return null;
    }

    @Override
    public TradeRefundVo tradeRefund(String configStr, String orderNo, String refundNo, BigDecimal refundAmount) {
        return null;
    }

    @Override
    public TradeRefundQueryVo tradeRefundQuery(String configStr, String orderNo, String refundNo) {
        return null;
    }
}
