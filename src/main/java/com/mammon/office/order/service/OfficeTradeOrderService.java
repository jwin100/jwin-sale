package com.mammon.office.order.service;

import com.mammon.common.Generate;
import com.mammon.office.order.dao.OfficeTradeOrderDao;
import com.mammon.office.order.domain.dto.OfficeTradeOrderDto;
import com.mammon.office.order.domain.entity.OfficeTradeOrderEntity;
import com.mammon.office.order.domain.enums.OfficeTradeOrderStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-03-20 17:17:45
 */
@Service
public class OfficeTradeOrderService {

    @Resource
    private OfficeTradeOrderDao officeTradeOrderDao;

    public void paySave(OfficeTradeOrderDto dto) {
        OfficeTradeOrderEntity entity = officeTradeOrderDao.findByTradeNo(dto.getOutTradeNo(), dto.getTradeNo());
        if (entity == null) {
            entity = new OfficeTradeOrderEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setId(Generate.generateUUID());
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            officeTradeOrderDao.save(entity);
        } else {
            int originalStatus = OfficeTradeOrderStatus.waitPay.getCode();
            if (entity.getStatus() == OfficeTradeOrderStatus.payCancel.getCode()) {
                originalStatus = OfficeTradeOrderStatus.payCancel.getCode();
            }
            officeTradeOrderDao.editStatus(entity.getId(), originalStatus, dto.getStatus());
        }
    }

    public void refundSave(OfficeTradeOrderDto dto) {
        OfficeTradeOrderEntity entity = officeTradeOrderDao.findByTradeNo(dto.getOutTradeNo(), dto.getTradeNo());
        if (entity == null) {
            entity = new OfficeTradeOrderEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setId(Generate.generateUUID());
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            officeTradeOrderDao.save(entity);
        } else {
            int originalStatus = OfficeTradeOrderStatus.refunding.getCode();
            if (entity.getStatus() == OfficeTradeOrderStatus.refundCancel.getCode()) {
                originalStatus = OfficeTradeOrderStatus.refundCancel.getCode();
            }
            officeTradeOrderDao.editStatus(entity.getId(), originalStatus, dto.getStatus());
        }
    }
}
