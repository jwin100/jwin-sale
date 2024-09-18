package com.mammon.clerk.service;

import cn.hutool.core.collection.CollUtil;
import com.mammon.clerk.domain.enums.CommissionRuleType;
import com.mammon.clerk.domain.enums.CommissionRuleUnit;
import com.mammon.clerk.domain.query.AccountQuery;
import com.mammon.clerk.domain.query.CommissionListQuery;
import com.mammon.clerk.domain.query.CommissionRuleQuery;
import com.mammon.clerk.domain.vo.CommissionRuleVo;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.merchant.domain.entity.MerchantEntity;
import com.mammon.merchant.domain.query.MerchantQuery;
import com.mammon.merchant.service.MerchantService;
import com.mammon.clerk.dao.CommissionDao;
import com.mammon.clerk.domain.dto.CommissionSummaryDto;
import com.mammon.clerk.domain.entity.CommissionEntity;
import com.mammon.clerk.domain.query.CommissionQuery;
import com.mammon.clerk.domain.vo.CommissionVo;
import com.mammon.summary.domain.vo.SummaryAccountConvertVo;
import com.mammon.summary.service.SummaryAccountService;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/4/7 16:48
 */
@Slf4j
@Service
public class CommissionService {

    @Resource
    private CommissionDao commissionDao;

    @Resource
    private MerchantService merchantService;

    @Resource
    private SummaryAccountService summaryAccountService;

    @Resource
    private AccountService accountService;

    @Resource
    private CommissionRuleService commissionRuleService;

    /**
     * 手动重算提成
     *
     * @param dto
     */
    public void summaryReuse(long merchantNo, CommissionSummaryDto dto) {
        summary(merchantNo, dto);
    }

    /**
     * 定时任务统计调用
     *
     * @param dto
     */
    public void summary(CommissionSummaryDto dto) {
        if (dto == null) {
            dto = new CommissionSummaryDto();
            dto.setStartDate(LocalDate.now().minusDays(-2));
            dto.setEndDate(LocalDate.now().minusDays(-1));
        }
        MerchantQuery query = new MerchantQuery();
        query.setPageIndex(1);
        query.setPageSize(200);
        merchantPage(dto, query);
    }

    /**
     * 分页获取门店信息
     *
     * @param dto
     * @param query
     */
    private void merchantPage(CommissionSummaryDto dto, MerchantQuery query) {
        PageVo<MerchantEntity> merchantPage = merchantService.page(query);
        if (merchantPage == null || CollectionUtils.isEmpty(merchantPage.getData())) {
            return;
        }
        merchantPage.getData().forEach(x -> {
            summary(x.getMerchantNo(), dto);
        });
        if (query.getPageIndex() < merchantPage.getRows()) {
            query.setPageIndex(query.getPageIndex() + 1);
            merchantPage(dto, query);
        }
    }

    /**
     * 计算商户下所有店员订单
     *
     * @param merchantNo
     * @param dto
     */
    public void summary(long merchantNo, CommissionSummaryDto dto) {
        // 获取所有店员、或指定门店下店员
        // 获取商户下所有订单
        List<UserVo> accounts = accountService.findAllByStoreNo(merchantNo, dto.getStoreNo(), null);
        List<String> accountIds = accounts.stream().map(UserVo::getId).collect(Collectors.toList());
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate().plusDays(1);
        while (startDate.isBefore(endDate)) {
            List<SummaryAccountConvertVo> summaryVos = new ArrayList<>();
            if (Objects.equals(startDate, LocalDate.now())) {
                summaryVos = summaryAccountService.findListByYesterday(merchantNo, accountIds);
            } else {
                summaryVos = summaryAccountService.findListByData(accountIds, startDate, startDate);
            }
            summary(merchantNo, startDate, accounts, summaryVos);
            startDate = startDate.plusDays(1);
        }
    }

