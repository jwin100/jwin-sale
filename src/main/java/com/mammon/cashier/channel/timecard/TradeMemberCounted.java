package com.mammon.cashier.channel.timecard;

import com.mammon.cashier.channel.factory.TradeMemberChannel;
import com.mammon.cashier.channel.factory.dto.TradeMemberPayDto;
import com.mammon.cashier.channel.factory.dto.TradeMemberRefundDto;
import com.mammon.cashier.channel.factory.enums.TradeMemberStatus;
import com.mammon.cashier.channel.factory.vo.TradeMemberPayVo;
import com.mammon.cashier.channel.factory.vo.TradeMemberRefundVo;
import com.mammon.cashier.domain.model.*;
import com.mammon.member.domain.dto.MemberTimeCardConsumeDto;
import com.mammon.member.domain.vo.TimeCardChangeVo;
import com.mammon.member.service.MemberTimeCardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2023-05-29 10:34:26
 */
@Service
public class TradeMemberCounted implements TradeMemberChannel {

    @Resource
    private MemberTimeCardService memberTimeCardService;

    @Override
    public TradeMemberPayVo pay(TradeMemberPayDto payDto) {
        MemberTimeCardConsumeDto consumeDto = new MemberTimeCardConsumeDto();
        consumeDto.setOrderNo(payDto.getOrderNo());
        consumeDto.setMemberId(payDto.getMemberId());
        consumeDto.setCountedId(payDto.getCountedId());
        consumeDto.setCountedTotal(payDto.getCountedTotal());
        TimeCardChangeVo vo = memberTimeCardService.timeCardConsume(payDto.getAccountId(), consumeDto);

        TradeMemberPayVo payVo = new TradeMemberPayVo();
        payVo.setTradeNo(vo.getTradeNo());
        if (vo.getCode() == 1) {
            payVo.setStatus(TradeMemberStatus.SUCCESS.getCode());
            payVo.setDescribe(TradeMemberStatus.SUCCESS.getName());
            return payVo;
        }
        payVo.setStatus(TradeMemberStatus.FAILED.getCode());
        payVo.setDescribe(vo.getMessage());
        return payVo;
    }

    @Override
    public TradeMemberRefundVo refund(TradeMemberRefundDto dto) {
        MemberTimeCardConsumeDto consumeDto = new MemberTimeCardConsumeDto();
        consumeDto.setOrderNo(dto.getOrderNo());
        consumeDto.setMemberId(dto.getMemberId());
        consumeDto.setCountedId(dto.getCountedId());
        consumeDto.setCountedTotal(dto.getCountedTotal());
        TimeCardChangeVo vo = memberTimeCardService.timeCardRefund(dto.getAccountId(), consumeDto);
        TradeMemberRefundVo refundVo = new TradeMemberRefundVo();
        if (vo.getCode() == 1) {
            refundVo.setStatus(TradeMemberStatus.SUCCESS.getCode());
            refundVo.setDescribe(TradeMemberStatus.SUCCESS.getName());
            return refundVo;
        }
        refundVo.setStatus(TradeMemberStatus.FAILED.getCode());
        refundVo.setDescribe(vo.getMessage());
        return refundVo;
    }
}
