package com.mammon.cashier.channel.factory;

import com.mammon.cashier.channel.recharge.TradeMemberRecharge;
import com.mammon.cashier.channel.timecard.TradeMemberCounted;
import com.mammon.config.ApplicationBean;
import com.mammon.payment.domain.enums.PayModeConst;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dcl
 * @date 2023-05-30 15:30:50
 */
public class TradeMemberFactory {

    public static final Map<Integer, TradeMemberChannel> map = new HashMap<>();

    static {
        map.put(PayModeConst.payModeRecharge.getCode(), ApplicationBean.getBean(TradeMemberRecharge.class));
        map.put(PayModeConst.payModeTimeCard.getCode(), ApplicationBean.getBean(TradeMemberCounted.class));
    }

    public static TradeMemberChannel get(int code) {
        return map.get(code);
    }
}
