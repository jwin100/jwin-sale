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
import com.mammon.trade.channel.factory.model.dto.ChannelRefundDto;
import com.mammon.trade.channel.factory.model.vo.ChannelRefundNotifyVo;
import com.mammon.trade.channel.factory.model.vo.ChannelRefundQueryVo;
import com.mammon.trade.channel.factory.model.vo.ChannelRefundVo;
import com.mammon.trade.common.ConvertServletParam;
import com.mammon.trade.dao.TradeRefundDao;
import com.mammon.trade.model.dto.TradeRefundDto;
import com.mammon.trade.model.entity.TradeChannelEntity;
import com.mammon.trade.model.entity.TradeEntity;
import com.mammon.trade.model.entity.TradeRefundEntity;
import com.mammon.trade.model.enums.TradeRefundStatus;
import com.mammon.trade.model.vo.TradeRefundVo;
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
 * @since 2024/3/8 10:18
 */
@Service
@Slf4j
public class TradeRefundService {

    @Resource
    private TradeRefundDao tradeRefundDao;

    @Resource
    private TradeService tradeService;

    @Resource
    private TradeChannelService tradeChannelService;

    @Resource
    private LeafCodeService leafCodeService;

    public TradeRefundVo tradeRefund(TradeRefundDto dto) {
        TradeEntity trade = tradeService.findBaseByTradeNo(dto.getTradeNo());
        if (trade == null) {
            throw new CustomException("交易信息不存在");
        }
        String refundTradeNo = leafCodeService.generateDocketNo(DocketType.PAYMENT_TRADE_REFUND);
        TradeChannelEntity channel = tradeChannelService.findById(trade.getChannelId());
        ChannelPay factory = ChannelPayFactory.get(channel.getChannelCode());

        ChannelRefundDto refundDto = new ChannelRefundDto();
        refundDto.setTradeNo(trade.getTradeNo());
        refundDto.setRefundTradeNo(refundTradeNo);
        refundDto.setRefundAmount(dto.getRefundAmount());
        ChannelRefundVo channelRefund = factory.channelRefund(channel.getChannelConfig(), refundDto);
        save(trade, refundTradeNo, refundDto.getRefundAmount(), channelRefund);

        TradeRefundVo refundVo = new TradeRefundVo();
        refundVo.setStatus(channelRefund.getStatus());
        refundVo.setDescribe(channelRefund.getDescribe());
        refundVo.setOrderTradeNo(trade.getTradeNo());
        refundVo.setRefundTradeNo(refundTradeNo);
        return refundVo;
    }

    public TradeRefundVo tradeRefundQuery(String refundTradeNo) {
        TradeRefundEntity tradeRefund = tradeRefundDao.findByRefundTradeNo(refundTradeNo);
        if (tradeRefund == null) {
            throw new CustomException("退款信息不存在");
        }
        TradeChannelEntity channel = tradeChannelService.findById(tradeRefund.getChannelId());
        ChannelPay factory = ChannelPayFactory.get(channel.getChannelCode());

        ChannelRefundQueryVo channelRefund = factory.channelRefundQuery(channel.getChannelConfig(), refundTradeNo);
        if (channelRefund == null) {
            throw new CustomException("交易信息不存在");
        }
        if (channelRefund.getStatus() == TradeRefundStatus.FAILED.getCode()) {
            tradeRefundDao.editStatus(tradeRefund.getId(), channelRefund.getStatus(), channelRefund.getDescribe());
        }
        TradeRefundVo refundVo = new TradeRefundVo();
        refundVo.setStatus(channelRefund.getStatus());
        refundVo.setDescribe(channelRefund.getDescribe());
        refundVo.setOrderTradeNo(tradeRefund.getTradeNo());
        refundVo.setRefundTradeNo(tradeRefund.getRefundTradeNo());
        return refundVo;
    }

    public Object tradeRefundNotify(String channelCode,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        Map<String, String> paramMap = ConvertServletParam.convert(request);
        log.info("订单结果回调：{}", JsonUtil.toJSONString(paramMap));

        ChannelPayNotify factory = ChannelPayFactory.getNotify(channelCode);
        ChannelRefundNotifyVo refundNotifyVo = factory.tradeRefundNotify(paramMap);
        if (refundNotifyVo == null) {
            return factory.responseFail("交易信息不存在");
        }
        TradeRefundEntity tradeRefund = tradeRefundDao.findByRefundTradeNo(refundNotifyVo.getRefundTradeNo());
        if (tradeRefund == null) {
            return factory.responseFail("交易退款信息不存在");
        }
        if (refundNotifyVo.getStatus() == TradeRefundStatus.SUCCESS.getCode()) {
            tradeRefundDao.editSuccess(tradeRefund.getId(), refundNotifyVo.getRefundSuccessTime());
        }
        return factory.responseSuccess();
    }

    public void save(TradeEntity trade, String refundTradeNo, long refundAmount, ChannelRefundVo refundVo) {
        TradeRefundEntity entity = new TradeRefundEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(trade.getMerchantNo());
        entity.setStoreNo(trade.getStoreNo());
        entity.setTradeNo(trade.getTradeNo());
        entity.setRefundTradeNo(refundTradeNo);
        entity.setRefundSubject(trade.getOrderSubject());
        entity.setRefundAmount(refundAmount);
        entity.setChannelId(trade.getChannelId());
        entity.setChannelTradeNo(refundVo.getChannelTradeNo());
        entity.setBankOrderNo(refundVo.getBankOrderNo());
        entity.setBankTradeNo(refundVo.getBankTradeNo());
        entity.setPayWay(refundVo.getPayWay());
        entity.setStatus(refundVo.getStatus());
        entity.setDescribe(refundVo.getDescribe());
        entity.setCreateTime(LocalDateTime.now());
        tradeRefundDao.save(entity);
    }
}
