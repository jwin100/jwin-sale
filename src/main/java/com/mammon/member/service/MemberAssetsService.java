package com.mammon.member.service;

import cn.hutool.core.collection.CollUtil;
import com.mammon.exception.CustomException;
import com.mammon.market.domain.vo.MarketRechargeRuleVo;
import com.mammon.market.service.MarketRechargeRuleService;
import com.mammon.member.dao.MemberAssetsDao;
import com.mammon.member.domain.dto.MemberAssetsConsumeDto;
import com.mammon.member.domain.dto.MemberRechargeOrderDto;
import com.mammon.member.domain.entity.MemberAssetsEntity;
import com.mammon.member.domain.enums.MemberAssetsCategory;
import com.mammon.member.domain.enums.MemberAssetsLogType;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.sms.service.SmsSendNoticeService;
import com.mammon.sms.service.SmsSendService;
import com.mammon.sms.service.SmsTemplateSettingService;
import com.mammon.utils.AmountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author dcl
 * @date 2023-04-04 11:32:58
 */
@Service
@Slf4j
public class MemberAssetsService {

    @Resource
    private MemberRechargeOrderService memberRechargeOrderService;

    @Resource
    private MemberAssetsLogService memberAssetsLogService;

    @Resource
    private MemberAttrService memberAttrService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private SmsSendService smsSendService;

    @Resource
    private MemberService memberService;

    @Resource
    private SmsTemplateSettingService smsTemplateSettingService;

    @Resource
    private MemberAssetsDao memberAssetsDao;

    @Resource
    private MarketRechargeRuleService marketRechargeRuleService;

    @Resource
    private SmsSendNoticeService smsSendNoticeService;

    /**
     * 会员开卡初始化
     *
     * @param id
     */
    public void init(long merchantNo, String id, long addRecharge, long addIntegral) {
        MemberAssetsEntity entity = new MemberAssetsEntity();
        entity.setId(id);
        entity.setNowIntegral(addIntegral);
        entity.setAccrualIntegral(addIntegral);
        entity.setNowRecharge(addRecharge);
        entity.setAccrualRecharge(addRecharge);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        memberAssetsDao.save(entity);
        if (addRecharge > 0) {
            memberAssetsLogService.create(id, MemberAssetsCategory.RECHARGE_ORDER.getCode(),
                    MemberAssetsLogType.CHANGE_RECHARGE.getCode(),
                    null, 0, addRecharge, entity.getNowRecharge(), "会员储值");
        }
        if (addIntegral > 0) {
            this.addIntegral(merchantNo, id, null, addIntegral, "开卡赠送");
        }
    }
    // 储值退款改一下

