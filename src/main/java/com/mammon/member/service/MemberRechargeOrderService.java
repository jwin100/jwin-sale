package com.mammon.member.service;

import com.mammon.common.Generate;
import com.mammon.member.dao.MemberRechargeOrderDao;
import com.mammon.member.domain.dto.MemberRechargeOrderDto;
import com.mammon.member.domain.entity.MemberRechargeOrderEntity;
import com.mammon.member.domain.query.MemberRechargeOrderSummaryQuery;
import com.mammon.utils.AmountUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dcl
 * @since 2024/2/21 16:31
 */
@Service
public class MemberRechargeOrderService {

    @Resource
    private MemberRechargeOrderDao memberRechargeOrderDao;

    public void create(long merchantNo, long storeNo, MemberRechargeOrderDto dto) {
        MemberRechargeOrderEntity entity = new MemberRechargeOrderEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setRefunded(0);
        if (entity.getRechargeTime() == null) {
            entity.setRechargeTime(LocalDateTime.now());
        }
        memberRechargeOrderDao.save(entity);
    }

    public void refund(long merchantNo, String orderNo, LocalDateTime refundTime) {
        if (refundTime == null) {
            refundTime = LocalDateTime.now();
        }
        memberRechargeOrderDao.editRefund(merchantNo, orderNo, refundTime);
    }

    public List<MemberRechargeOrderEntity> summaryList(MemberRechargeOrderSummaryQuery query) {
        if (query.getEndDate() != null) {
            query.setEndDate(query.getEndDate().plusDays(1));
        }
        query.setRefunded(0); // 未退款
        return memberRechargeOrderDao.summaryList(query);
    }
}
