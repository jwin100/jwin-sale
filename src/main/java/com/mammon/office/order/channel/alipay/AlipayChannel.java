package com.mammon.office.order.channel.alipay;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.mammon.config.JsonMapper;
import com.mammon.exception.CustomException;
import com.mammon.office.order.channel.alipay.model.AlipayConfigModel;
import com.mammon.office.order.channel.factory.model.TradeRefundQueryVo;
import com.mammon.office.order.channel.factory.model.TradeRefundVo;
import com.mammon.office.order.domain.enums.OfficeOrderPayTypeConst;
import com.mammon.office.order.domain.enums.OfficeOrderRefundStatus;
import com.mammon.office.order.domain.enums.OfficeOrderStatus;
import com.mammon.office.order.channel.factory.model.TradeNativeVo;
import com.mammon.office.order.channel.factory.BaseTradePayChannel;
import com.mammon.office.order.channel.factory.model.TradePayVo;
import com.mammon.office.order.domain.entity.OfficeOrderEntity;
import com.mammon.office.order.service.OfficeTradeLogService;
import com.mammon.office.order.service.OfficeTradeOrderService;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import com.mammon.utils.QrCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-02-02 15:45:24
 */
@Service
public class AlipayChannel implements BaseTradePayChannel {

    private static final String serverUrl = "https://openapi.alipay.com/gateway.do";
    private static final String requestFormat = "json";
    private static final String requestCharset = "utf-8";

    @Resource
    private OfficeTradeLogService officeTradeLogService;

    @Resource
    private OfficeTradeOrderService officeTradeOrderService;

