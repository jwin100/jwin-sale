package com.mammon.sms.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.exception.CustomException;
import com.mammon.sms.dao.SmsTemplateDao;
import com.mammon.sms.domain.dto.SmsTempDto;
import com.mammon.sms.domain.dto.SmsTempQuery;
import com.mammon.sms.domain.entity.SmsTemplateEntity;
import com.mammon.sms.enums.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SmsTemplateService {

    @Resource
    private SmsTemplateDao smsTemplateDao;

    public void create(long merchantNo, SmsTempDto dto) {
        SmsTempTypeEnum tempType = IEnum.getByCode(dto.getTempType(), SmsTempTypeEnum.class);
        if (tempType == null) {
            throw new CustomException("模板类型错误");
        }

        SmsTemplateEntity entity = new SmsTemplateEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setTempName(dto.getTempName());
        entity.setTempGroup(SmsTempGroupEnum.MERCHANT_SMS.getCode());
        entity.setTempType(tempType.getClassify());
        entity.setSmsType(dto.getTempType());
        entity.setTemplate(dto.getTemplate());
        entity.setDefaultStatus(CommonStatus.DISABLED.getCode());
        entity.setStatus(SmsTempStatusEnum.WAITING.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        smsTemplateDao.save(entity);
    }

    public void edit(long merchantNo, String id, SmsTempDto dto) {
        SmsTemplateEntity entity = findById(merchantNo, id);
        if (entity == null) {
            throw new CustomException("模板信息不存在");
        }
        smsTemplateDao.edit(merchantNo, id, dto.getTempName(), dto.getTemplate(), SmsTempStatusEnum.WAITING.getCode());
    }

    public SmsTemplateEntity findById(long merchantNo, String id) {
        return smsTemplateDao.findById(merchantNo, id);
    }

    public List<SmsTemplateEntity> findAllByIds(long merchantNo, List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return smsTemplateDao.findAllByIds(merchantNo, ids);
    }

    /**
     * 获取系统默认设定模板信息
     *
     * @param merchantNo
     * @return
     */
    public List<SmsTemplateEntity> findAllDefaultStatus(long merchantNo) {
        List<SmsTemplateEntity> list = smsTemplateDao.findAllBySmsType(merchantNo, SmsTypeEnum.NOTICE.getCode());
        return list.stream().filter(x -> x.getDefaultStatus() == CommonStatus.ENABLED.getCode()).collect(Collectors.toList());
    }

    public List<SmsTemplateEntity> findAllByTempType(long merchantNo, int tempType) {
        return smsTemplateDao.findAllByTempType(merchantNo, tempType);
    }

    public List<SmsTemplateEntity> findAll(long merchantNo) {
        return smsTemplateDao.findAll(merchantNo);
    }

    public PageVo<SmsTemplateEntity> page(long merchantNo, long storeNo, String accountId, SmsTempQuery dto) {
        dto.setTempGroup(SmsTempGroupEnum.MERCHANT_SMS.getCode());
        int total = smsTemplateDao.countPage(merchantNo, storeNo, accountId, dto);
        if (total <= 0) {
            return null;
        }
        List<SmsTemplateEntity> list = smsTemplateDao.findPage(merchantNo, storeNo, accountId, dto);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        return PageResult.of(dto.getPageIndex(), dto.getPageSize(), total, list);
    }
}