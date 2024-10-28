package com.mammon.member.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import com.mammon.cashier.domain.entity.CashierOrderEntity;
import com.mammon.cashier.domain.entity.CashierRefundEntity;
import com.mammon.cashier.domain.enums.CashierOrderCategory;
import com.mammon.cashier.domain.enums.CashierOrderType;
import com.mammon.cashier.domain.enums.CashierRefundStatus;
import com.mammon.cashier.domain.query.CashierOrderSummaryQuery;
import com.mammon.cashier.domain.query.CashierRefundSummaryQuery;
import com.mammon.cashier.service.CashierOrderService;
import com.mammon.cashier.service.CashierRefundService;
import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.common.ResultCode;
import com.mammon.enums.CommonSource;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.exception.CustomException;
import com.mammon.member.dao.MemberDao;
import com.mammon.member.domain.dto.*;
import com.mammon.member.domain.entity.*;
import com.mammon.member.domain.enums.MemberChannel;
import com.mammon.member.domain.enums.MemberSex;
import com.mammon.member.domain.query.MemberQuery;
import com.mammon.member.domain.query.MemberSummaryQuery;
import com.mammon.member.domain.vo.MemberInfoVo;
import com.mammon.member.domain.vo.MemberSummaryCashierVo;
import com.mammon.member.domain.vo.MemberTagMapVo;
import com.mammon.member.domain.vo.MemberTimeCardListVo;
import com.mammon.merchant.domain.entity.MerchantStoreEntity;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.clerk.service.AccountService;
import com.mammon.sms.service.SmsSendNoticeService;
import com.mammon.sms.service.SmsSendService;
import com.mammon.sms.service.SmsTemplateSettingService;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MemberService {

    @Resource
    private MemberDao memberDao;

    @Resource
    private MemberAssetsService memberAssetsService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private AccountService accountService;

    @Resource
    private SmsSendService smsSendService;

    @Resource
    private SmsTemplateSettingService smsTemplateSettingService;

    @Resource
    private MemberTagMapService memberTagMapService;

    @Resource
    private MemberTagService memberTagService;

    @Resource
    private MemberTimeCardService memberTimeCardService;

    @Resource
    private MemberLevelService memberLevelService;

    @Resource
    private SmsSendNoticeService smsSendNoticeService;

    @Resource
    private CashierOrderService cashierOrderService;

    @Resource
    private CashierRefundService cashierRefundService;

    @Transactional(rollbackFor = CustomException.class)
    public void create(long merchantNo, long storeNo, String accountId, MemberDto dto) {
        create(merchantNo, storeNo, accountId, dto, 0, 0);
    }

    @Transactional(rollbackFor = Exception.class)
    public MemberEntity create(long merchantNo, long storeNo, String accountId, MemberDto dto,
                               long initRecharge, long initIntegral) {
        if (existByPhone(merchantNo, dto.getPhone())) {
            throw new CustomException(ResultCode.BAD_REQUEST, "会员手机号已存在");
        }
        MemberEntity entity = new MemberEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setAccountId(accountId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        memberDao.save(entity);

        memberAssetsService.init(merchantNo, entity.getId(), initRecharge, initIntegral);
        // 修改会员等级
        syncMemberLevel(merchantNo, entity.getId());
        // 事务提交后执行
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 发送短信通知
                smsSendNoticeService.memberRegisterSend(accountId, entity.getId());
            }
        });
        return entity;
    }

    @Transactional(rollbackFor = CustomException.class)
    public void memberImport(long merchantNo, long storeNo, String accountId, MultipartFile file) {
        List<MemberImportDto> dtos = ExcelUtils.read(file, MemberImportDto.class);
        if (CollectionUtils.isEmpty(dtos)) {
            throw new CustomException("没有获取到文件内容");
        }
        dtos.forEach(x -> {
            int sex = MemberSex.MAN.getCode();
            if ("女".equals(x.getSex())) {
                sex = MemberSex.WOMAN.getCode();
            }

            MemberDto dto = new MemberDto();
            dto.setPhone(x.getPhone());
            dto.setName(x.getName());
            dto.setSex(sex);
            dto.setBirthDay(x.getBirthDay());
            dto.setRemark("文件导入会员");
            dto.setSource(CommonSource.ADMIN.getCode());
            dto.setChannel(MemberChannel.MANUAL.getCode());
            long initRechage = AmountUtil.parse(x.getRecharge());
            long initIntegral = 0;
            if (x.getIntegral() != null) {
                initIntegral = x.getIntegral();
            }
            MemberEntity member = create(merchantNo, storeNo, accountId, dto, initRechage, initIntegral);
            initTags(merchantNo, member.getId(), x.getTags());
        });
    }

    public void initTags(long merchantNo, String memberId, String tags) {
        if (StringUtils.isBlank(tags)) {
            return;
        }
        List<String> tagIds = new ArrayList<>();
        List<String> tagList = Arrays.stream(tags.replaceAll("，", ",").split(",")).collect(Collectors.toList());
        tagList.forEach(x -> {
            MemberTagEntity entity = memberTagService.findByName(merchantNo, x);
            if (entity != null) {
                tagIds.add(entity.getId());
            } else {
                MemberTagDto dto = new MemberTagDto();
                dto.setName(x);
                dto.setStatus(CommonStatus.ENABLED.getCode());
                entity = memberTagService.create(merchantNo, dto);
                if (entity != null) {
                    tagIds.add(entity.getId());
                }
            }
        });
        memberTagMapService.batchSave(memberId, tagIds);
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(long merchantNo, long storeNo, String accountId, String id, MemberDto dto) {
        if (!existById(merchantNo, id)) {
            throw new CustomException(ResultCode.BAD_REQUEST, "会员不存在");
        }

        MemberEntity entity = new MemberEntity();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
        memberDao.edit(entity);
        if (!Validator.isMobile(dto.getPhone())) {
            MemberEntity member = memberDao.findByPhone(merchantNo, dto.getPhone());
            if (member != null && !member.getId().equals(id)) {
                throw new CustomException("手机号重复");
            }
            memberDao.editPhone(id, dto.getPhone());
        }
    }

    public int editStatus(long merchantNo, long storeNo, String accountId, String id, Integer status) {
        if (!existById(merchantNo, id)) {
            throw new CustomException(ResultCode.BAD_REQUEST, "会员不存在");
        }
        if (IEnum.getByCode(status, CommonStatus.class) == null) {
            throw new CustomException(ResultCode.BAD_REQUEST, "修改状态错误");
        }
        return memberDao.editStatus(merchantNo, id, status);
    }

    public void syncMemberLevel(long merchantNo) {
        List<MemberInfoVo> members = findAll(merchantNo);
        List<MemberLevelEntity> memberLevels = memberLevelService.findAllByMerchantNo(merchantNo);
        members.forEach(x -> {
            editLevel(merchantNo, x, memberLevels);
        });
    }

    public void syncMemberLevel(long merchantNo, String memberId) {
        MemberInfoVo member = findById(memberId);
        if (member == null) {
            return;
        }
        List<MemberLevelEntity> memberLevels = memberLevelService.findAllByMerchantNo(merchantNo);
        editLevel(merchantNo, member, memberLevels);
    }

    public void editLevel(long merchantNo, MemberInfoVo member, List<MemberLevelEntity> memberLevels) {
        memberLevels.stream()
                .filter(level -> member.getTotalIntegral() >= level.getStartIntegral() && member.getTotalIntegral() < level.getEndIntegral())
                .findFirst()
                .ifPresent(level -> {
                    memberDao.editLevel(merchantNo, member.getId(), level.getId());
                });
    }

    public int delete(long merchantNo, String id) {
        return memberDao.deleteById(merchantNo, id);
    }

    public boolean existById(long merchantNo, String id) {
        return memberDao.existById(merchantNo, id);
    }

    public boolean existByPhone(long merchantNo, String phone) {
        return memberDao.existByPhone(merchantNo, phone);
    }

    public MemberEntity findBaseById(String id) {
        return memberDao.findById(id);
    }

    public MemberInfoVo findById(String id) {
        MemberEntity entity = memberDao.findById(id);
        if (entity == null) {
            return null;
        }
        return convertMemberInfo(entity);
    }

    public List<MemberInfoVo> findAllByIds(long merchantNo, List<String> ids) {
        List<MemberEntity> members = memberDao.findAllByIds(merchantNo, ids);
        if (members.isEmpty()) {
            return Collections.emptyList();
        }
        return members.stream().map(this::convertMemberInfo).collect(Collectors.toList());
    }

    public List<MemberInfoVo> findAll(long merchantNo) {
        List<MemberEntity> members = memberDao.findAll(merchantNo);
        if (members.isEmpty()) {
            return Collections.emptyList();
        }
        return members.stream().map(this::convertMemberInfo).collect(Collectors.toList());
    }

    public PageVo<MemberInfoVo> page(long merchantNo, MemberQuery query) {
        int total = memberDao.countPage(merchantNo, query);
        if (total <= 0) {
            return null;
        }
        List<MemberEntity> members = memberDao.findPage(merchantNo, query);
        List<MemberInfoVo> vos = convertMemberList(merchantNo, members);
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public List<MemberInfoVo> memberSearch(long merchantNo, String searchKey) {
        List<MemberEntity> members = memberDao.findAllBySearchKey(merchantNo, searchKey, CommonStatus.ENABLED.getCode());
        return convertMemberList(merchantNo, members);
    }

    public List<MemberEntity> summaryList(MemberSummaryQuery query) {
        if (query.getEndDate() != null) {
            query.setEndDate(query.getEndDate().plusDays(1));
        }
        return memberDao.summaryList(query);
    }

    public MemberSummaryCashierVo summaryCashier(long merchantNo, String id) {
        MemberSummaryCashierVo vo = new MemberSummaryCashierVo();
        CashierOrderSummaryQuery orderSummaryQuery = new CashierOrderSummaryQuery();
        orderSummaryQuery.setMerchantNo(merchantNo);
        orderSummaryQuery.setMemberId(id);
        List<CashierOrderEntity> cashierOrders = cashierOrderService.summaryList(orderSummaryQuery);

        CashierRefundSummaryQuery refundSummaryQuery = new CashierRefundSummaryQuery();
        refundSummaryQuery.setMerchantNo(merchantNo);
        refundSummaryQuery.setMemberId(id);
        List<CashierRefundEntity> cashierRefunds = cashierRefundService.summaryList(refundSummaryQuery);

        cashierOrders = cashierOrders.stream()
                .filter(x -> x.getType() == CashierOrderType.AMOUNT.getCode() && x.getCategory() == CashierOrderCategory.GOODS.getCode())
                .collect(Collectors.toList());
        cashierRefunds = cashierRefunds.stream()
                .filter(x -> x.getStatus() == CashierRefundStatus.REFUND_SUBMIT.getCode() || x.getStatus() == CashierRefundStatus.REFUND_FINISH.getCode())
                .filter(x -> x.getType() == CashierOrderType.AMOUNT.getCode() && x.getCategory() == CashierOrderCategory.GOODS.getCode())
                .collect(Collectors.toList());
        long cashierAmount = cashierOrders.stream().mapToLong(x -> x.getRealityAmount() - x.getRefundAmount()).sum();
        long refundAmount = cashierRefunds.stream().mapToLong(CashierRefundEntity::getPayableAmount).sum();

        vo.setCashierAmount(AmountUtil.parseBigDecimal(cashierAmount));
        vo.setCashierTotal(cashierOrders.size());
        vo.setRefundAmount(AmountUtil.parseBigDecimal(refundAmount));
        vo.setRefundTotal(cashierRefunds.size());

        if (CollUtil.isNotEmpty(cashierOrders)) {
            BigDecimal referenceAmount = AmountUtil.divide(vo.getCashierAmount(), BigDecimal.valueOf(vo.getCashierTotal()));
            vo.setReferenceAmount(referenceAmount);
        }
        // 最近下单时间
        cashierOrders.stream().max(Comparator.comparing(CashierOrderEntity::getCashierTime))
                .ifPresent(x -> vo.setLastCashierTime(x.getCashierTime()));
        return vo;
    }

    private List<MemberInfoVo> convertMemberList(long merchantNo, List<MemberEntity> members) {
        List<String> ids = members.stream().map(MemberEntity::getId).collect(Collectors.toList());
        List<String> referenceIds = members.stream().map(MemberEntity::getReferenceId).distinct().collect(Collectors.toList());
        List<MerchantStoreEntity> stores = merchantStoreService.findAllByMerchantNo(merchantNo);
        List<MemberAssetsEntity> assets = memberAssetsService.findListByIds(ids);
        List<MemberLevelEntity> levels = memberLevelService.findAllByMerchantNo(merchantNo);
        List<UserVo> users = accountService.findAllByIds(merchantNo, referenceIds);

        return members.stream().map(entity -> {
            MemberInfoVo vo = new MemberInfoVo();
            BeanUtils.copyProperties(entity, vo);
            stores.stream().filter(x -> x.getStoreNo() == entity.getStoreNo())
                    .findFirst()
                    .ifPresent(x -> vo.setStoreName(x.getStoreName()));

            assets.stream().filter(x -> x.getId().equals(entity.getId()))
                    .findFirst()
                    .ifPresent(memberAssets -> {
                        vo.setNowIntegral(memberAssets.getNowIntegral());
                        vo.setTotalIntegral(memberAssets.getAccrualIntegral());
                        vo.setNowRecharge(AmountUtil.parseBigDecimal(memberAssets.getNowRecharge()));
                        vo.setTotalRecharge(AmountUtil.parseBigDecimal(memberAssets.getAccrualRecharge()));
                    });
            List<MemberTagMapVo> tagMapVos = memberTagMapService.findAllByMemberId(entity.getMerchantNo(), entity.getId());
            vo.setTags(tagMapVos);
            List<MemberTimeCardListVo> timecardList = memberTimeCardService.findAllByMemberId(entity.getId());
            vo.setCounted(timecardList.size());
            levels.stream().filter(x -> x.getId().equals(entity.getLevelId()))
                    .findFirst()
                    .ifPresent(level -> vo.setLevelName(level.getName()));

            CashierOrderEntity cashierOrder = cashierOrderService.findLastByMemberId(entity.getMerchantNo(), entity.getId());
            if (cashierOrder != null) {
                vo.setLastCashierTime(cashierOrder.getCashierTime());
                vo.setLastCashierStoreNo(cashierOrder.getStoreNo());
                vo.setLastCashierStoreName(getStoreName(entity.getMerchantNo(), cashierOrder.getStoreNo()));
            }
            users.stream().filter(user -> user.getId().equals(vo.getReferenceId()))
                    .findFirst()
                    .ifPresent(user -> vo.setReferenceName(user.getName()));
            return vo;
        }).collect(Collectors.toList());
    }

    private MemberInfoVo convertMemberInfo(MemberEntity entity) {
        MemberInfoVo vo = new MemberInfoVo();
        BeanUtils.copyProperties(entity, vo);
        vo.setStoreName(getStoreName(entity.getMerchantNo(), entity.getStoreNo()));
        MemberAssetsEntity memberAssets = memberAssetsService.findById(entity.getId());
        if (memberAssets != null) {
            vo.setNowIntegral(memberAssets.getNowIntegral());
            vo.setTotalIntegral(memberAssets.getAccrualIntegral());
            vo.setNowRecharge(AmountUtil.parseBigDecimal(memberAssets.getNowRecharge()));
            vo.setTotalRecharge(AmountUtil.parseBigDecimal(memberAssets.getAccrualRecharge()));
        }
        List<MemberTagMapVo> tagMapVos = memberTagMapService.findAllByMemberId(entity.getMerchantNo(), entity.getId());
        vo.setTags(tagMapVos);
        List<MemberTimeCardListVo> timecardList = memberTimeCardService.findAllByMemberId(entity.getId());
        vo.setCounted(timecardList.size());

        List<MemberLevelEntity> levels = memberLevelService.findAllByMerchantNo(entity.getMerchantNo());
        levels.stream().filter(x -> x.getId().equals(entity.getLevelId()))
                .findFirst()
                .ifPresent(level -> vo.setLevelName(level.getName()));
        CashierOrderEntity cashierOrder = cashierOrderService.findLastByMemberId(entity.getMerchantNo(), entity.getId());
        if (cashierOrder != null) {
            vo.setLastCashierTime(cashierOrder.getCashierTime());
            vo.setLastCashierStoreNo(cashierOrder.getStoreNo());
            vo.setLastCashierStoreName(getStoreName(entity.getMerchantNo(), cashierOrder.getStoreNo()));
        }
        AccountEntity user = accountService.findById(vo.getReferenceId());
        if (user != null) {
            vo.setReferenceName(user.getName());
        }
        return vo;
    }

    private String getStoreName(long merchantNo, long storeNo) {
        MerchantStoreVo store = merchantStoreService.findByStoreNo(merchantNo, storeNo);
        if (store == null) {
            return null;
        }
        return store.getStoreName();
    }
}
