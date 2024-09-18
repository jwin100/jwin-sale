package com.mammon.summary.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.AsyncUtil;
import com.mammon.cashier.domain.entity.CashierOrderEntity;
import com.mammon.cashier.domain.enums.CashierOrderCategory;
import com.mammon.cashier.domain.query.CashierOrderSummaryQuery;
import com.mammon.cashier.service.CashierOrderService;
import com.mammon.clerk.domain.query.AccountQuery;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.clerk.service.AccountService;
import com.mammon.clerk.service.CommissionService;
import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.merchant.domain.entity.MerchantEntity;
import com.mammon.merchant.domain.query.MerchantQuery;
import com.mammon.merchant.service.MerchantService;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.summary.dao.SummaryAccountDao;
import com.mammon.summary.domain.dto.SummaryAccountDto;
import com.mammon.summary.domain.entity.SummaryAccountEntity;
import com.mammon.summary.domain.query.SummaryAccountQuery;
import com.mammon.summary.domain.query.SummaryAccountSelfQuery;
import com.mammon.summary.domain.vo.SummaryAccountConvertVo;
import com.mammon.summary.domain.vo.SummaryAccountDashVo;
import com.mammon.summary.domain.vo.SummaryAccountVo;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/4/22 14:57
 */
@Service
@Slf4j
public class SummaryAccountService {

    @Resource
    private SummaryAccountDao summaryAccountDao;

    @Resource
    private CashierOrderService cashierOrderService;

    @Resource
    private MerchantService merchantService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private AccountService accountService;

    @Resource
    private CommissionService commissionService;

