package com.mammon.cashier.channel.factory;

import com.mammon.cashier.channel.factory.dto.TradeMemberPayDto;
import com.mammon.cashier.channel.factory.dto.TradeMemberRefundDto;
import com.mammon.cashier.channel.factory.vo.TradeMemberPayVo;
import com.mammon.cashier.channel.factory.vo.TradeMemberRefundVo;
import com.mammon.cashier.domain.model.*;

/**
 * @author dcl
 * @date 2023-05-29 10:14:50
 */
public interface TradeMemberChannel {

    TradeMemberPayVo pay(TradeMemberPayDto payDto);

    TradeMemberRefundVo refund(TradeMemberRefundDto dto);
}
