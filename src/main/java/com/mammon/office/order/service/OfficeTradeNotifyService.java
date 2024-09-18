package com.mammon.office.order.service;

import cn.hutool.json.JSONUtil;
import com.mammon.config.JsonMapper;
import com.mammon.exception.CustomException;
import com.mammon.office.order.channel.factory.BaseTradeNotifyChannel;
import com.mammon.office.order.channel.factory.TradeNotifyChannelFactory;
import com.mammon.office.order.channel.factory.model.TradePayVo;
import com.mammon.office.order.common.ConvertServletParam;
import com.mammon.office.order.domain.entity.OfficePayChannelEntity;
import com.mammon.office.order.domain.enums.OfficeOrderStatus;
import com.mammon.office.order.domain.vo.OfficeOrderVo;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author dcl
 * @date 2023-03-14 00:09:36
 */
@Service
@Slf4j
public class OfficeTradeNotifyService {

    @Resource
    private OfficeOrderService officeOrderService;

    @Resource
    private OfficePayChannelService officePayChannelService;

    public Object payNotify(String channelCode,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        BaseTradeNotifyChannel notifyChannel = TradeNotifyChannelFactory.get(channelCode);
        OfficePayChannelEntity payChannel = officePayChannelService.findByCode(channelCode);
        if (notifyChannel == null || payChannel == null) {
            throw new CustomException("支付渠道错误");
        }
        TradePayVo payVo = notifyChannel.payNotify(channelCode, payChannel.getConfigStr(), request);
        log.info("商城订单结果通知：channelCode:{},result:{}", channelCode, JsonUtil.toJSONString(payVo));
        OfficeOrderVo order = officeOrderService.findByOrderNo(0, payVo.getOrderNo());
        if (payVo.getStatus() == OfficeOrderStatus.paySuccess.getCode() ||
                payVo.getStatus() == OfficeOrderStatus.payCancel.getCode()) {
            officeOrderService.payResult(order.getMerchantNo(), order.getStoreNo(), order.getId(),
                    OfficeOrderStatus.waitPay.getCode(), payVo);
        }
        return notifyChannel.responseSuccess();
    }

    public Object refundNotify(String channelCode,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        Map<String, String> paramMap = ConvertServletParam.convertParam(request);
//        BaseShopNotifyChannel channel = ShopChannelNotifyFactory.get(channelCode);
//        RefundNotifyResp resp = channel.refundNotify(paramMap);
//        ShopTradeEntity trade = shopTradeService.findByOrderNoAndTradeNo("", resp.getTradeNo());
//        if (trade == null) {
//            throw new Exception("交易信息不存在");
//        }
//        ShopChannelTradeEntity channelTrade = shopChannelTradeService.findByTradeId(trade.getId());
//        if (channelTrade == null) {
//            throw new Exception("交易渠道信息不存在，无法退款");
//        }
//        //修改channelTrade中状态
//        log.info(mapper.writeValueAsString(channelTrade));
//        return channel.notifyReturn();
        return null;
    }
}
