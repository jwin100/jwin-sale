package com.mammon.cashier.service;

import com.mammon.cashier.domain.enums.CashierIgnoreType;
import com.mammon.cashier.domain.vo.CashierIgnoreSetVo;
import com.mammon.cashier.domain.vo.CashierIgnoreVo;
import com.mammon.common.Generate;
import com.mammon.cashier.dao.CashierIgnoreDao;
import com.mammon.cashier.domain.entity.CashierIgnoreEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashierIgnoreService {

    @Resource
    private CashierIgnoreDao cashierIgnoreDao;

    public void edit(long merchantNo, int type) {
        CashierIgnoreEntity entity = new CashierIgnoreEntity();
        entity.setType(type);
        entity.setUpdateTime(LocalDateTime.now());

        CashierIgnoreEntity ignore = findByMerchantNo(merchantNo);
        if (ignore == null) {
            entity.setId(Generate.generateUUID());
            entity.setMerchantNo(merchantNo);
            entity.setCreateTime(LocalDateTime.now());
            cashierIgnoreDao.save(entity);
            return;
        }
        entity.setId(ignore.getId());
        cashierIgnoreDao.edit(entity);
    }

    public List<CashierIgnoreSetVo> getSetList() {
        return Arrays.stream(CashierIgnoreType.values()).map(x -> {
            CashierIgnoreSetVo vo = new CashierIgnoreSetVo();
            vo.setValue(x.getCode());
            vo.setLabel(x.getName());
            return vo;
        }).collect(Collectors.toList());
    }

    public CashierIgnoreVo info(long merchantNo) {
        CashierIgnoreEntity entity = findByMerchantNo(merchantNo);
        CashierIgnoreVo vo = new CashierIgnoreVo();
        if (entity != null) {
            vo.setType(entity.getType());
            return vo;
        }
        vo.setMerchantNo(merchantNo);
        vo.setType(CashierIgnoreType.NOT_IGNORE.getCode());
        return vo;
    }

    public CashierIgnoreEntity findByMerchantNo(long merchantNo) {
        return cashierIgnoreDao.findByMerchantNo(merchantNo);
    }
}
