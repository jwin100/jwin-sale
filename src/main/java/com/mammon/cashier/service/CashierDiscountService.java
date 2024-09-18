package com.mammon.cashier.service;

import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.cashier.dao.CashierDiscountDao;
import com.mammon.cashier.domain.dto.CashierDiscountDto;
import com.mammon.cashier.domain.entity.CashierDiscountEntity;
import com.mammon.cashier.domain.vo.CashierDiscountVo;
import com.mammon.utils.QuantityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashierDiscountService {

    @Resource
    private CashierDiscountDao cashierDiscountDao;

    public String edit(long merchantNo, CashierDiscountDto dto) {
        long longDiscount = QuantityUtil.parse(dto.getDiscount());

        List<CashierDiscountEntity> discounts = findAllByMerchantNo(merchantNo);

        if (discounts.stream().anyMatch(x -> x.getDiscount() == longDiscount
                && (StringUtils.isBlank(dto.getId()) || !x.getId().equals(dto.getId())))) {
            throw new CustomException("当前设置折扣已存在");
        }

        CashierDiscountEntity discountEntity = discounts.stream().filter(x -> x.getId().equals(dto.getId())).findFirst().orElse(null);
        CashierDiscountEntity entity = new CashierDiscountEntity();
        entity.setDiscount(longDiscount);
        entity.setUpdateTime(LocalDateTime.now());

        if (discountEntity == null) {
            entity.setId(Generate.generateUUID());
            entity.setMerchantNo(merchantNo);
            entity.setCreateTime(LocalDateTime.now());
            cashierDiscountDao.save(entity);
            return entity.getId();
        }
        entity.setId(discountEntity.getId());
        cashierDiscountDao.edit(entity);
        return entity.getId();
    }

    public void delete(String id) {
        cashierDiscountDao.delete(id);
    }

    public List<CashierDiscountVo> list(long merchantNo) {
        List<CashierDiscountEntity> discounts = findAllByMerchantNo(merchantNo);
        if (CollectionUtils.isEmpty(discounts)) {
            return Collections.emptyList();
        }
        return discounts.stream().map(x -> {
            CashierDiscountVo vo = new CashierDiscountVo();
            BeanUtils.copyProperties(x, vo);
            vo.setDiscount(QuantityUtil.parseBigDecimal(x.getDiscount()));
            vo.setDiscountName(vo.getDiscount().stripTrailingZeros().toPlainString() + "折");
            return vo;
        }).collect(Collectors.toList());
    }

    public List<CashierDiscountEntity> findAllByMerchantNo(long merchantNo) {
        return cashierDiscountDao.findAllByMerchantNo(merchantNo);
    }
}