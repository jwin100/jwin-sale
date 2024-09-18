package com.mammon.summary.service;

import com.mammon.cashier.domain.entity.CashierOrderEntity;
import com.mammon.cashier.domain.enums.CashierOrderCategory;
import com.mammon.cashier.domain.enums.CashierOrderStatus;
import com.mammon.cashier.domain.query.CashierOrderSummaryQuery;
import com.mammon.cashier.service.CashierOrderService;
import com.mammon.member.domain.entity.MemberEntity;
import com.mammon.member.domain.entity.MemberRechargeOrderEntity;
import com.mammon.member.domain.query.MemberRechargeOrderSummaryQuery;
import com.mammon.member.domain.query.MemberSummaryQuery;
import com.mammon.member.service.MemberRechargeOrderService;
import com.mammon.member.service.MemberService;
import com.mammon.summary.dao.SummaryCashierDao;
import com.mammon.summary.domain.entity.SummaryCashierEntity;
import com.mammon.summary.domain.query.SummaryHomeDashQuery;
import com.mammon.summary.domain.query.SummaryHomeTrendQuery;
import com.mammon.summary.domain.vo.SummaryHomeDashVo;
import com.mammon.summary.domain.vo.SummaryHomeTrendVo;
import com.mammon.utils.AmountUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/2/29 14:28
 */
@Service
public class SummaryHomeService {

    @Resource
    private SummaryCashierDao summaryCashierDao;

    @Resource
    private CashierOrderService cashierOrderService;

    @Resource
    private MemberService memberService;

    @Resource
    private MemberRechargeOrderService memberRechargeOrderService;

    public SummaryHomeDashVo summaryHomeDash(long merchantNo, long storeNo, SummaryHomeDashQuery query) {
        SummaryHomeDashVo vo = new SummaryHomeDashVo();
        summaryCashierDash(merchantNo, storeNo, query, vo);
        summaryMemberDash(merchantNo, storeNo, query, vo);
        summaryMemberRechargeDash(merchantNo, storeNo, query, vo);
        return vo;
    }

    private void summaryCashierDash(long merchantNo, long storeNo, SummaryHomeDashQuery query, SummaryHomeDashVo vo) {
        CashierOrderSummaryQuery cashierQuery = new CashierOrderSummaryQuery();
        if (query.isAllStore()) {
            cashierQuery.setStoreNo(null);
        } else {
            cashierQuery.setStoreNo(storeNo);
        }
        cashierQuery.setMerchantNo(merchantNo);
        cashierQuery.setStartDate(query.getStartDate());
        cashierQuery.setEndDate(query.getEndDate());
        cashierQuery.setCategory(CashierOrderCategory.GOODS.getCode());
        cashierQuery.setStatus(CashierOrderStatus.FINISH.getCode());
        List<CashierOrderEntity> summaryCashiers = cashierOrderService.summaryList(cashierQuery);

        // 销售金额=订单实付金额-退款金额
        long cashierAmount = summaryCashiers.stream().mapToLong(x -> x.getRealityAmount() - x.getRefundAmount()).sum();
        int cashierTotal = summaryCashiers.size();
        vo.setCashierTotal(cashierTotal);
        vo.setCashierAmount(AmountUtil.parseBigDecimal(cashierAmount));
    }

    private void summaryMemberDash(long merchantNo, long storeNo, SummaryHomeDashQuery query, SummaryHomeDashVo vo) {
        MemberSummaryQuery memberQuery = new MemberSummaryQuery();
        if (query.isAllStore()) {
            memberQuery.setStoreNo(null);
        } else {
            memberQuery.setStoreNo(storeNo);
        }
        memberQuery.setMerchantNo(merchantNo);
        memberQuery.setStartDate(query.getStartDate());
        memberQuery.setEndDate(query.getEndDate());
        List<MemberEntity> members = memberService.summaryList(memberQuery);
        vo.setMemberRegisterTotal(members.size());
    }

    private void summaryMemberRechargeDash(long merchantNo, long storeNo, SummaryHomeDashQuery query, SummaryHomeDashVo vo) {
        MemberRechargeOrderSummaryQuery rechargeQuery = new MemberRechargeOrderSummaryQuery();
        if (query.isAllStore()) {
            rechargeQuery.setStoreNo(null);
        } else {
            rechargeQuery.setStoreNo(storeNo);
        }
        rechargeQuery.setMerchantNo(merchantNo);
        rechargeQuery.setStartDate(query.getStartDate());
        rechargeQuery.setEndDate(query.getEndDate());
        List<MemberRechargeOrderEntity> list = memberRechargeOrderService.summaryList(rechargeQuery);

        long rechargeAmount = list.stream().mapToLong(MemberRechargeOrderEntity::getReceivesAmount).sum();
        vo.setMemberRechargeAmount(AmountUtil.parseBigDecimal(rechargeAmount));
    }

    public List<SummaryHomeTrendVo> summaryHomeTrend(long merchantNo, long storeNo, SummaryHomeTrendQuery query) {
        List<SummaryHomeTrendVo> vos = new ArrayList<>();
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        endDate = endDate.plusDays(1);
        List<SummaryCashierEntity> list = summaryCashierDao.findListByDate(merchantNo, storeNo, startDate, endDate);
        while (startDate.isBefore(endDate)) {
            LocalDate finalStartDate = startDate;

            List<SummaryCashierEntity> finalList = list.stream().filter(x -> finalStartDate.equals(x.getSummaryDate())).collect(Collectors.toList());
            int cashierTotal = finalList.stream().mapToInt(SummaryCashierEntity::getCashierTotal).sum();
            long cashierAmount = finalList.stream().mapToLong(SummaryCashierEntity::getCashierAmount).sum();
            long refundAmount = finalList.stream().mapToLong(SummaryCashierEntity::getRefundAmount).sum();

            SummaryHomeTrendVo vo = new SummaryHomeTrendVo();
            vo.setSummaryDate(startDate);
            vo.setCashierTotal(cashierTotal);
            vo.setCashierAmount(AmountUtil.parseBigDecimal(cashierAmount));
            vo.setRefundAmount(AmountUtil.parseBigDecimal(refundAmount));
            vos.add(vo);

            startDate = startDate.plusDays(1);
        }
        return vos;
    }
}
