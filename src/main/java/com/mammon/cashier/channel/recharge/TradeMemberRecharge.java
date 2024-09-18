package com.mammon.cashier.channel.recharge;

import com.mammon.cashier.channel.factory.TradeMemberChannel;
import com.mammon.cashier.channel.factory.dto.TradeMemberPayDto;
import com.mammon.cashier.channel.factory.dto.TradeMemberRefundDto;
import com.mammon.cashier.channel.factory.enums.TradeMemberStatus;
import com.mammon.cashier.channel.factory.vo.TradeMemberPayVo;
import com.mammon.cashier.channel.factory.vo.TradeMemberRefundVo;
import com.mammon.exception.CustomException;
import com.mammon.member.domain.dto.MemberAssetsConsumeDto;
import com.mammon.member.service.MemberAssetsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2023-05-29 10:26:19
 */
@Service
public class TradeMemberRecharge implements TradeMemberChannel {

    @Resource
    private MemberAssetsService memberAssetsService;

    @Override
    public TradeMemberPayVo pay(TradeMemberPayDto payDto) {
        TradeMemberPayVo payVo = new TradeMemberPayVo();
        try {
            MemberAssetsConsumeDto dto = new MemberAssetsConsumeDto();
            dto.setMerchantNo(payDto.getMerchantNo());
            dto.setStoreNo(payDto.getStoreNo());
            dto.setAccountId(payDto.getAccountId());
            dto.setMemberId(payDto.getMemberId());
            dto.setOrderNo(payDto.getOrderNo());
            dto.setChangeAmount(payDto.getAmount());
            memberAssetsService.rechargeConsume(dto);
            payVo.setStatus(TradeMemberStatus.SUCCESS.getCode());
            payVo.setDescribe(TradeMemberStatus.SUCCESS.getName());
            return payVo;
        } catch (CustomException e) {
            payVo.setStatus(TradeMemberStatus.FAILED.getCode());
            payVo.setDescribe("支付异常");
            if (e.getResultJson() != null) {
                payVo.setDescribe(e.getResultJson().getMsg());
            }
            return payVo;
        }
    }

    @Override
    public TradeMemberRefundVo refund(TradeMemberRefundDto refundDto) {
        TradeMemberRefundVo refundVo = new TradeMemberRefundVo();
        try {
            MemberAssetsConsumeDto dto = new MemberAssetsConsumeDto();
            dto.setMerchantNo(refundDto.getMerchantNo());
            dto.setStoreNo(refundDto.getStoreNo());
            dto.setAccountId(refundDto.getAccountId());
            dto.setMemberId(refundDto.getMemberId());
            dto.setOrderNo(refundDto.getRefundNo());
            dto.setChangeAmount(refundDto.getRefundAmount());
            memberAssetsService.rechargeRefund(dto);
            refundVo.setStatus(TradeMemberStatus.SUCCESS.getCode());
            refundVo.setDescribe(TradeMemberStatus.SUCCESS.getName());
            return refundVo;
        } catch (CustomException e) {
            refundVo.setStatus(TradeMemberStatus.FAILED.getCode());
            refundVo.setDescribe("退款提交异常");
            if (e.getResultJson() != null) {
                refundVo.setDescribe(e.getResultJson().getMsg());
            }
            return refundVo;
        }
    }
}
