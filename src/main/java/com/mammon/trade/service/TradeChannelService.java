package com.mammon.trade.service;

import com.mammon.exception.CustomException;
import com.mammon.trade.dao.TradeChannelDao;
import com.mammon.trade.model.entity.TradeChannelEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/3/5 14:25
 */
@Service
public class TradeChannelService {

    @Resource
    private TradeChannelDao tradeChannelDao;

    public TradeChannelEntity findById(String id) {
        TradeChannelEntity channel = tradeChannelDao.findById(id);
        if (channel == null) {
            throw new CustomException("商户未开通移动支付，请确认");
        }
        return channel;
    }

    /**
     * 查询商户当前启用的支付渠道
     *
     * @param merchantNo
     * @param storeNo
     * @return
     */
    public TradeChannelEntity findByStoreNo(long merchantNo, long storeNo) {
        TradeChannelEntity channel = tradeChannelDao.findByStoreNo(merchantNo, storeNo);
        if (channel == null) {
            throw new CustomException("商户未开通移动支付，请确认");
        }
        return channel;
    }
}
