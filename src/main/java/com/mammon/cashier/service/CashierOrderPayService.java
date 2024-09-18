package com.mammon.cashier.service;

import com.mammon.cashier.dao.CashierOrderPayDao;
import com.mammon.cashier.domain.dto.CashierOrderPayDto;
import com.mammon.cashier.domain.entity.CashierOrderPayEntity;
import com.mammon.cashier.domain.enums.CashierOrderPayStatus;
import com.mammon.cashier.domain.vo.CashierOrderPayResultVo;
import com.mammon.cashier.domain.vo.CashierOrderVo;
import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.utils.AmountUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CashierOrderPayService {

    @Resource
    private CashierOrderService cashierOrderService;

    @Resource
    private CashierOrderPayDao cashierOrderPayDao;

    /**
     * 获取订单支付结果
     *
     * @param id
     * @return
     */
    public CashierOrderPayResultVo getPayResult(String id) {
        CashierOrderVo order = cashierOrderService.findById(id);
        if (order == null) {
            throw new CustomException("订单信息错误");
        }
        List<CashierOrderPayEntity> orderPays = findAllByOrderId(order.getId());
        long payableAmount = AmountUtil.parse(order.getPayableAmount());
        long realityAmount = orderPays.stream()
                .filter(x -> x.getStatus() == CashierOrderPayStatus.PAY_SUCCESS.getCode())
                .mapToLong(CashierOrderPayEntity::getPayableAmount).sum();
        long waitAmount = payableAmount - realityAmount;
        CashierOrderPayResultVo vo = new CashierOrderPayResultVo();
        vo.setOrderId(id);
        vo.setPayableAmount(order.getPayableAmount());
        vo.setReceivedAmount(AmountUtil.parseBigDecimal(realityAmount));
        vo.setWaitAmount(AmountUtil.parseBigDecimal(waitAmount));
        return vo;
    }

    public CashierOrderPayEntity save(CashierOrderPayDto dto) {
        CashierOrderPayEntity entity = new CashierOrderPayEntity();
        entity.setId(Generate.generateUUID());
        entity.setOrderId(dto.getId());
        entity.setPayCode(dto.getPayCode());
        entity.setPayableAmount(AmountUtil.parse(dto.getPayAmount()));
        entity.setAuthCode(dto.getAuthCode());
        entity.setCountedId(dto.getCountedId());
        entity.setCountedTotal(dto.getCountedTotal());
        entity.setCreateTime(LocalDateTime.now());
        entity.setStatus(CashierOrderPayStatus.PAYING.getCode());
        cashierOrderPayDao.save(entity);
        return entity;
    }

    public void paySuccess(String payId, String tradeNo, long realityAmount) {
        updatePayStatus(payId, tradeNo,
                CashierOrderPayStatus.PAY_SUCCESS.getCode(), null, realityAmount);
    }

    public void payFailed(String payId, String tradeNo, String message) {
        updatePayStatus(payId, tradeNo,
                CashierOrderPayStatus.PAY_ERROR.getCode(), message, 0);
    }

    public void updatePayStatus(String id, String tradeNo, int payStatus, String message, long realityAmount) {
        cashierOrderPayDao.updatePayStatus(id, tradeNo, payStatus, message, realityAmount);
    }

    public List<CashierOrderPayEntity> findAllByIds(List<String> ids) {
        return cashierOrderPayDao.findAllByIds(ids);
    }

    public List<CashierOrderPayEntity> findAllByOrderIds(List<String> orderIds) {
        return cashierOrderPayDao.findAllByOrderIds(orderIds);
    }

    public List<CashierOrderPayEntity> findAllByOrderId(String orderId) {
        return cashierOrderPayDao.findAllByOrderId(orderId);
    }
}
