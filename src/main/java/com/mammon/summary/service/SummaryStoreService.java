package com.mammon.summary.service;

import com.mammon.cashier.domain.entity.CashierOrderEntity;
import com.mammon.cashier.domain.enums.CashierOrderCategory;
import com.mammon.cashier.domain.query.CashierOrderSummaryQuery;
import com.mammon.cashier.service.CashierOrderService;
import com.mammon.common.Generate;
import com.mammon.common.PageVo;
import com.mammon.merchant.domain.entity.MerchantEntity;
import com.mammon.merchant.domain.entity.MerchantStoreEntity;
import com.mammon.merchant.domain.query.MerchantQuery;
import com.mammon.merchant.service.MerchantService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.summary.dao.SummaryStoreDao;
import com.mammon.summary.domain.dto.SummaryStoreDto;
import com.mammon.summary.domain.entity.SummaryStoreEntity;
import com.mammon.summary.domain.query.SummaryStoreQuery;
import com.mammon.summary.domain.vo.SummaryStoreDashVo;
import com.mammon.summary.domain.vo.SummaryStoreVo;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/4/22 14:57
 */
@Service
@Slf4j
public class SummaryStoreService {

    @Resource
    private SummaryStoreDao summaryStoreDao;

    @Resource
    private CashierOrderService cashierOrderService;

    @Resource
    private MerchantService merchantService;

    @Resource
    private MerchantStoreService merchantStoreService;

    public void yesterdaySummary(LocalDate yesterday) {
        MerchantQuery query = new MerchantQuery();
        query.setPageIndex(1);
        query.setPageSize(200);
        merchantPage(yesterday, query);
    }

    private void merchantPage(LocalDate yesterday, MerchantQuery query) {
        PageVo<MerchantEntity> merchantPage = merchantService.page(query);
        if (merchantPage == null || CollectionUtils.isEmpty(merchantPage.getData())) {
            return;
        }
        log.info("merchantPage:{}", JsonUtil.toJSONString(merchantPage));
        summary(yesterday, merchantPage.getData());
        if (query.getPageIndex() < merchantPage.getRows()) {
            query.setPageIndex(query.getPageIndex() + 1);
            merchantPage(yesterday, query);
        }
    }

    private void summary(LocalDate yesterday, List<MerchantEntity> merchants) {
        // 根据当前日期，统计前一天的数据
        if (yesterday == null) {
            yesterday = LocalDate.now().minusDays(1);
        }
        // 店员业绩的时候不传门店编号参数，因为店员会出现切换门店的情况
        LocalDate finalYesterday = yesterday;
        merchants.forEach(merchant -> {
            List<MerchantStoreEntity> stores = merchantStoreService.findAllByMerchantNo(merchant.getMerchantNo());
            stores.forEach(store -> {
                // 统计销售金额
                // 统计储值金额
                // 统计开卡金额
                // 统计服务金额
                CashierOrderSummaryQuery query = new CashierOrderSummaryQuery();
                query.setMerchantNo(merchant.getMerchantNo());
                query.setStoreNo(store.getStoreNo());
                query.setStartDate(finalYesterday);
                query.setEndDate(finalYesterday);
                List<CashierOrderEntity> list = cashierOrderService.summaryList(query);

                List<CashierOrderEntity> cashierList = list.stream()
                        .filter(x -> x.getCategory() == CashierOrderCategory.GOODS.getCode())
                        .collect(Collectors.toList());
                List<CashierOrderEntity> rechargeList = list.stream()
                        .filter(x -> x.getCategory() == CashierOrderCategory.RECHARGE.getCode())
                        .collect(Collectors.toList());
                List<CashierOrderEntity> countedList = list.stream()
                        .filter(x -> x.getCategory() == CashierOrderCategory.COUNTED.getCode())
                        .collect(Collectors.toList());
                List<CashierOrderEntity> serviceList = list.stream()
                        .filter(x -> x.getCategory() == CashierOrderCategory.SERVICE.getCode())
                        .collect(Collectors.toList());

                long cashierAmount = cashierList.stream().mapToLong(x -> x.getRealityAmount() - x.getRefundAmount()).sum();
                long rechargeAmount = rechargeList.stream().mapToLong(x -> x.getRealityAmount() - x.getRefundAmount()).sum();
                long countedAmount = countedList.stream().mapToLong(x -> x.getRealityAmount() - x.getRefundAmount()).sum();
                long serviceAmount = serviceList.stream().mapToLong(x -> x.getRealityAmount() - x.getRefundAmount()).sum();

                SummaryStoreDto dto = new SummaryStoreDto();
                dto.setSummaryDate(finalYesterday);
                dto.setTotalAmount(cashierAmount + rechargeAmount + countedAmount + serviceAmount);
                dto.setCashierAmount(cashierAmount);
                dto.setRechargeAmount(rechargeAmount);
                dto.setCountedAmount(countedAmount);
                dto.setServiceAmount(serviceAmount);
                SummaryStoreEntity summaryStore = summaryStoreDao.findByDate(merchant.getMerchantNo(), store.getStoreNo(), finalYesterday);
                if (summaryStore == null) {
                    save(merchant.getMerchantNo(), store.getStoreNo(), dto);
                } else {
                    edit(summaryStore.getId(), dto);
                }
            });
        });
    }

