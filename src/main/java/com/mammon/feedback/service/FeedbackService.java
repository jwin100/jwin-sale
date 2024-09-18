package com.mammon.feedback.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.feedback.dao.FeedbackDao;
import com.mammon.feedback.domain.dto.FeedbackDto;
import com.mammon.feedback.domain.entity.FeedbackEntity;
import com.mammon.feedback.domain.enums.FeedbackContactType;
import com.mammon.feedback.domain.enums.FeedbackStatus;
import com.mammon.feedback.domain.query.FeedbackPageQuery;
import com.mammon.feedback.domain.vo.FeedbackVo;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.service.AccountService;
import com.mammon.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2022-11-02 14:52:02
 */
@Service
public class FeedbackService {

    @Resource
    private FeedbackDao feedbackDao;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private AccountService accountService;

    public void save(long merchantNo, long storeNo, String accountId, FeedbackDto dto) {
        FeedbackEntity entity = new FeedbackEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setAccountId(accountId);
        entity.setStatus(FeedbackStatus.HANDLE_WAIT.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (!CollectionUtils.isEmpty(dto.getImages())) {
            entity.setImages(JsonUtil.toJSONString(dto.getImages()));
        }
        if (StringUtils.isBlank(dto.getContactNo())) {
            AccountEntity account = accountService.findById(accountId);
            if (account != null) {
                entity.setContactType(FeedbackContactType.PHONE.getCode());
                entity.setContactNo(account.getPhone());
            }
        }
        feedbackDao.save(entity);
    }

    public void update(String id, FeedbackDto dto) {
        FeedbackEntity entity = new FeedbackEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        if (!CollectionUtils.isEmpty(dto.getImages())) {
            entity.setImages(JsonUtil.toJSONString(dto.getImages()));
        }
        feedbackDao.update(entity);
    }

    public FeedbackVo findById(String id) {
        FeedbackEntity entity = feedbackDao.findById(id);
        if (entity == null) {
            return null;
        }
        FeedbackVo vo = new FeedbackVo();
        BeanUtils.copyProperties(entity, vo);
        if (StringUtils.isNotBlank(entity.getImages())) {
            vo.setImages(JsonUtil.toList(entity.getImages(), String.class));
        }
        MerchantStoreVo store = merchantStoreService.findByStoreNo(entity.getMerchantNo(), entity.getStoreNo());
        if (store != null) {
            vo.setStoreName(store.getStoreName());
        }
        return vo;
    }

    public PageVo<FeedbackVo> page(long merchantNo, long storeNo, String accountId, FeedbackPageQuery query) {
        int total = feedbackDao.countPage(merchantNo, storeNo, accountId, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<FeedbackEntity> list = feedbackDao.findPage(merchantNo, storeNo, accountId, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<Long> storeNos = list.stream().map(FeedbackEntity::getStoreNo).distinct().collect(Collectors.toList());
        List<MerchantStoreVo> stores = merchantStoreService.findAllByStoreNos(merchantNo, storeNos);
        List<FeedbackVo> vos = list.stream().map(x -> {
            FeedbackVo vo = new FeedbackVo();
            BeanUtils.copyProperties(x, vo);
            if (StringUtils.isNotBlank(x.getImages())) {
                vo.setImages(JsonUtil.toList(x.getImages(), String.class));
            }
            stores.stream().filter(store -> store.getStoreNo() == x.getStoreNo())
                    .findFirst()
                    .ifPresent(store -> vo.setStoreName(store.getStoreName()));
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }
}
