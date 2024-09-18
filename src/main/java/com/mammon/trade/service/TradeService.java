package com.mammon.trade.service;

import cn.hutool.json.JSONUtil;
import com.mammon.common.Generate;
import com.mammon.config.JsonMapper;
import com.mammon.exception.CustomException;
import com.mammon.leaf.enums.DocketType;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.trade.channel.factory.ChannelPay;
import com.mammon.trade.channel.factory.ChannelPayFactory;
import com.mammon.trade.channel.factory.ChannelPayNotify;
import com.mammon.trade.channel.factory.model.dto.ChannelDto;
import com.mammon.trade.channel.factory.model.vo.*;
import com.mammon.trade.common.ConvertServletParam;
import com.mammon.trade.consts.AuthCodeConst;
import com.mammon.trade.dao.TradeDao;
import com.mammon.trade.model.dto.TradeDto;
import com.mammon.trade.model.dto.TradeRefundDto;
import com.mammon.trade.model.entity.TradeChannelEntity;
import com.mammon.trade.model.entity.TradeEntity;
import com.mammon.trade.model.enums.*;
import com.mammon.trade.model.vo.*;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author dcl
 * @since 2024/3/1 15:21
 */
@Service
@Slf4j
public class TradeService {

    @Resource
    private LeafCodeService leafCodeService;

    @Resource
    private TradeChannelService tradeChannelService;

    @Resource
    private TradeDao tradeDao;

    // C扫B，B扫C，查单，

    // 订单关闭，仅用于预下单接口且未支付的订单
    // 订单撤销，付交易返回失败或支付系统超时，调用该接口撤销交易。
    // 订单退款，
    // 退款查询

    public TradeNativeVo tradeNative(TradeDto dto) {
        TradeChannelEntity channel = tradeChannelService.findByStoreNo(dto.getMerchantNo(), dto.getStoreNo());
        TradeEntity trade = save(TradePayMode.NATIVE, channel, dto);
        ChannelPay factory = ChannelPayFactory.get(channel.getChannelCode());
        ChannelDto channelDto = convert(trade.getTradeNo(), dto);
        ChannelNativeVo nativeVo = factory.channelNative(channel.getChannelCode(), channel.getChannelConfig(), channelDto);
        int status = TradeStatus.WAIT_PAYMENT.getCode();
        if (nativeVo.getStatus() == TradeCommonStatus.FAILED.getCode()) {
            status = TradeStatus.FAILED.getCode();
        }
        trade.setStatus(status);
        trade.setDescribe(nativeVo.getDescribe());
        trade.setChannelTradeNo(nativeVo.getChannelTradeNo());
        trade.setBankOrderNo(nativeVo.getBankOrderNo());
        trade.setBankTradeNo(nativeVo.getBankTradeNo());
        tradeDao.edit(trade);

        TradeNativeVo vo = new TradeNativeVo();
        vo.setStatus(nativeVo.getStatus());
        vo.setDescribe(nativeVo.getDescribe());
        vo.setOrderNo(dto.getOrderNo());
        vo.setCodeUrl(nativeVo.getCodeUrl());
        vo.setTradeNo(trade.getTradeNo());
        return vo;
    }

    public TradeAuthVo tradeAuth(TradeDto dto) {
        TradeChannelEntity channel = tradeChannelService.findByStoreNo(dto.getMerchantNo(), dto.getStoreNo());
        TradeEntity trade = save(TradePayMode.AUTH, channel, dto);
        ChannelPay factory = ChannelPayFactory.get(channel.getChannelCode());
        ChannelDto channelDto = convert(trade.getTradeNo(), dto);
        ChannelAuthVo authVo = factory.channelAuth(channel.getChannelCode(), channel.getChannelConfig(), channelDto);

        int status = TradeStatus.WAIT_PAYMENT.getCode();
        if (authVo.getStatus() == TradeAuthStatus.SUCCESS.getCode()) {
            status = TradeStatus.SUCCESS.getCode();
        } else if (authVo.getStatus() == TradeAuthStatus.FAILED.getCode()) {
            status = TradeStatus.FAILED.getCode();
        }
        trade.setStatus(status);
        trade.setDescribe(authVo.getDescribe());
        trade.setChannelTradeNo(authVo.getChannelTradeNo());
        trade.setBankOrderNo(authVo.getBankOrderNo());
        trade.setBankTradeNo(authVo.getBankTradeNo());
        trade.setSuccessTime(authVo.getSuccessTime());
        tradeDao.edit(trade);

        TradeAuthVo vo = new TradeAuthVo();
        vo.setStatus(authVo.getStatus());
        vo.setDescribe(authVo.getDescribe());
        vo.setOrderNo(trade.getOrderNo());
        vo.setTradeNo(authVo.getTradeNo());
        vo.setPayWay(trade.getPayWay());
        vo.setSuccessTime(authVo.getSuccessTime());
        return vo;
    }

