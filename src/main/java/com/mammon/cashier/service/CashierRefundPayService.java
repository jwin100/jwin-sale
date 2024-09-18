package com.mammon.cashier.service;

import com.mammon.cashier.domain.entity.*;
import com.mammon.cashier.domain.enums.*;
import com.mammon.cashier.domain.model.*;
import com.mammon.common.Generate;
import com.mammon.cashier.dao.CashierRefundPayDao;
import com.mammon.utils.AmountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CashierRefundPayService {

    @Resource
    private CashierRefundPayDao cashierRefundPayDao;

    public List<CashierRefundPayEntity> save(String refundId, List<CashierRefundPayTypeModel> payTypeModels) {
        return payTypeModels.stream().map(x -> {
            CashierRefundPayEntity entity = new CashierRefundPayEntity();
            entity.setId(Generate.generateUUID());
            entity.setRefundId(refundId);
            entity.setPayCode(x.getPayCode());
            entity.setPayableAmount(AmountUtil.parse(x.getRefundAmount()));
            entity.setRealityAmount(AmountUtil.parse(x.getRefundAmount()));
            entity.setCountedId(x.getCountedId());
            entity.setCountedTotal(x.getCountedTotal());
            entity.setCreateTime(LocalDateTime.now());
            entity.setStatus(CashierRefundPayStatus.REFUND_ING.getCode());
            cashierRefundPayDao.save(entity);
            return entity;
        }).collect(Collectors.toList());
    }

    public void updatePayStatus(String id, String tradeNo, int status, String message) {
        cashierRefundPayDao.updatePayStatus(id, tradeNo, status, message);
    }

    public void paySubmit(String id, String tradeNo) {
        cashierRefundPayDao.updatePayStatus(id, tradeNo, CashierRefundPayStatus.REFUND_ING.getCode(), null);
    }

    public void payFinish(String id, String tradeNo) {
        cashierRefundPayDao.updatePayStatus(id, tradeNo, CashierRefundPayStatus.REFUND_FINISH.getCode(), null);
    }

    public void payError(String id, String tradeNo, String message) {
        cashierRefundPayDao.updatePayStatus(id, tradeNo, CashierRefundPayStatus.REFUND_ERROR.getCode(), message);
    }

    public List<CashierRefundPayEntity> findAllByRefundIds(List<String> refundIds) {
        return cashierRefundPayDao.findAllByRefundIds(refundIds);
    }

    public List<CashierRefundPayEntity> findAllByRefundId(String refundId) {
        return cashierRefundPayDao.findAllByRefundId(refundId);
    }
}