    @Override
    public String tradeNative(String channelCode, String configStr, OfficeOrderEntity order) {
        String notifyUrl = getNotifyUrl(channelCode);

        TradeNativeVo vo = new TradeNativeVo();
        vo.setOrderNo(order.getOrderNo());
        String requestJson = "";
        try {
            AlipayConfigModel configModel = JsonUtil.toObject(configStr, AlipayConfigModel.class);
            AlipayClient client = new DefaultAlipayClient(serverUrl, configModel.getAppId(), configModel.getPrivateKey(),
                    requestFormat, requestCharset, configModel.getAlipayPublicKey(), configModel.getSignType());

            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            request.setNotifyUrl(notifyUrl);

            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", order.getOrderNo());
            bizContent.put("total_amount", AmountUtil.parseBigDecimal(order.getPayableAmount()));
            bizContent.put("subject", order.getSubject());
            request.setBizContent(bizContent.toString());
            AlipayTradePrecreateResponse response = client.execute(request);
            requestJson = JsonUtil.toJSONString(request);
            String responseJson = JsonUtil.toJSONString(response);
            officeTradeLogService.save(order.getOrderNo(), "preCreate", requestJson, responseJson, "");
            if (response.isSuccess()) {
                return QrCodeUtil.getBase64QRCode(response.getQrCode(), OfficeOrderPayTypeConst.alipayLogoUrl);
            }
            throw new CustomException(response.getSubMsg());
        } catch (Exception e) {
            officeTradeLogService.save(order.getOrderNo(), "preCreate", requestJson, "", e.getMessage());
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public TradePayVo tradeQuery(String configStr, String orderNo) {
        TradePayVo vo = new TradePayVo();
        vo.setOrderNo(orderNo);
        vo.setStatus(OfficeOrderStatus.waitPay.getCode());
        String requestJson = "";
        try {
            AlipayConfigModel configModel = JsonUtil.toObject(configStr, AlipayConfigModel.class);
            AlipayClient client = new DefaultAlipayClient(serverUrl, configModel.getAppId(), configModel.getPrivateKey(),
                    requestFormat, requestCharset, configModel.getAlipayPublicKey(), configModel.getSignType());

            AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            alipayRequest.setBizContent(bizContent.toString());
            AlipayTradeQueryResponse response = client.execute(alipayRequest);
            requestJson = JsonUtil.toJSONString(alipayRequest);
            String responseJson = JsonUtil.toJSONString(response);
            officeTradeLogService.save(orderNo, "query", requestJson, responseJson, "");
            if (response.isSuccess() && "10000".equals(response.getCode())) {
                vo.setTradeNo(response.getTradeNo());
                vo.setStatus(convertPayStatus(response.getTradeStatus()));
                vo.setAmount(AmountUtil.parseBigDecimal(response.getReceiptAmount()));
                vo.setPayTime(LocalDateTime.now());
                return vo;
            }
            vo.setMessage(response.getSubMsg());
            return vo;
        } catch (Exception e) {
            officeTradeLogService.save(orderNo, "query", requestJson, "", e.getMessage());
            vo.setMessage(e.getMessage());
            return vo;
        }
    }

    @Override
    public TradeRefundVo tradeRefund(String configStr, String orderNo, String refundNo, BigDecimal refundAmount) {
        TradeRefundVo vo = new TradeRefundVo();
        vo.setOrderNo(orderNo);
        vo.setRefundNo(refundNo);
        String requestJson = "";
        try {
            AlipayConfigModel configModel = JsonUtil.toObject(configStr, AlipayConfigModel.class);
            AlipayClient client = new DefaultAlipayClient(serverUrl, configModel.getAppId(), configModel.getPrivateKey(),
                    requestFormat, requestCharset, configModel.getAlipayPublicKey(), configModel.getSignType());

            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            bizContent.put("refund_amount", refundAmount);
            bizContent.put("out_request_no", refundNo);
            request.setBizContent(bizContent.toString());

            AlipayTradeRefundResponse response = client.execute(request);
            requestJson = JsonUtil.toJSONString(request);
            String responseJson = JsonUtil.toJSONString(response);
            officeTradeLogService.save(refundNo, "refund", requestJson, responseJson, "");
            vo.setTradeNo(response.getTradeNo());
            if (response.isSuccess() && "10000".equals(response.getCode())) {
                vo.setCode(1);
                vo.setMessage("退款请求成功");
                return vo;
            }
            vo.setCode(2);
            vo.setMessage(response.getSubMsg());
            return vo;
        } catch (Exception e) {
            officeTradeLogService.save(refundNo, "refund", requestJson, "", e.getMessage());
            vo.setCode(2);
            vo.setMessage(e.getMessage());
            return vo;
        }
    }

    @Override
    public TradeRefundQueryVo tradeRefundQuery(String configStr, String orderNo, String refundNo) {
        TradeRefundQueryVo vo = new TradeRefundQueryVo();
        vo.setRefundNo(refundNo);
        vo.setStatus(OfficeOrderRefundStatus.refunding.getCode());
        String requestJson = "";
        try {
            AlipayConfigModel configModel = JsonUtil.toObject(configStr, AlipayConfigModel.class);
            AlipayClient client = new DefaultAlipayClient(serverUrl, configModel.getAppId(), configModel.getPrivateKey(),
                    requestFormat, requestCharset, configModel.getAlipayPublicKey(), configModel.getSignType());
            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            bizContent.put("out_request_no", refundNo);
            request.setBizContent(bizContent.toString());
            AlipayTradeFastpayRefundQueryResponse response = client.execute(request);
            requestJson = JsonUtil.toJSONString(request);
            String responseJson = JsonUtil.toJSONString(response);
            officeTradeLogService.save(refundNo, "refundQuery", requestJson, responseJson, "");
            if (response.isSuccess() && "10000".equals(response.getCode())) {
                vo.setTradeNo(response.getTradeNo());
                vo.setStatus(convertRefundStatus(response.getRefundStatus()));
                vo.setMessage(response.getSubMsg());
                return vo;
            }
            vo.setMessage(response.getSubMsg());
            return vo;
        } catch (Exception e) {
            officeTradeLogService.save(refundNo, "refundQuery", requestJson, "", e.getMessage());
            vo.setMessage(e.getMessage());
            return vo;
        }
    }

    private int convertPayStatus(String status) {
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

    private int convertRefundStatus(String status) {
        switch (status) {
            case "REFUND_SUCCESS":
                return OfficeOrderRefundStatus.refundSuccess.getCode();
            default:
                return OfficeOrderRefundStatus.refunding.getCode();
        }
    }
}
