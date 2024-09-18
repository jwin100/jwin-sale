package com.mammon.merchant.service;

import com.mammon.common.Generate;
import com.mammon.merchant.dao.MerchantIndustryDao;
import com.mammon.office.edition.domain.dto.IndustryMerchantActiveDto;
import com.mammon.office.edition.domain.dto.IndustryMerchantCallbackDto;
import com.mammon.office.edition.domain.entity.IndustryAttrEntity;
import com.mammon.office.edition.domain.entity.IndustryEntity;
import com.mammon.merchant.domain.entity.MerchantIndustryEntity;
import com.mammon.office.edition.domain.vo.IndustryActiveVo;
import com.mammon.merchant.domain.vo.MerchantIndustryInfoVo;
import com.mammon.office.edition.service.IndustryAttrService;
import com.mammon.office.edition.service.IndustryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2023-02-02 11:40:56
 */
@Service
public class MerchantIndustryService {

    @Resource
    private IndustryService industryService;

    @Resource
    private MerchantIndustryDao merchantIndustryDao;

    @Resource
    private MerchantIndustryLogService merchantIndustryLogService;


    @Resource
    private IndustryAttrService industryAttrService;

    @Transactional(rollbackFor = Exception.class)
    public IndustryActiveVo industryActive(IndustryMerchantActiveDto dto) {
        //版本开通时，判断是否有开通版本，如果没有开通则新开通
        //如果已到期，则按当前日期开始开通
        //如果未到期，则按实际到期日期开始开通
        IndustryActiveVo vo = new IndustryActiveVo();
        MerchantIndustryEntity industryMerchant = findByMerchantNo(dto.getMerchantNo());
        if (industryMerchant == null) {
            save(dto.getMerchantNo(), dto.getIndustryId(), dto.getIndustryType(), dto.getAddMonth());
        } else {
            industryActive(industryMerchant.getId(), dto.getIndustryId(), dto.getIndustryType(),
                    industryMerchant.getExpireDate(), dto.getAddMonth());
        }
        merchantIndustryLogService.save(dto.getMerchantNo(), dto.getOrderId(), dto.getIndustryId(),
                dto.getIndustryType(), dto.getAddMonth());
        vo.setStatus(1);
        vo.setActiveTime(LocalDateTime.now());
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public IndustryActiveVo industryCallback(IndustryMerchantCallbackDto dto) {
        IndustryActiveVo vo = new IndustryActiveVo();
        MerchantIndustryEntity industryMerchant = findByIndustryId(dto.getIndustryId());
        if (industryMerchant != null) {
            industryCallback(industryMerchant.getId(), dto.getIndustryId(), industryMerchant.getType(),
                    industryMerchant.getExpireDate(), dto.getCallbackMonth());
            merchantIndustryLogService.save(dto.getMerchantNo(), dto.getOrderId(), dto.getIndustryId(),
                    industryMerchant.getType(), dto.getCallbackMonth());
        }
        vo.setStatus(1);
        vo.setActiveTime(LocalDateTime.now());
        return vo;
    }

    public int industryActive(String id, String industryId, int industryType, LocalDate expireDate, long addMonth) {
        LocalDate now = LocalDate.now();
        LocalDate endDate = expireDate;
        if (expireDate.isBefore(now)) {
            endDate = now.plusMonths(addMonth);
        } else {
            endDate = endDate.plusMonths(addMonth);
        }
        return merchantIndustryDao.editByExpireDate(id, industryId, industryType, endDate);
    }

    public int industryCallback(String id, String industryId, int industryType, LocalDate expireDate, long callbackMonth) {
        LocalDate endDate = expireDate.plusMonths(callbackMonth);
        return merchantIndustryDao.editByExpireDate(id, industryId, industryType, endDate);
    }

    public int save(long merchantNo, String industryId, int industryType, long addMonth) {
        LocalDate now = LocalDate.now();
        LocalDate expireDate = now.plusMonths(addMonth);
        MerchantIndustryEntity entity = new MerchantIndustryEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setIndustryId(industryId);
        entity.setExpireDate(expireDate);
        entity.setType(industryType);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return merchantIndustryDao.save(entity);
    }

    public MerchantIndustryEntity findByIndustryId(String industryId) {
        return merchantIndustryDao.industryId(industryId);
    }

    public MerchantIndustryEntity findByMerchantNo(long merchantNo) {
        return merchantIndustryDao.findByMerchantNo(merchantNo);
    }

    public MerchantIndustryInfoVo industryInfo(long merchantNo) {
        MerchantIndustryInfoVo vo = new MerchantIndustryInfoVo();
        MerchantIndustryEntity industryMerchant = merchantIndustryDao.findByMerchantNo(merchantNo);
        if (industryMerchant == null) {
            return null;
        }
        BeanUtils.copyProperties(industryMerchant, vo);
        IndustryEntity industry = industryService.findById(vo.getIndustryId());
        if (industry != null) {
            vo.setIndustryName(industry.getName());
        }
        return vo;
    }

    public List<IndustryAttrEntity> merchantChargeAbility(long merchantNo) {
        LocalDate now = LocalDate.now();
        MerchantIndustryInfoVo industryMerchantInfo = industryInfo(merchantNo);
        if (industryMerchantInfo == null || industryMerchantInfo.getExpireDate().isBefore(now)) {
            List<IndustryAttrEntity> attrs = industryAttrService.findAll();
            return attrs.stream().filter(IndustryAttrEntity::isFee).collect(Collectors.toList());
        }
        return industryAttrService.findAllByIndustryId(industryMerchantInfo.getIndustryId());
    }
}
