package com.mammon.member.service;

import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.market.domain.vo.MarketTimeCardRuleListVo;
import com.mammon.market.domain.vo.MarketTimeCardRuleVo;
import com.mammon.market.domain.vo.MarketTimeCardSpuVo;
import com.mammon.market.service.MarketTimeCardRuleService;
import com.mammon.member.dao.MemberTimeCardDao;
import com.mammon.member.domain.dto.*;
import com.mammon.member.domain.entity.MemberEntity;
import com.mammon.member.domain.entity.MemberTimeCardEntity;
import com.mammon.member.domain.vo.*;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.clerk.service.AccountService;
import com.mammon.sms.domain.dto.MemberCountedNoticeDto;
import com.mammon.sms.service.SmsSendNoticeService;
import com.mammon.sms.service.SmsSendService;
import com.mammon.sms.service.SmsTemplateSettingService;
import com.mammon.stock.domain.vo.StockSkuVo;
import com.mammon.stock.service.StockSkuService;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MemberTimeCardService {

    @Resource
    private MemberService memberService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private MemberTimeCardDao memberTimeCardDao;

    @Resource
    private MarketTimeCardRuleService marketTimeCardRuleService;

    @Resource
    private MemberTimeCardLogService memberTimeCardLogService;

    @Resource
    private AccountService accountService;

    @Resource
    private SmsSendService smsSendService;

    // 计次卡购买，计次卡购买退款，计次卡消费，计次卡消费退款

    @Resource
    private SmsTemplateSettingService smsTemplateSettingService;

    @Resource
    private StockSkuService stockSkuService;

    @Resource
    private SmsSendNoticeService smsSendNoticeService;

    public MemberTimeCardEntity save(MemberTimeCardEntity entity) {
        return memberTimeCardDao.save(entity);
    }

    public MemberTimeCardEntity edit(String id, String memberId, int addTimeCard, int expireType, LocalDateTime expireTime) {
        return memberTimeCardDao.edit(id, memberId, addTimeCard, expireType, expireTime);
    }

    public MemberTimeCardEntity edit(String id, String memberId, long timeCard) {
        return memberTimeCardDao.edit(id, memberId, timeCard);
    }

    public void deleteById(String id) {
        memberTimeCardDao.deleteById(id);
    }

    /**
     * 计次开卡
     */
    public void created(String accountId, String memberId, String orderNo, int quantity, String ruleId) {
        MarketTimeCardRuleVo timeCardRule = marketTimeCardRuleService.findById(ruleId);
        if (timeCardRule == null) {
            throw new CustomException("获取计次卡信息失败");
        }
        int addTimeCard = timeCardRule.getTimeTotal() * quantity;
        MemberTimeCardEntity timeCard = findByMemberIdAndTimeCardRuleId(memberId, timeCardRule.getId());
        if (timeCard != null) {
            LocalDateTime expireTime = null;
            LocalDateTime originalExpireTime = timeCard.getExpireTime() == null ? LocalDateTime.now() : timeCard.getExpireTime();
            if (timeCardRule.getExpireType() == 1) {
                expireTime = originalExpireTime.plusMonths(timeCardRule.getExpireMonth());
            }
            timeCard = edit(timeCard.getId(), memberId, addTimeCard, timeCardRule.getExpireType(), expireTime);
        } else {
            timeCard = new MemberTimeCardEntity();
            timeCard.setId(Generate.generateUUID());
            timeCard.setMemberId(memberId);
            timeCard.setTimeCardId(timeCardRule.getId());
            timeCard.setName(timeCardRule.getName());
            timeCard.setExpireType(timeCardRule.getExpireType());
            if (timeCardRule.getExpireType() == 1) {
                timeCard.setExpireTime(LocalDateTime.now().plusMonths(timeCardRule.getExpireMonth()));
            }
            timeCard.setNowTimeCard(addTimeCard);
            timeCard.setTotalTimeCard(addTimeCard);
            timeCard.setSpuIds(JsonUtil.toJSONString(timeCardRule.getSpuIds()));
            timeCard.setCreateTime(LocalDateTime.now());
            timeCard.setUpdateTime(LocalDateTime.now());
            timeCard = save(timeCard);
        }
        MemberTimeCardLogDto dto = new MemberTimeCardLogDto();
        dto.setAccountId(accountId);
        dto.setMemberId(memberId);
        dto.setOrderNo(orderNo);
        dto.setChangeType(1);
        dto.setTimeTotal(addTimeCard);
        dto.setChangeAfter(timeCard.getNowTimeCard());
        dto.setRemark("购买计次卡");
        memberTimeCardLogService.create(dto);
        // 事务提交后执行
        // 发送短信通知
        MemberCountedNoticeDto noticeDto = new MemberCountedNoticeDto();
        noticeDto.setMemberId(memberId);
        noticeDto.setTimeCardName(timeCard.getName());
        noticeDto.setAddTimeCard(addTimeCard);
        noticeDto.setNowTimeCard(timeCard.getNowTimeCard());
        smsSendNoticeService.memberCountedCreateSend(noticeDto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void countedChange(MemberTimeCardLogDto dto) {
        memberTimeCardLogService.create(dto);
        // 事务提交后执行
        // 发送短信通知
        MemberCountedNoticeDto noticeDto = new MemberCountedNoticeDto();
        noticeDto.setMemberId(dto.getMemberId());
        noticeDto.setTimeCardName(noticeDto.getTimeCardName());
        noticeDto.setAddTimeCard((int) dto.getTimeTotal());
        noticeDto.setNowTimeCard(dto.getChangeType());
        smsSendNoticeService.memberCountedCreateSend(noticeDto);
    }

    /**
     * 计次退卡
     *
     * @param accountId
     * @param memberId
     * @param refundNo
     * @param ruleId
     */
    public void refund(String accountId, String memberId, String refundNo, BigDecimal quantity, String ruleId) {
        MemberTimeCardEntity timeCard = findByMemberIdAndTimeCardRuleId(memberId, ruleId);
        if (timeCard == null) {
            return;
        }
        MarketTimeCardRuleVo timeCardRule = marketTimeCardRuleService.findById(ruleId);
        if (timeCardRule == null) {
            return;
        }
        int addTimeCard = timeCardRule.getTimeTotal() * quantity.intValue();
        timeCard = edit(timeCard.getId(), memberId, -addTimeCard);
        MemberTimeCardLogDto dto = new MemberTimeCardLogDto();
        dto.setAccountId(accountId);
        dto.setMemberId(memberId);
        dto.setOrderNo(refundNo);
        dto.setChangeType(1);
        dto.setTimeTotal(-addTimeCard);
        dto.setChangeAfter(timeCard.getNowTimeCard());
        dto.setRemark("计次卡退款");
        memberTimeCardLogService.create(dto);
    }

    /**
     * 计次消费减次
     *
     * @param accountId
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TimeCardChangeVo timeCardConsume(String accountId, MemberTimeCardConsumeDto dto) {
        MemberTimeCardEntity timeCardEntity = findById(dto.getCountedId());
        if (timeCardEntity == null || !timeCardEntity.getMemberId().equals(dto.getMemberId())) {
            return TimeCardChangeVo.builder().message("计次卡信息错误").build();
        }
        if (timeCardEntity.getExpireType() == 1 && LocalDate.now().isAfter(timeCardEntity.getExpireTime().toLocalDate())) {
            return TimeCardChangeVo.builder().message("计次卡已过期，无法使用").build();
        }
        if (timeCardEntity.getNowTimeCard() < dto.getCountedTotal()) {
            return TimeCardChangeVo.builder().message("计次卡余额不足").build();
        }
        long timeCardTotal = -dto.getCountedTotal();
        MemberTimeCardEntity timeCard = edit(dto.getCountedId(), dto.getMemberId(), timeCardTotal);
        if (timeCard == null) {
            return TimeCardChangeVo.builder().message("计次卡核销失败").build();
        }

        MemberTimeCardLogDto logDto = new MemberTimeCardLogDto();
        logDto.setCountedId(dto.getCountedId());
        logDto.setAccountId(accountId);
        logDto.setMemberId(dto.getMemberId());
        logDto.setOrderNo(dto.getOrderNo());
        logDto.setChangeType(dto.getChangeType());
        logDto.setTimeTotal(timeCardTotal);
        logDto.setChangeAfter(timeCard.getNowTimeCard());
        logDto.setRemark(dto.getRemark());
        afterCountedChange(logDto);
        return TimeCardChangeVo.builder().code(1).build();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCountedChange(MemberTimeCardLogDto logDto) {
        memberTimeCardLogService.create(logDto);
        // 发送短信通知
        smsSendNoticeService.timeCardConsumeSms(logDto.getCountedId());
    }

    /**
     * 计次退款返还
     *
     * @param accountId
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TimeCardChangeVo timeCardRefund(String accountId, MemberTimeCardConsumeDto dto) {
        MemberTimeCardEntity timeCardEntity = findById(dto.getCountedId());
        if (timeCardEntity == null || !timeCardEntity.getMemberId().equals(dto.getMemberId())) {
            return TimeCardChangeVo.builder().message("计次卡信息错误").build();
        }
        MemberTimeCardEntity timeCard = edit(dto.getCountedId(), dto.getMemberId(), dto.getCountedTotal());
        if (timeCard == null) {
            return TimeCardChangeVo.builder().message("计次卡退款失败").build();
        }

        MemberTimeCardLogDto logDto = new MemberTimeCardLogDto();
        logDto.setCountedId(dto.getCountedId());
        logDto.setAccountId(accountId);
        logDto.setMemberId(dto.getMemberId());
        logDto.setOrderNo(dto.getOrderNo());
        logDto.setChangeType(dto.getChangeType());
        logDto.setTimeTotal(dto.getCountedTotal());
        logDto.setChangeAfter(timeCard.getNowTimeCard());
        logDto.setRemark(dto.getRemark());
        memberTimeCardLogService.create(logDto);
        // 发送短信通知
        afterCountedChange(logDto);
        return TimeCardChangeVo.builder().code(1).build();
    }

    public MemberTimeCardEntity findById(String id) {
        return memberTimeCardDao.findById(id);
    }

    public MemberTimeCardEntity findByMemberIdAndTimeCardRuleId(String memberId, String timeCardRuleId) {
        return memberTimeCardDao.findByMemberIdAndRuleId(memberId, timeCardRuleId);
    }

    public List<MemberTimeCardEntity> findAllByMemberIdAndSpuId(String memberId, String spuId) {
        List<MemberTimeCardEntity> timeCards = memberTimeCardDao.findByMemberId(memberId);
        if (CollectionUtils.isEmpty(timeCards)) {
            return Collections.emptyList();
        }
        List<String> timeCardIds = timeCards.stream().map(MemberTimeCardEntity::getTimeCardId).collect(Collectors.toList());
        List<MarketTimeCardRuleListVo> ruleVos = marketTimeCardRuleService.findAllByIds(timeCardIds);
        return ruleVos.stream().map(rule -> {
            if (CollectionUtils.isEmpty(rule.getSpuIds()) || rule.getSpuIds().contains(spuId)) {
                return timeCards.stream().filter(x -> x.getTimeCardId().equals(rule.getId()))
                        .findFirst().orElse(null);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<MemberTimeCardListVo> findAllByMemberId(String memberId) {
        MemberEntity member = memberService.findBaseById(memberId);
        if (member == null) {
            return Collections.emptyList();
        }
        List<MemberTimeCardEntity> list = memberTimeCardDao.findByMemberId(memberId);
        return list.stream().map(timeCard -> {
            MemberTimeCardListVo vo = new MemberTimeCardListVo();
            BeanUtils.copyProperties(timeCard, vo);
            vo.setSpuIds(JsonUtil.toList(timeCard.getSpuIds(), String.class));
            List<StockSkuVo> skus = stockSkuService.findListBySpuIds(member.getMerchantNo(), member.getStoreNo(), vo.getSpuIds());
            List<MarketTimeCardSpuVo> spus = skus.stream().map(x -> {
                MarketTimeCardSpuVo spuVo = new MarketTimeCardSpuVo();
                spuVo.setSkuId(x.getSkuId());
                spuVo.setSkuName(x.getSkuName());
                spuVo.setSpuId(x.getSpuId());
                spuVo.setSpuName(x.getSkuName());
                spuVo.setSellStock(x.getSellStock());
                return spuVo;
            }).collect(Collectors.toList());
            vo.setSpus(spus);
            return vo;
        }).collect(Collectors.toList());
    }
}