    public TradeJsApiVo tradeJsApi(TradeDto dto) {
        TradeChannelEntity channel = tradeChannelService.findByStoreNo(dto.getMerchantNo(), dto.getStoreNo());
        TradeEntity trade = save(TradePayMode.JSAPI, channel, dto);
        ChannelPay factory = ChannelPayFactory.get(channel.getChannelCode());
        ChannelDto channelDto = convert(trade.getTradeNo(), dto);
        ChannelJsApiVo jsApiVo = factory.channelJsApi(channel.getChannelCode(), channel.getChannelConfig(), channelDto);

        int status = TradeStatus.WAIT_PAYMENT.getCode();
        if (jsApiVo.getStatus() == TradeCommonStatus.FAILED.getCode()) {
            status = TradeStatus.FAILED.getCode();
        }
        trade.setStatus(status);
        trade.setDescribe(jsApiVo.getDescribe());
        trade.setChannelTradeNo(jsApiVo.getChannelTradeNo());
        trade.setBankOrderNo(jsApiVo.getBankOrderNo());
        trade.setBankTradeNo(jsApiVo.getBankTradeNo());
        tradeDao.edit(trade);

        TradeJsApiVo vo = new TradeJsApiVo();
        vo.setStatus(jsApiVo.getStatus());
        vo.setDescribe(jsApiVo.getDescribe());
        vo.setOrderNo(dto.getOrderNo());
        vo.setTradeNo(trade.getTradeNo());
        vo.setPrepayId(jsApiVo.getPrepayId());
        vo.setPrepayData(jsApiVo.getPrepayData());
        return vo;
    }

    public TradeVo tradeQuery(String orderNo) {
        TradeEntity trade = tradeDao.findByOrderNo(orderNo);
        if (trade == null) {
            throw new CustomException("交易信息不存在");
        }
        if (trade.getStatus() == TradeStatus.WAIT_PAYMENT.getCode()) {
            return channelTradeQuery(trade);
        }
        TradeVo vo = new TradeVo();
        vo.setOrderNo(trade.getOrderNo());
        vo.setTradeNo(trade.getTradeNo());
        vo.setStatus(trade.getStatus());
        vo.setPayMode(trade.getPayMode());
        vo.setPayWay(trade.getPayWay());
        vo.setOrderAmount(trade.getOrderAmount());
        vo.setRefundAmount(trade.getRefundAmount());
        vo.setSuccessTime(trade.getSuccessTime());
        return vo;
    }

    public TradeVo channelTradeQuery(TradeEntity trade) {
        TradeChannelEntity channel = tradeChannelService.findById(trade.getChannelId());
        if (channel == null) {
            throw new CustomException("交易渠道不存在");
        }
        ChannelPay factory = ChannelPayFactory.get(channel.getChannelCode());
        ChannelQueryVo queryVo = factory.channelQuery(channel.getChannelConfig(), trade.getTradeNo());
        if (queryVo == null) {
            throw new CustomException("交易信息不存在");
        }
        // 由待付款修改为支付结果
        tradeDao.editStatus(trade.getId(), TradeStatus.WAIT_PAYMENT.getCode(), queryVo.getStatus(), queryVo.getDescribe());
        if (queryVo.getStatus() == TradeStatus.SUCCESS.getCode()) {
            // 如果交易完成，修改买家实际支付金额和支付日期
            tradeDao.editSuccess(trade.getId(), queryVo.getBuyerPayAmount(), queryVo.getSuccessTime());
        }
        if (queryVo.isRefund()) {
            // 说明有成功退款记录，修改订单成功退款总金额
            tradeDao.editRefund(trade.getId(), TradeRefund.HAVE_REFUND.getCode(), queryVo.getRefundAmount());
        }
        TradeVo vo = new TradeVo();
        vo.setOrderNo(trade.getOrderNo());
        vo.setTradeNo(trade.getTradeNo());
        vo.setStatus(queryVo.getStatus());
        vo.setPayMode(trade.getPayMode());
        vo.setPayWay(trade.getPayWay());
        vo.setOrderAmount(trade.getOrderAmount());
        vo.setRefundAmount(trade.getRefundAmount());
        vo.setSuccessTime(queryVo.getSuccessTime());
        return vo;
    }

    /**
     * 订单关闭
     * <p>
     * 预下单接口超时未支付的订单，调用关单
     * <p>
     * 非预下单接口是同步阻塞形式拿支付结果
     *
     * @param orderNo
     */
    public TradeCloseVo tradeClose(String orderNo) {
        TradeEntity trade = tradeDao.findByOrderNo(orderNo);
        if (trade == null) {
            throw new CustomException("交易信息不存在");
        }
        TradeChannelEntity channel = tradeChannelService.findById(trade.getChannelId());
        if (channel == null) {
            throw new CustomException("交易渠道不存在");
        }
        ChannelPay factory = ChannelPayFactory.get(channel.getChannelCode());
        ChannelCloseVo closeVo = factory.channelClose(channel.getChannelConfig(), trade.getTradeNo());

        if (closeVo.getStatus() == TradeCommonStatus.SUCCESS.getCode()) {
            tradeDao.editStatus(trade.getId(), trade.getStatus(), TradeStatus.CLOSE.getCode(), null);
        }

        TradeCloseVo vo = new TradeCloseVo();
        vo.setStatus(closeVo.getStatus());
        vo.setDescribe(closeVo.getDescribe());
        vo.setOrderNo(orderNo);
        vo.setTradeNo(trade.getTradeNo());
        return vo;
    }

