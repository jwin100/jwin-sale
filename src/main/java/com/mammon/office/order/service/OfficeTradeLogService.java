package com.mammon.office.order.service;

import com.mammon.common.Generate;
import com.mammon.office.order.dao.OfficeTradeLogDao;
import com.mammon.office.order.domain.entity.OfficeTradeLogEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-03-02 16:00:48
 */
@Service
public class OfficeTradeLogService {

    @Resource
    private OfficeTradeLogDao officeTradeLogDao;

    public void save(String tradeNo, String tradeType,
                     String params, String result, String remark) {
        OfficeTradeLogEntity entity = new OfficeTradeLogEntity();
        entity.setId(Generate.generateUUID());
        entity.setTradeNo(tradeNo);
        entity.setTradeType(tradeType);
        entity.setPayParams(params);
        entity.setPayResult(result);
        entity.setRemark(remark);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        officeTradeLogDao.save(entity);
    }
}
