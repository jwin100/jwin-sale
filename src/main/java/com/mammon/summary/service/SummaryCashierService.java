package com.mammon.summary.service;

import com.mammon.cashier.service.CashierOrderService;
import com.mammon.summary.dao.SummaryCashierDao;
import com.mammon.summary.domain.dto.SummaryCashierDto;
import com.mammon.cashier.domain.entity.CashierOrderEntity;
import com.mammon.cashier.domain.enums.CashierOrderCategory;
import com.mammon.cashier.domain.query.CashierOrderSummaryQuery;
import com.mammon.common.Generate;
import com.mammon.common.PageVo;
import com.mammon.merchant.domain.entity.MerchantEntity;
import com.mammon.merchant.domain.entity.MerchantStoreEntity;
import com.mammon.merchant.domain.query.MerchantQuery;
import com.mammon.merchant.service.MerchantService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.summary.domain.entity.SummaryCashierEntity;
import com.mammon.summary.domain.query.SummaryCashierQuery;
import com.mammon.summary.domain.vo.SummaryCashierDashVo;
import com.mammon.summary.domain.vo.SummaryCashierVo;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/2/29 10:16
 */
@Service
@Slf4j
public class SummaryCashierService {

    @Resource
    private SummaryCashierDao summaryCashierDao;

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
        CashierOrderSummaryQuery query = new CashierOrderSummaryQuery();
        query.setStartDate(yesterday);
        query.setEndDate(yesterday);
        query.setCategory(CashierOrderCategory.GOODS.getCode());

        LocalDate finalYesterday = yesterday;
        merchants.forEach(merchant -> {
            List<MerchantStoreEntity> stores = merchantStoreService.findAllByMerchantNo(merchant.getMerchantNo());
            stores.forEach(store -> {
                query.setMerchantNo(store.getMerchantNo());
                query.setStoreNo(store.getStoreNo());

                List<CashierOrderEntity> list = cashierOrderService.summaryList(query);

                int cashierTotal = list.size();
                long cashierAmount = list.stream().mapToLong(x -> x.getRealityAmount() - x.getRefundAmount()).sum();

                SummaryCashierDto dto = new SummaryCashierDto();
                dto.setSummaryDate(finalYesterday);
                dto.setCashierTotal(cashierTotal);
                dto.setCashierAmount(cashierAmount);

                SummaryCashierEntity cashierSummary = summaryCashierDao.findByDate(merchant.getMerchantNo(), store.getStoreNo(), finalYesterday);
                if (cashierSummary == null) {
                    save(merchant.getMerchantNo(), store.getStoreNo(), dto);
                } else {
                    edit(cashierSummary.getId(), dto);
                }
            });
        });
    }

    public void save(long merchantNo, long storeNo, SummaryCashierDto dto) {
        SummaryCashierEntity entity = new SummaryCashierEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setSummaryDate(dto.getSummaryDate());
        entity.setCashierTotal(dto.getCashierTotal());
        entity.setCashierAmount(dto.getCashierAmount());
        entity.setCreateTime(LocalDateTime.now());
        summaryCashierDao.save(entity);
    }

    public void edit(String id, SummaryCashierDto dto) {
        SummaryCashierEntity entity = new SummaryCashierEntity();
        entity.setId(id);
        entity.setCashierTotal(dto.getCashierTotal());
        entity.setCashierAmount(dto.getCashierAmount());
        summaryCashierDao.update(entity);
    }

    public SummaryCashierDashVo dash(long merchantNo, long storeNo, String accountId, SummaryCashierQuery query) {
        List<SummaryCashierEntity> summaryStores = findListByData(merchantNo, query.getStoreNo(), query.getStartDate(), query.getEndDate());
        int cashierTotal = summaryStores.stream().mapToInt(SummaryCashierEntity::getCashierTotal).sum();
        long cashierAmount = summaryStores.stream().mapToLong(SummaryCashierEntity::getCashierAmount).sum();
        long refundAmount = summaryStores.stream().mapToLong(SummaryCashierEntity::getRefundAmount).sum();

        BigDecimal unitAmount = BigDecimal.ZERO;
        if (cashierTotal > 0) {
            unitAmount = AmountUtil.parseBigDecimal(cashierAmount).divide(BigDecimal.valueOf(cashierAmount), 2, RoundingMode.HALF_UP);
        }

        SummaryCashierDashVo vo = new SummaryCashierDashVo();
        vo.setCashierTotal(cashierTotal);
        vo.setCashierAmount(AmountUtil.parseBigDecimal(cashierAmount));
        vo.setRefundAmount(AmountUtil.parseBigDecimal(refundAmount));
        vo.setUnitAmount(unitAmount);
        return vo;
    }

    public List<SummaryCashierVo> list(long merchantNo, long storeNo, String accountId, SummaryCashierQuery query) {
        List<SummaryCashierEntity> summaryStores = findListByData(merchantNo, query.getStoreNo(), query.getStartDate(), query.getEndDate());
        List<SummaryCashierVo> list = new ArrayList<>();
        LocalDate endDate = query.getEndDate().plusDays(1);
        while (query.getStartDate().isBefore(endDate)) {
            List<SummaryCashierEntity> finalSummaryStores = summaryStores.stream()
                    .filter(x -> x.getSummaryDate().equals(query.getStartDate()))
                    .collect(Collectors.toList());
            int cashierTotal = finalSummaryStores.stream().mapToInt(SummaryCashierEntity::getCashierTotal).sum();
            long cashierAmount = finalSummaryStores.stream().mapToLong(SummaryCashierEntity::getCashierAmount).sum();
            long refundAmount = finalSummaryStores.stream().mapToLong(SummaryCashierEntity::getRefundAmount).sum();
            SummaryCashierVo vo = new SummaryCashierVo();
            vo.setSummaryDate(query.getStartDate());
            vo.setCashierTotal(cashierTotal);
            vo.setCashierAmount(AmountUtil.parseBigDecimal(cashierAmount));
            vo.setRefundAmount(AmountUtil.parseBigDecimal(refundAmount));
            list.add(vo);
            query.setStartDate(query.getStartDate().plusDays(1));
        }
        return list;
    }

    public List<SummaryCashierEntity> findListByData(long merchantNo, Long storeNo,
                                                     LocalDate summaryStartDate, LocalDate summaryEndDate) {
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
        return summaryCashierDao.findListByDate(merchantNo, storeNo, summaryStartDate, summaryEndDate);
    }
}
