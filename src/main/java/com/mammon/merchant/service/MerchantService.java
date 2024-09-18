package com.mammon.merchant.service;

import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.exception.CustomException;
import com.mammon.merchant.dao.MerchantDao;
import com.mammon.merchant.domain.dto.MerchantDto;
import com.mammon.merchant.domain.entity.MerchantEntity;
import com.mammon.merchant.domain.query.MerchantQuery;
import com.mammon.merchant.domain.vo.MerchantIndustryInfoVo;
import com.mammon.merchant.domain.vo.MerchantVo;
import com.mammon.auth.domain.dto.RegisterDto;
import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.service.AccountService;
import com.mammon.clerk.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MerchantService {

    @Resource
    private MerchantDao merchantDao;

    @Resource
    private AccountService accountService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private MerchantIndustryService merchantIndustryService;

    @Resource
    private RoleService roleService;

    public void register(long merchantNo, String accountId, RegisterDto dto) {
        MerchantEntity entity = new MerchantEntity();
        entity.setMerchantNo(merchantNo);
        entity.setMerchantName("我的旺铺");
        entity.setAccountId(accountId);
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setSource(dto.getSource());
        save(entity);
    }

    public void save(MerchantEntity entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        merchantDao.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(long merchantNo, MerchantDto dto) {
        MerchantEntity merchant = merchantDao.findByMerchantNo(merchantNo);
        if (merchant == null) {
            throw new CustomException("商户信息错误");
        }
        merchant.setMerchantName(dto.getMerchantName());
        merchant.setPcLogo(dto.getPcLogo());
        merchant.setMobileLogo(dto.getMobileLogo());
        merchant.setUpdateTime(LocalDateTime.now());
        merchant.setMerchantNo(merchantNo);
        merchantDao.edit(merchant);
    }

    public MerchantEntity findByMerchantNo(long merchantNo) {
        return merchantDao.findByMerchantNo(merchantNo);
    }

    public MerchantVo merchantInfo(long merchantNo) {
        MerchantVo vo = new MerchantVo();
        MerchantEntity merchant = findByMerchantNo(merchantNo);
        if (merchant == null) {
            return null;
        }
        BeanUtils.copyProperties(merchant, vo);
        AccountEntity account = accountService.findById(vo.getAccountId());
        if (account != null) {
            vo.setAccountName(account.getName());
            vo.setAccountPhone(account.getPhone());
        }
        MerchantIndustryInfoVo industryMerchant = merchantIndustryService.industryInfo(merchantNo);
        if (industryMerchant != null) {
            vo.setIndustryId(industryMerchant.getIndustryId());
            vo.setIndustryName(industryMerchant.getIndustryName());
            vo.setExpireDate(industryMerchant.getExpireDate());
            vo.setIndustryType(industryMerchant.getType());
        }
        return vo;
    }

    public PageVo<MerchantEntity> page(MerchantQuery query) {
        int total = merchantDao.countPage(query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<MerchantEntity> list = merchantDao.findPage(query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, list);
    }
}