    /**
     * 订单撤销
     * <p>
     * 支付交易返回失败或支付系统超时，调用该接口撤销交易
     * <p>
     * 只有发生支付系统超时或者支付结果未知时可调用撤销
     *
     * @param orderNo 商户订单号
     */
    public TradeCancelVo tradeCancel(String orderNo) {
        TradeEntity trade = tradeDao.findByOrderNo(orderNo);
        if (trade == null) {
            throw new CustomException("交易信息不存在");
        }
        TradeChannelEntity channel = tradeChannelService.findById(trade.getChannelId());
        if (channel == null) {
            throw new CustomException("交易渠道不存在");
        }
        ChannelPay factory = ChannelPayFactory.get(channel.getChannelCode());
        ChannelCancelVo cancelVo = factory.channelCancel(channel.getChannelConfig(), trade.getTradeNo());

        if (cancelVo.getStatus() == TradeCommonStatus.SUCCESS.getCode()) {
            tradeDao.editStatus(trade.getId(), trade.getStatus(), TradeStatus.CANCEL.getCode(), null);
        }

        TradeCancelVo vo = new TradeCancelVo();
        vo.setStatus(cancelVo.getStatus());
        vo.setDescribe(cancelVo.getDescribe());
        vo.setOrderNo(orderNo);
        vo.setTradeNo(trade.getTradeNo());
        return vo;
    }

    public Object tradeNotify(String channelCode,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        Map<String, String> paramMap = ConvertServletParam.convert(request);
        log.info("交易结果回调：{}", JsonUtil.toJSONString(paramMap));

        ChannelPayNotify factory = ChannelPayFactory.getNotify(channelCode);
        ChannelNotifyVo notifyVo = factory.tradeNotify(paramMap);
        if (notifyVo == null) {
            return factory.responseFail("交易信息不存在");
        }
        TradeEntity trade = tradeDao.findByTradeNo(notifyVo.getTradeNo());
        if (trade == null) {
            return factory.responseFail("交易信息不存在");
        }
        trade.setPayMode(notifyVo.getPayMode());
        trade.setPayWay(notifyVo.getPayWay());
        trade.setChannelTradeNo(notifyVo.getChannelTradeNo());
        trade.setBankOrderNo(notifyVo.getBankOrderNo());
        trade.setBankTradeNo(notifyVo.getBankTradeNo());
        tradeDao.edit(trade);
        tradeDao.editStatus(trade.getId(), TradeStatus.WAIT_PAYMENT.getCode(), notifyVo.getStatus(), notifyVo.getDescribe());
        if (notifyVo.getStatus() == TradeStatus.SUCCESS.getCode()) {
            tradeDao.editSuccess(trade.getId(), notifyVo.getPayerAmount(), notifyVo.getSuccessTime());
        }
        return factory.responseSuccess();
    }

    public TradeEntity save(TradePayMode payMode, TradeChannelEntity channel, TradeDto dto) {
        String tradeNo = leafCodeService.generateDocketNo(DocketType.PAYMENT_TRADE);

        TradePayWay payWay = TradePayWay.OTHER;
        if (payMode.getCode() == TradePayMode.AUTH.getCode()) {
            payWay = AuthCodeConst.convertPayWay(dto.getAuthCode());
        }

        TradeEntity entity = new TradeEntity();
        entity.setId(Generate.generateUUID());
        entity.setTradeNo(tradeNo);
        entity.setMerchantNo(dto.getMerchantNo());
        entity.setStoreNo(dto.getStoreNo());
        entity.setChannelId(channel.getId());
        entity.setPayMode(payMode.getCode());
        entity.setPayWay(payWay.getCode());
        entity.setOrderNo(dto.getOrderNo());
        entity.setOrderSubject(dto.getOrderSubject());
        entity.setOrderAmount(dto.getOrderAmount());
        entity.setMemberId(dto.getMemberId());
        entity.setAuthCode(dto.getAuthCode());
        entity.setStatus(TradeStatus.CREATED.getCode());
        entity.setRefund(TradeRefund.NOT_REFUND.getCode());
        entity.setCreateTime(LocalDateTime.now());
        tradeDao.save(entity);
        return entity;
    }

    public TradeEntity findBaseByTradeNo(String tradeNo) {
        return tradeDao.findByTradeNo(tradeNo);
    }

    private ChannelDto convert(String tradeNo, TradeDto dto) {
        ChannelDto channelDto = new ChannelDto();
        channelDto.setTradeNo(tradeNo);
        channelDto.setOrderSubject(dto.getOrderSubject());
        channelDto.setOrderAmount(dto.getOrderAmount());
        channelDto.setAuthCode(dto.getAuthCode());
        return channelDto;
    }
}
