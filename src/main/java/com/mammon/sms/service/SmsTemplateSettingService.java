package com.mammon.sms.service;

import com.mammon.common.Generate;
import com.mammon.enums.CommonStatus;
import com.mammon.sms.dao.SmsTemplateSettingDao;
import com.mammon.sms.domain.entity.SmsTemplateEntity;
import com.mammon.sms.domain.entity.SmsTemplateSettingEntity;
import com.mammon.sms.domain.vo.SmsTemplateSettingVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/5/9 13:46
 */
@Service
public class SmsTemplateSettingService {

    @Resource
    private SmsTemplateService smsTemplateService;

    @Resource
    private SmsTemplateSettingDao smsTemplateSettingDao;


    @Transactional(rollbackFor = Exception.class)
    public List<SmsTemplateSettingEntity> init(long merchantNo) {
        List<SmsTemplateEntity> list = smsTemplateService.findAllDefaultStatus(merchantNo);
        deleteAll(merchantNo);
        return list.stream()
                .map(x -> save(merchantNo, x.getTempType(), x.getId(), CommonStatus.ENABLED.getCode()))
                .collect(Collectors.toList());
    }

    public SmsTemplateSettingEntity save(long merchantNo, int tempType, String tempId, int status) {
        SmsTemplateSettingEntity entity = new SmsTemplateSettingEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setTempType(tempType);
        entity.setTempId(tempId);
        entity.setStatus(status);
        smsTemplateSettingDao.save(entity);
        return entity;
    }

    public int editTempId(long merchantNo, int tempType, String tempId) {
        SmsTemplateSettingEntity entity = new SmsTemplateSettingEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setTempType(tempType);
        entity.setTempId(tempId);
        return smsTemplateSettingDao.editTempId(entity);
    }

    public int editTempStatus(long merchantNo, int tempType, int status) {
        SmsTemplateSettingEntity entity = new SmsTemplateSettingEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setTempType(tempType);
        entity.setStatus(status);
        return smsTemplateSettingDao.editStatus(entity);
    }

    public void deleteAll(long merchantNo) {
        smsTemplateSettingDao.deleteAll(merchantNo);
    }

    public boolean isSmsSend(long merchantNo, int tempType) {
        SmsTemplateSettingEntity setting = smsTemplateSettingDao.findByTempType(merchantNo, tempType);
        if (setting == null) {
            return false;
        }
        return setting.getStatus() == CommonStatus.ENABLED.getCode();
    }

    /**
     * 获取默认发送的短信模板
     *
     * @param merchantNo
     * @param tempType
     * @return
     */
    public SmsTemplateEntity findByTempType(long merchantNo, int tempType) {
        SmsTemplateSettingEntity setting = smsTemplateSettingDao.findByTempType(merchantNo, tempType);
        if (setting == null) {
            return null;
        }
        return smsTemplateService.findById(merchantNo, setting.getTempId());
    }

    @Transactional(rollbackFor = Exception.class)
    public List<SmsTemplateSettingVo> list(long merchantNo) {
        List<SmsTemplateSettingEntity> settings = smsTemplateSettingDao.findAll(merchantNo);
        if (CollectionUtils.isEmpty(settings)) {
            settings = init(merchantNo);
        }
        List<SmsTemplateEntity> templates = smsTemplateService.findAll(merchantNo);
        return settings.stream().map(x -> {
            SmsTemplateEntity template = templates.stream().filter(y -> y.getId().equals(x.getTempId())).findFirst().orElse(null);
            if (template == null) {
                return null;
            }
            SmsTemplateSettingVo vo = new SmsTemplateSettingVo();
            BeanUtils.copyProperties(template, vo);
            vo.setId(x.getId());
            vo.setTempId(template.getId());
            vo.setStatus(x.getStatus());
            return vo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