    /**
     * 充值
     *
     * @param memberId
     */
    @Transactional(rollbackFor = Exception.class)
    public void investRecharge(long merchantNo, long storeNo, String memberId, String orderNo, long quantity, String ruleId) {
        MarketRechargeRuleVo ruleVo = marketRechargeRuleService.findById(ruleId);
        if (ruleVo == null) {
            return;
        }
        long addRecharge = AmountUtil.parse(ruleVo.getPrepaidAmount()) * quantity;

        // 根据会员设置判断是否增加积分
        MemberAssetsEntity entity = findById(memberId);
        if (entity == null) {
            throw new CustomException("会员不存在");
        }
        long beforeRecharge = entity.getNowRecharge();
        long afterRecharge = entity.getNowRecharge() + addRecharge;
        long accrualRecharge = entity.getAccrualRecharge() + addRecharge;

        entity.setId(memberId);
        entity.setNowRecharge(afterRecharge);
        entity.setAccrualRecharge(accrualRecharge);
        entity.setUpdateTime(LocalDateTime.now());
        memberAssetsDao.updateRecharge(entity);

        //储值记录
        MemberRechargeOrderDto rechargeOrderDto = new MemberRechargeOrderDto();
        rechargeOrderDto.setMemberId(memberId);
        rechargeOrderDto.setOrderNo(orderNo);
        rechargeOrderDto.setRuleId(ruleId);
        rechargeOrderDto.setQuantity(quantity);
        rechargeOrderDto.setRechargeAmount(AmountUtil.parse(ruleVo.getPrepaidAmount()));
        rechargeOrderDto.setGiveAmount(AmountUtil.parse(ruleVo.getGiveAmount()));
        rechargeOrderDto.setReceivesAmount(AmountUtil.parse(ruleVo.getRealAmount()));
        rechargeOrderDto.setGiveIntegral(ruleVo.getGiveIntegral());
        rechargeOrderDto.setRechargeTime(LocalDateTime.now());
        memberRechargeOrderService.create(merchantNo, storeNo, rechargeOrderDto);

        // 储值日志
        memberAssetsLogService.create(memberId, MemberAssetsCategory.RECHARGE_ORDER.getCode(),
                MemberAssetsLogType.CHANGE_RECHARGE.getCode(),
                orderNo, beforeRecharge, addRecharge, afterRecharge, "会员充值");
        // 事务提交后执行
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 发送短信通知
                smsSendNoticeService.memberRechargeSend(memberId, addRecharge, afterRecharge);
            }
        });
    }

    /**
     * 充值退款
     *
     * @param memberId
     */
    @Transactional(rollbackFor = Exception.class)
    public void investReturnRecharge(long merchantNo, String memberId, String orderNo, String refundNo, BigDecimal quantity, String ruleId) {
        MemberAssetsEntity entity = findById(memberId);
        if (entity == null) {
            throw new CustomException("会员不存在");
        }
        MarketRechargeRuleVo ruleVo = marketRechargeRuleService.findById(ruleId);
        if (ruleVo == null) {
            return;
        }
        long addRecharge = AmountUtil.parse(ruleVo.getPrepaidAmount().multiply(quantity));

        long beforeAssets = entity.getNowRecharge();
        long afterAssets = entity.getNowRecharge() - addRecharge;
        long accrualRecharge = entity.getAccrualRecharge() - addRecharge;

        entity.setId(memberId);
        entity.setNowRecharge(afterAssets);
        entity.setAccrualRecharge(accrualRecharge);
        entity.setUpdateTime(LocalDateTime.now());
        memberAssetsDao.updateRecharge(entity);

        // 会员储值退款
        memberRechargeOrderService.refund(merchantNo, orderNo, LocalDateTime.now());

        // 记录退储值日志
        memberAssetsLogService.create(memberId, MemberAssetsCategory.RECHARGE_REFUND.getCode(),
                MemberAssetsLogType.CHANGE_RECHARGE.getCode(),
                refundNo, beforeAssets, -addRecharge, afterAssets, "会员充值退款");
    }

    /**
     * 储值余额消费
     *
     * @param dto 会员消费信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void rechargeConsume(MemberAssetsConsumeDto dto) {
        if (dto.getChangeAmount() > 0) {
            dto.setChangeAmount(-dto.getChangeAmount());
        }
        rechargeChange(dto, MemberAssetsCategory.CASHIER_ORDER.getCode());
    }

    /**
     * 消费退款
     */
    @Transactional(rollbackFor = Exception.class)
    public void rechargeRefund(MemberAssetsConsumeDto dto) {
        rechargeChange(dto, MemberAssetsCategory.CASHIER_REFUND.getCode());
    }

    /**
     * 余额变更
     * <p>
     * 储值金额做累加，如果是减余额操作记得传负数过来
     *
     * @param dto            余额变更信息
     * @param assetsCategory 变更类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void rechargeChange(MemberAssetsConsumeDto dto, int assetsCategory) {
        MemberAssetsEntity entity = getMemberAssets(dto.getMemberId());
        long beforeRecharge = entity.getNowRecharge();
        setRecharge(entity, assetsCategory, dto.getChangeAmount());
        memberAssetsDao.updateRecharge(entity);
        // 储值日志
        memberAssetsLogService.create(dto.getMemberId(),
                MemberAssetsCategory.CASHIER_ORDER.getCode(),
                MemberAssetsLogType.CHANGE_RECHARGE.getCode(),
                dto.getOrderNo(), beforeRecharge, dto.getChangeAmount(), entity.getNowRecharge(), "储值余额变更");
        // 事务提交后执行
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 发送短信通知
                smsSendNoticeService.memberRechargeChangeSend(dto.getMemberId(), dto.getChangeAmount(), entity.getNowRecharge());
            }
        });
    }

    public MemberAssetsEntity getMemberAssets(String memberId) {
        MemberAssetsEntity entity = findById(memberId);
        if (entity == null) {
            throw new CustomException("会员不存在");
        }
        return entity;
    }

    public void setRecharge(MemberAssetsEntity entity, int assetsCategory, long changeAmount) {
        long afterRecharge = entity.getNowRecharge() + changeAmount;
        long accrualRecharge = entity.getAccrualRecharge();
        if (afterRecharge < 0) {
            throw new CustomException("余额不足");
        }
        if (assetsCategory == MemberAssetsCategory.RECHARGE_ORDER.getCode()) {
            accrualRecharge += changeAmount;
        }
        // 余额变更
        entity.setNowRecharge(afterRecharge);
        entity.setAccrualRecharge(accrualRecharge);
        entity.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 增加积分
     *
     * @param id
     */
    public void addIntegral(long merchantNo, String id, String orderNo, long addIntegral, String remark) {
        MemberAssetsEntity entity = findById(id);
        if (entity == null) {
            throw new CustomException("会员不存在");
        }
        long beforeIntegral = entity.getNowIntegral();
        long nowIntegral = entity.getNowIntegral() + addIntegral;
        long accrualIntegral = entity.getAccrualIntegral() + addIntegral;

        entity.setId(id);
        entity.setNowIntegral(nowIntegral);
        entity.setAccrualIntegral(accrualIntegral);
        entity.setUpdateTime(LocalDateTime.now());
        memberAssetsDao.updateIntegral(entity);

        // 储值日志
        memberAssetsLogService.create(id, MemberAssetsCategory.INTEGRAL_ADD.getCode(),
                MemberAssetsLogType.CHANGE_INTEGRAL.getCode(),
                orderNo, beforeIntegral, addIntegral, nowIntegral, remark);

        memberService.syncMemberLevel(merchantNo, id);
    }

    /**
     * 退货减少积分-不能和积分消费共用
     *
     * @param id
     */
    public void removeIntegral(long merchantNo, String id, String orderNo, long removeIntegral, String remark) {
        MemberAssetsEntity entity = findById(id);
        if (entity == null) {
            throw new CustomException("会员不存在");
        }
        long beforeIntegral = entity.getNowIntegral();
        long afterIntegral = entity.getNowIntegral() - removeIntegral;
        long accrualIntegral = entity.getAccrualIntegral() - Math.abs(removeIntegral);

        entity.setId(id);
        entity.setNowIntegral(afterIntegral);
        entity.setAccrualIntegral(accrualIntegral);
        entity.setUpdateTime(LocalDateTime.now());
        memberAssetsDao.updateIntegral(entity);

        // 储值日志
        memberAssetsLogService.create(id, MemberAssetsCategory.INTEGRAL_REMOVE.getCode(),
                MemberAssetsLogType.CHANGE_INTEGRAL.getCode(),
                orderNo, beforeIntegral, -removeIntegral, afterIntegral, remark);

        memberService.syncMemberLevel(merchantNo, id);
    }

    public MemberAssetsEntity findById(String id) {
        return memberAssetsDao.findById(id);
    }

    public List<MemberAssetsEntity> findListByIds(List<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return memberAssetsDao.findListByIds(ids);
    }
}
