package com.mammon.cashier.service;

import com.mammon.cashier.dao.CashierOrderProductDao;
import com.mammon.cashier.domain.dto.CashierOrderProductDto;
import com.mammon.cashier.domain.entity.CashierOrderProductEntity;
import com.mammon.cashier.domain.enums.OrderProductStatusConst;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.StockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CashierOrderProductService {

    @Resource
    private CashierOrderProductDao cashierOrderProductDao;

    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<CashierOrderProductDto> list) {
        list.forEach(this::save);
    }

    public void save(CashierOrderProductDto dto) {
        CashierOrderProductEntity entity = new CashierOrderProductEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setSaleQuantity(StockUtil.parse(dto.getSaleQuantity()));
        entity.setReferenceAmount(AmountUtil.parse(dto.getReferenceAmount()));
        entity.setAdjustAmount(AmountUtil.parse(dto.getAdjustAmount()));
        entity.setPayableAmount(AmountUtil.parse(dto.getPayableAmount()));
        entity.setDivideAmount(AmountUtil.parse(dto.getDivideAmount()));
        entity.setStatus(OrderProductStatusConst.销售);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        cashierOrderProductDao.save(entity);
    }

    public void updateRefund(String id, long refundQuantity, long refundAmount) {
        cashierOrderProductDao.updateRefund(id, refundQuantity, refundAmount);
    }

    public List<CashierOrderProductEntity> findAllByOrderId(String orderId) {
        return cashierOrderProductDao.findAllByOrderId(orderId);
    }

    public List<CashierOrderProductEntity> findAllByOrderIds(List<String> orderIds) {
        return cashierOrderProductDao.findAllByOrderIds(orderIds);
    }
}