    /**
     * 计算店员提成
     */
    public void summary(long merchantNo, LocalDate commissionDate, List<UserVo> accounts,
                        List<SummaryAccountConvertVo> summaryVos) {
        if (CollUtil.isEmpty(summaryVos)) {
            return;
        }
        CommissionRuleQuery ruleQuery = new CommissionRuleQuery();
        ruleQuery.setStatus(CommonStatus.ENABLED.getCode());
        List<CommissionRuleVo> ruleVos = commissionRuleService.list(merchantNo, ruleQuery);
        List<CommissionEntity> list = summaryVos.stream().map(x -> {
            CommissionEntity entity = new CommissionEntity();
            entity.setMerchantNo(merchantNo);
            entity.setCommissionTime(commissionDate);
            entity.setAccountId(x.getAccountId());
            // setStoreNo
            accounts.stream().filter(account -> account.getId().equals(x.getAccountId()))
                    .findFirst()
                    .ifPresent(account -> entity.setStoreNo(account.getStoreNo()));

            ruleVos.stream().filter(rule -> rule.getType() == CommissionRuleType.CASHIER.getCode()).findFirst()
                    .ifPresent(rule -> {
                        if (x.getCashierAmount() > 0) {
                            long cashierAmount = (long) (rule.getRate() * 100);
                            if (rule.getUnit() == CommissionRuleUnit.PERCENTAGE.getCode()) {
                                cashierAmount = (long) (x.getCashierAmount() * (rule.getRate() / 100));
                            }
                            entity.setCashierAmount(cashierAmount);
                        }
                    });
            ruleVos.stream().filter(rule -> rule.getType() == CommissionRuleType.RECHARGE.getCode()).findFirst()
                    .ifPresent(rule -> {
                        if (x.getRechargeAmount() > 0) {
                            long rechargeAmount = (long) (rule.getRate() * 100);
                            if (rule.getUnit() == CommissionRuleUnit.PERCENTAGE.getCode()) {
                                rechargeAmount = (long) (x.getRechargeAmount() * rule.getRate() / 100);
                            }
                            entity.setRechargeAmount(rechargeAmount);
                        }
                    });
            ruleVos.stream().filter(rule -> rule.getType() == CommissionRuleType.COUNTED.getCode()).findFirst()
                    .ifPresent(rule -> {
                        if (x.getCountedAmount() > 0) {
                            long countedAmount = (long) (rule.getRate() * 100);
                            if (rule.getUnit() == CommissionRuleUnit.PERCENTAGE.getCode()) {
                                countedAmount = (long) (x.getCountedAmount() * rule.getRate() / 100);
                            }
                            entity.setCountedAmount(countedAmount);
                        }
                    });
            ruleVos.stream().filter(rule -> rule.getType() == CommissionRuleType.SERVICE.getCode()).findFirst()
                    .ifPresent(rule -> {
                        if (x.getServiceAmount() > 0) {
                            long serviceAmount = (long) (rule.getRate() * 100);
                            if (rule.getUnit() == CommissionRuleUnit.PERCENTAGE.getCode()) {
                                serviceAmount = (long) (x.getServiceAmount() * rule.getRate() / 100);
                            }
                            entity.setServiceAmount(serviceAmount);
                        }
                    });
            entity.setTotalAmount(entity.getCashierAmount() + entity.getRechargeAmount() + entity.getCountedAmount() + entity.getServiceAmount());
            return entity;
        }).collect(Collectors.toList());
        batchEdit(list);
    }

    public void batchEdit(List<CommissionEntity> list) {
        list.forEach(x -> {
            CommissionEntity entity = commissionDao.findByCommissionDate(x.getMerchantNo(), x.getStoreNo(), x.getAccountId(), x.getCommissionTime());
            if (entity != null) {
                x.setId(entity.getId());
                x.setUpdateTime(LocalDateTime.now());
                commissionDao.edit(x);
            } else {
                x.setId(Generate.generateUUID());
                x.setCreateTime(LocalDateTime.now());
                x.setUpdateTime(LocalDateTime.now());
                commissionDao.save(x);
            }
        });
    }

    public PageVo<CommissionVo> page(long merchantNo, long storeNo, String accountId, CommissionQuery query) {
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setStoreNo(query.getStoreNo());
        accountQuery.setPageIndex(query.getPageIndex());
        accountQuery.setPageSize(query.getPageSize());
        PageVo<UserVo> userPage = accountService.page(merchantNo, storeNo, accountId, accountQuery);
        if (userPage.getTotal() == 0) {
            return PageResult.of();
        }
        List<UserVo> accounts = userPage.getData();
        List<String> accountIds = accounts.stream().map(UserVo::getId).collect(Collectors.toList());

        CommissionListQuery listQuery = new CommissionListQuery();
        listQuery.setAccountIds(accountIds);
        listQuery.setCommissionStartTime(query.getCommissionStartTime());
        listQuery.setCommissionEndTime(query.getCommissionEndTime());
        List<CommissionEntity> list = commissionDao.findList(merchantNo, listQuery);
        List<CommissionVo> vos = accounts.stream().map(account -> {
            CommissionVo vo = new CommissionVo();
            vo.setMerchantNo(account.getMerchantNo());
            vo.setStoreNo(account.getStoreNo());
            vo.setAccountId(account.getId());
            vo.setAccountName(account.getName());

            List<CommissionEntity> tempList = list.stream().filter(x -> x.getAccountId().equals(account.getId()))
                    .collect(Collectors.toList());

            long totalAmount = tempList.stream().mapToLong(CommissionEntity::getTotalAmount).sum();
            long cashierAmount = tempList.stream().mapToLong(CommissionEntity::getCashierAmount).sum();
            long rechargeAmount = tempList.stream().mapToLong(CommissionEntity::getRechargeAmount).sum();
            long countedAmount = tempList.stream().mapToLong(CommissionEntity::getCountedAmount).sum();
            long serviceAmount = tempList.stream().mapToLong(CommissionEntity::getServiceAmount).sum();

            vo.setTotalAmount(AmountUtil.parseBigDecimal(totalAmount));
            vo.setCashierAmount(AmountUtil.parseBigDecimal(cashierAmount));
            vo.setRechargeAmount(AmountUtil.parseBigDecimal(rechargeAmount));
            vo.setCountedAmount(AmountUtil.parseBigDecimal(countedAmount));
            vo.setServiceAmount(AmountUtil.parseBigDecimal(serviceAmount));
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), userPage.getTotal(), vos);
    }
}
