package com.mammon.cashier.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.mammon.common.Generate;
import com.mammon.cashier.dao.CashierFlowDao;
import com.mammon.cashier.domain.dto.CashierFlowDto;
import com.mammon.cashier.domain.entity.CashierFlowEntity;
import com.mammon.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CashierFlowService {

    private static final String CASHIER_CUSTOMER_NO = "CASHIER_CUSTOMER_NO_%s";

    @Resource
    private CashierFlowDao cashierFlowDao;

    @Resource
    private RedisService redisService;

    public void edit(long merchantNo, CashierFlowDto dto) {
        CashierFlowEntity entity = findByMerchantNo(merchantNo);
        if (entity == null) {
            entity = new CashierFlowEntity();
            BeanUtils.copyProperties(dto, entity);

            entity.setId(Generate.generateUUID());
            entity.setMerchantNo(merchantNo);
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            cashierFlowDao.save(entity);
        } else {
            entity.setCustomFlowModel(dto.getCustomFlowModel());
            entity.setRatingRestFlow(dto.getRatingRestFlow());
            entity.setInitFlow(dto.getInitFlow());
            cashierFlowDao.edit(entity);
        }

        String key = String.format(CASHIER_CUSTOMER_NO, merchantNo);
        if (redisService.exists(key)) {
            redisService.delete(key);
        }
        // 如果是自增流水单号
        if (dto.getCustomFlowModel() == 0) {
            long expire = -1;
            if (dto.getRatingRestFlow() == 1) {
                // 设置过期时长为1天
                expire = diffBetween(getNextStartDay());
            } else if (dto.getRatingRestFlow() == 2) {
                // 设置过期时长为每月1号前
                expire = diffBetween(getNextStartMonth());
            } else if (dto.getRatingRestFlow() == 3) {
                // 设置过期时长为每季度第一天前
                expire = diffBetween(getNextStartQuarter());
            }
            log.error("increment过期时长为,{}", expire);
            if (expire < 0) {
                // 一直不重置
                redisService.set(key, String.valueOf(dto.getInitFlow()));
            } else {
                redisService.set(key, String.valueOf(dto.getInitFlow()), expire, TimeUnit.SECONDS);
            }
        }
    }

    public CashierFlowEntity info(long merchantNo) {
        CashierFlowEntity entity = findByMerchantNo(merchantNo);
        if (entity != null) {
            return entity;
        }
        entity = new CashierFlowEntity();
        entity.setMerchantNo(merchantNo);
        entity.setCustomFlowModel(1);
        entity.setRatingRestFlow(0);
        entity.setInitFlow(1);
        return entity;
    }

    public CashierFlowEntity findByMerchantNo(long merchantNo) {
        return cashierFlowDao.findByMerchantNo(merchantNo);
    }

    public String getCustomerNo(long merchantNo) {
        CashierFlowEntity entity = findByMerchantNo(merchantNo);
        if (entity == null || entity.getCustomFlowModel() != 0) {
            return null;
        }
        String key = String.format(CASHIER_CUSTOMER_NO, merchantNo);
        if (redisService.exists(key)) {
            return String.valueOf(redisService.increment(key, 1));
        }

        long expire = -1;
        if (entity.getRatingRestFlow() == 1) {
            expire = diffBetween(getNextStartDay());
        } else if (entity.getRatingRestFlow() == 2) {
            expire = diffBetween(getNextStartMonth());
        } else if (entity.getRatingRestFlow() == 3) {
            expire = diffBetween(getNextStartQuarter());
        }
        log.error("increment过期时长为,{}", expire);
        if (expire < 0) {
            redisService.set(key, String.valueOf(entity.getInitFlow()));
        } else {
            redisService.set(key, String.valueOf(entity.getInitFlow()), expire, TimeUnit.SECONDS);
        }
        return String.valueOf(redisService.increment(key, 1));
    }

    private long diffBetween(LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration between = LocalDateTimeUtil.between(now, endTime);
        return between.getSeconds();
    }

    private LocalDateTime getNextStartDay() {
        LocalDate now = LocalDate.now();
        return now.plusDays(1).atStartOfDay();
    }

    /**
     * 获取下一个月第一天
     *
     * @return
     */
    private LocalDateTime getNextStartMonth() {
        LocalDate now = LocalDate.now().plusMonths(1);
        LocalDate currentStartDate = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
        return currentStartDate.atStartOfDay();
    }

    /**
     * 获取下一个季度第一天
     *
     * @return
     */
    private LocalDateTime getNextStartQuarter() {
        //获取当前月后的第三个月
        LocalDate now = LocalDate.now().plusMonths(3);
        // 获取月份
        int month = now.getMonthValue();
        int currentQuarterMonth = 1;
        if (month >= 4 && month < 7) {
            currentQuarterMonth = 4;
        } else if (month < 10) {
            currentQuarterMonth = 7;
        } else {
            currentQuarterMonth = 10;
        }
        // 获取now所在季度
        LocalDate currentQuarter = LocalDate.of(now.getYear(), currentQuarterMonth, 1);
        // 获取now所在季度的第一天
        return currentQuarter.atStartOfDay();
    }
}