    public void save(long merchantNo, long storeNo, SummaryStoreDto dto) {
        SummaryStoreEntity entity = new SummaryStoreEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        summaryStoreDao.save(entity);
    }

    public void edit(String id, SummaryStoreDto dto) {
        SummaryStoreEntity entity = new SummaryStoreEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUpdateTime(LocalDateTime.now());
        summaryStoreDao.update(entity);
    }

    public SummaryStoreDashVo dash(long merchantNo, long storeNo, String accountId, SummaryStoreQuery query) {
        List<SummaryStoreEntity> summaryStores = findListByData(merchantNo, query.getStoreNo(), query.getStartDate(), query.getEndDate());
        long totalAmount = summaryStores.stream().mapToLong(SummaryStoreEntity::getTotalAmount).sum();
        long cashierAmount = summaryStores.stream().mapToLong(SummaryStoreEntity::getCashierAmount).sum();
        long rechargeAmount = summaryStores.stream().mapToLong(SummaryStoreEntity::getRechargeAmount).sum();
        long countedAmount = summaryStores.stream().mapToLong(SummaryStoreEntity::getCountedAmount).sum();
        long serviceAmount = summaryStores.stream().mapToLong(SummaryStoreEntity::getServiceAmount).sum();
        SummaryStoreDashVo vo = new SummaryStoreDashVo();
        vo.setTotalAmount(AmountUtil.parseBigDecimal(totalAmount));
        vo.setCashierAmount(AmountUtil.parseBigDecimal(cashierAmount));
        vo.setRechargeAmount(AmountUtil.parseBigDecimal(rechargeAmount));
        vo.setCountedAmount(AmountUtil.parseBigDecimal(countedAmount));
        vo.setServiceAmount(AmountUtil.parseBigDecimal(serviceAmount));
        return vo;
    }

    public List<SummaryStoreVo> list(long merchantNo, long storeNo, String accountId, SummaryStoreQuery query) {
        List<SummaryStoreEntity> summaryStores = findListByData(merchantNo, query.getStoreNo(), query.getStartDate(), query.getEndDate());
        List<SummaryStoreVo> list = new ArrayList<>();
        LocalDate endDate = query.getEndDate().plusDays(1);
        while (query.getStartDate().isBefore(endDate)) {
            List<SummaryStoreEntity> finalSummaryStores = summaryStores.stream()
                    .filter(x -> x.getSummaryDate().equals(query.getStartDate()))
                    .collect(Collectors.toList());
            long totalAmount = finalSummaryStores.stream().mapToLong(SummaryStoreEntity::getTotalAmount).sum();
            long cashierAmount = finalSummaryStores.stream().mapToLong(SummaryStoreEntity::getCashierAmount).sum();
            long rechargeAmount = finalSummaryStores.stream().mapToLong(SummaryStoreEntity::getRechargeAmount).sum();
            long countedAmount = finalSummaryStores.stream().mapToLong(SummaryStoreEntity::getCountedAmount).sum();
            long serviceAmount = finalSummaryStores.stream().mapToLong(SummaryStoreEntity::getServiceAmount).sum();
            SummaryStoreVo vo = new SummaryStoreVo();
            vo.setSummaryDate(query.getStartDate());
            vo.setTotalAmount(AmountUtil.parseBigDecimal(totalAmount));
            vo.setCashierAmount(AmountUtil.parseBigDecimal(cashierAmount));
            vo.setRechargeAmount(AmountUtil.parseBigDecimal(rechargeAmount));
            vo.setCountedAmount(AmountUtil.parseBigDecimal(countedAmount));
            vo.setServiceAmount(AmountUtil.parseBigDecimal(serviceAmount));
            list.add(vo);

            query.setStartDate(query.getStartDate().plusDays(1));
        }
        return list;
    }

    public List<SummaryStoreEntity> findListByData(long merchantNo, Long storeNo, LocalDate summaryStartDate, LocalDate summaryEndDate) {
        if (storeNo == null) {
            return Collections.emptyList();
        }
        if (summaryStartDate == null) {
            summaryStartDate = LocalDate.now();
        }
        if (summaryEndDate == null) {
            summaryEndDate = LocalDate.now();
        }
        summaryEndDate = summaryEndDate.plusDays(1);
        return summaryStoreDao.findListByDate(merchantNo, storeNo, summaryStartDate, summaryEndDate);
    }
}