    /**
     * 统计指定日期的数据
     *
     * @param yesterday
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void yesterdaySummary(LocalDate yesterday) {
        MerchantQuery query = new MerchantQuery();
        query.setPageIndex(1);
        query.setPageSize(200);
        merchantPage(yesterday, query);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                commissionService.summary(null);
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void merchantPage(LocalDate yesterday, MerchantQuery query) {
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

    @Transactional(rollbackFor = Exception.class)
    public void summary(LocalDate yesterday, List<MerchantEntity> merchants) {
        // 根据当前日期，统计前一天的数据
        if (yesterday == null) {
            yesterday = LocalDate.now().minusDays(1);
        }
        // 店员业绩的时候不传门店编号参数，因为店员会出现切换门店的情况
        LocalDate finalYesterday = yesterday;
        merchants.forEach(merchant -> {
            List<UserVo> users = accountService.findAll(merchant.getMerchantNo(), null);
            users.forEach(user -> {
                SummaryAccountDto dto = new SummaryAccountDto();
                dto.setSummaryDate(finalYesterday);
                summaryOrder(merchant.getMerchantNo(), user.getId(), finalYesterday, finalYesterday, dto);
                SummaryAccountEntity summaryAccount = summaryAccountDao.findByDate(merchant.getMerchantNo(), user.getId(), finalYesterday);
                if (summaryAccount == null) {
                    save(merchant.getMerchantNo(), user.getStoreNo(), dto);
                } else {
                    edit(summaryAccount.getId(), dto);
                }
            });
        });
    }

    private void summaryOrder(long merchantNo, String accountId, LocalDate startDate, LocalDate endDate,
                              SummaryAccountDto dto) {
        CashierOrderSummaryQuery query = new CashierOrderSummaryQuery();
        query.setMerchantNo(merchantNo);
        query.setStartDate(startDate);
        query.setEndDate(endDate);
        query.setServiceAccountId(accountId);
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

        long cashierAmount = cashierList.stream().mapToLong(x -> convert(x.getServiceAccountIds(), x.getRealityAmount(), x.getRefundAmount())).sum();
        long rechargeAmount = rechargeList.stream().mapToLong(x -> convert(x.getServiceAccountIds(), x.getRealityAmount(), x.getRefundAmount())).sum();
        long countedAmount = countedList.stream().mapToLong(x -> convert(x.getServiceAccountIds(), x.getRealityAmount(), x.getRefundAmount())).sum();
        long serviceAmount = serviceList.stream().mapToLong(x -> convert(x.getServiceAccountIds(), x.getRealityAmount(), x.getRefundAmount())).sum();
        dto.setAccountId(accountId);
        dto.setTotalAmount(cashierAmount + rechargeAmount + countedAmount + serviceAmount);
        dto.setCashierAmount(cashierAmount);
        dto.setRechargeAmount(rechargeAmount);
        dto.setCountedAmount(countedAmount);
        dto.setServiceAmount(serviceAmount);
    }

    private long convert(String serviceAccountIds, long realityAmount, long refundAmount) {
        if (StringUtils.isNotBlank(serviceAccountIds)) {
            List<String> accountIds = JsonUtil.toList(serviceAccountIds, String.class);
            return (realityAmount - refundAmount) / accountIds.size();
        }
        return realityAmount - refundAmount;
    }

    public void save(long merchantNo, long storeNo, SummaryAccountDto dto) {
        SummaryAccountEntity entity = new SummaryAccountEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        summaryAccountDao.save(entity);
    }

    public void edit(String id, SummaryAccountDto dto) {
        SummaryAccountEntity entity = new SummaryAccountEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUpdateTime(LocalDateTime.now());
        summaryAccountDao.update(entity);
    }

    /**
     * 我的业绩
     * <p>
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    public SummaryAccountDashVo self(long merchantNo, long storeNo, String accountId, SummaryAccountSelfQuery query) {
        //TODO 我的业绩统计先实时，后面扩硬件就是实时同步到es,然后实时取es数据
        SummaryAccountDto dto = new SummaryAccountDto();
        summaryOrder(merchantNo, accountId, query.getStartDate(), query.getEndDate(), dto);

        SummaryAccountDashVo vo = new SummaryAccountDashVo();
        vo.setTotalAmount(AmountUtil.parseBigDecimal(dto.getTotalAmount()));
        vo.setCashierAmount(AmountUtil.parseBigDecimal(dto.getCashierAmount()));
        vo.setRechargeAmount(AmountUtil.parseBigDecimal(dto.getRechargeAmount()));
        vo.setCountedAmount(AmountUtil.parseBigDecimal(dto.getCountedAmount()));
        vo.setServiceAmount(AmountUtil.parseBigDecimal(dto.getServiceAmount()));
        return vo;
    }

    public SummaryAccountDashVo dash(long merchantNo, long storeNo, String accountId, SummaryAccountQuery query) {
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setLookMy(query.isLookMy());
        accountQuery.setStoreNo(query.getStoreNo());
        List<UserVo> accounts = accountService.list(merchantNo, storeNo, accountId, accountQuery);
        List<String> accountIds = accounts.stream().map(UserVo::getId).collect(Collectors.toList());
        List<SummaryAccountConvertVo> summaryAccounts = new ArrayList<>();
        if (Objects.equals(query.getStartDate(), LocalDate.now()) && Objects.equals(query.getEndDate(), LocalDate.now())) {
            // 今日统计
            summaryAccounts = findListByYesterday(merchantNo, accountIds);
        } else {
            // 非今日统计
            summaryAccounts = findListByData(accountIds, query.getStartDate(), query.getEndDate());
        }

        long totalAmount = summaryAccounts.stream().mapToLong(SummaryAccountConvertVo::getTotalAmount).sum();
        long cashierAmount = summaryAccounts.stream().mapToLong(SummaryAccountConvertVo::getCashierAmount).sum();
        long rechargeAmount = summaryAccounts.stream().mapToLong(SummaryAccountConvertVo::getRechargeAmount).sum();
        long countedAmount = summaryAccounts.stream().mapToLong(SummaryAccountConvertVo::getCountedAmount).sum();
        long serviceAmount = summaryAccounts.stream().mapToLong(SummaryAccountConvertVo::getServiceAmount).sum();

        SummaryAccountDashVo vo = new SummaryAccountDashVo();
        vo.setTotalAmount(AmountUtil.parseBigDecimal(totalAmount));
        vo.setCashierAmount(AmountUtil.parseBigDecimal(cashierAmount));
        vo.setRechargeAmount(AmountUtil.parseBigDecimal(rechargeAmount));
        vo.setCountedAmount(AmountUtil.parseBigDecimal(countedAmount));
        vo.setServiceAmount(AmountUtil.parseBigDecimal(serviceAmount));
        return vo;
    }


    public PageVo<SummaryAccountVo> page(long merchantNo, long storeNo, String accountId, SummaryAccountQuery query) {
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setLookMy(query.isLookMy());
        accountQuery.setStoreNo(query.getStoreNo());
        accountQuery.setPageIndex(query.getPageIndex());
        accountQuery.setPageSize(query.getPageSize());
        PageVo<UserVo> accountPage = accountService.page(merchantNo, storeNo, accountId, accountQuery);
        List<UserVo> accounts = accountPage.getData();
        List<String> accountIds = accounts.stream().map(UserVo::getId).collect(Collectors.toList());
        List<SummaryAccountConvertVo> summaryAccounts = new ArrayList<>();
        if (Objects.equals(query.getStartDate(), LocalDate.now()) && Objects.equals(query.getEndDate(), LocalDate.now())) {
            // 今日统计
            summaryAccounts = findListByYesterday(merchantNo, accountIds);
        } else {
            // 非今日统计
            summaryAccounts = findListByData(accountIds, query.getStartDate(), query.getEndDate());
        }
        List<SummaryAccountConvertVo> finalSummaryAccounts = summaryAccounts;
        List<SummaryAccountVo> list = accounts.stream().map(account -> {
            List<SummaryAccountConvertVo> tempSummaryAccounts = finalSummaryAccounts.stream()
                    .filter(x -> x.getAccountId().equals(account.getId()))
                    .collect(Collectors.toList());
            long totalAmount = tempSummaryAccounts.stream().mapToLong(SummaryAccountConvertVo::getTotalAmount).sum();
            long cashierAmount = tempSummaryAccounts.stream().mapToLong(SummaryAccountConvertVo::getCashierAmount).sum();
            long rechargeAmount = tempSummaryAccounts.stream().mapToLong(SummaryAccountConvertVo::getRechargeAmount).sum();
            long countedAmount = tempSummaryAccounts.stream().mapToLong(SummaryAccountConvertVo::getCountedAmount).sum();
            long serviceAmount = tempSummaryAccounts.stream().mapToLong(SummaryAccountConvertVo::getServiceAmount).sum();

            SummaryAccountVo vo = new SummaryAccountVo();
            vo.setMerchantNo(account.getMerchantNo());
            vo.setMerchantName(account.getMerchantName());
            vo.setStoreNo(account.getStoreNo());
            vo.setStoreName(account.getStoreName());
            vo.setAccountId(account.getId());
            vo.setAccountName(account.getName());
            vo.setTotalAmount(AmountUtil.parseBigDecimal(totalAmount));
            vo.setCashierAmount(AmountUtil.parseBigDecimal(cashierAmount));
            vo.setRechargeAmount(AmountUtil.parseBigDecimal(rechargeAmount));
            vo.setCountedAmount(AmountUtil.parseBigDecimal(countedAmount));
            vo.setServiceAmount(AmountUtil.parseBigDecimal(serviceAmount));
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), accountPage.getTotal(), list);
    }

    public List<SummaryAccountConvertVo> findListByData(List<String> accountIds,
                                                        LocalDate summaryStartDate, LocalDate summaryEndDate) {
        if (CollUtil.isEmpty(accountIds)) {
            return Collections.emptyList();
        }
        if (summaryStartDate == null) {
            summaryStartDate = LocalDate.now();
        }
        if (summaryEndDate == null) {
            summaryEndDate = LocalDate.now();
        }
        summaryEndDate = summaryEndDate.plusDays(1);
        List<SummaryAccountEntity> list = summaryAccountDao.findListByDate(accountIds, summaryStartDate, summaryEndDate);
        return list.stream().map(x -> {
            SummaryAccountConvertVo vo = new SummaryAccountConvertVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public List<SummaryAccountConvertVo> findListByYesterday(long merchantNo, List<String> accountIds) {
        if (CollUtil.isEmpty(accountIds)) {
            return Collections.emptyList();
        }
        LocalDate summaryStartDate = LocalDate.now();
        LocalDate summaryEndDate = LocalDate.now().plusDays(1);
        return accountIds.stream().map(accountId -> {
            SummaryAccountDto dto = new SummaryAccountDto();
            summaryOrder(merchantNo, accountId, summaryStartDate, summaryEndDate, dto);

            SummaryAccountConvertVo vo = new SummaryAccountConvertVo();
            vo.setAccountId(accountId);
            vo.setTotalAmount(dto.getTotalAmount());
            vo.setCashierAmount(dto.getCashierAmount());
            vo.setRechargeAmount(dto.getRechargeAmount());
            vo.setCountedAmount(dto.getCountedAmount());
            vo.setServiceAmount(dto.getServiceAmount());
            return vo;
        }).collect(Collectors.toList());
    }
}
