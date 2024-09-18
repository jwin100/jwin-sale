package com.mammon.sms.service;

import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.sms.dao.SmsSignDao;
import com.mammon.sms.domain.dto.SmsSignDto;
import com.mammon.sms.domain.entity.SmsSignEntity;
import com.mammon.sms.enums.SmsSignStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SmsSignService {

    @Resource
    private SmsSignDao smsSignDao;

    /**
     * 功能开通时默认以商户名创建签名,状态为待审核(因为有的商户名不符合签名规范)
     *
     * @param merchantNo
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public int init(long merchantNo, long storeNo, String accountId, String merchantName) {
        //获取商户信息，商户名作为签名
        if (existsSignName(merchantNo, null, merchantName)) {
            return 1;
        }
        SmsSignEntity entity = new SmsSignEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setSignName(merchantName);
        entity.setDefaultStatus(existsDefaultStatus(merchantNo) ? 0 : 1);
        entity.setStatus(SmsSignStatusEnum.SUCCESS.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return smsSignDao.save(entity);
    }

    //创建签名
    //删除签名
    //签名信息
    public void create(long merchantNo, long storeNo, String accountId, SmsSignDto dto) {
        if (existsSignName(merchantNo, null, dto.getSignName())) {
            throw new CustomException("签名已存在");
        }
        SmsSignEntity entity = new SmsSignEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setSignName(dto.getSignName());
        entity.setDefaultStatus(existsDefaultStatus(merchantNo) ? 0 : 1);
        entity.setStatus(SmsSignStatusEnum.WAITING.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setOperationId(accountId);
        entity.setOperationTime(LocalDateTime.now());
        smsSignDao.save(entity);
    }

    public void edit(long merchantNo, String id, SmsSignDto dto) {
        if (existsSignName(merchantNo, id, dto.getSignName())) {
            throw new CustomException("签名已存在");
        }
        smsSignDao.edit(merchantNo, id, dto.getSignName(), SmsSignStatusEnum.WAITING.getCode());
    }

    public boolean existsDefaultStatus(long merchantNo) {
        List<SmsSignEntity> list = smsSignDao.findAllByMerchantNo(merchantNo, 1);
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean existsSignName(long merchantNo, String id, String signName) {
        List<SmsSignEntity> signs = findAllByMerchantNo(merchantNo);
        if (!signs.isEmpty()) {
            if (StringUtils.isNotBlank(id)) {
                SmsSignEntity validSign = signs.stream().filter(x -> !x.getId().equals(id) && x.getSignName().equals(signName)).findFirst().orElse(null);
                return validSign != null;
            } else {
                SmsSignEntity validSign = signs.stream().filter(x -> x.getSignName().equals(signName)).findFirst().orElse(null);
                return validSign != null;
            }
        }
        return false;
    }

    public SmsSignEntity findById(long merchantNo, String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return smsSignDao.findById(merchantNo, id);
    }

    public List<SmsSignEntity> findAllByMerchantNo(long merchantNo) {
        return smsSignDao.findAllByMerchantNo(merchantNo, null);
    }

    public void editDefaultStatus(long merchantNo, String id) {
        //商户下批量修改成0 然后设置id数据为默认签名
        int editAllDefault = smsSignDao.editDefaultStatus(merchantNo, null, 0);
        if (editAllDefault == 0) {
            throw new CustomException("修改默认签名失败");
        }
        smsSignDao.editDefaultStatus(merchantNo, id, 1);
    }

    public SmsSignEntity findByDefaultStatus(long merchantNo) {
        List<SmsSignEntity> list = smsSignDao.findAllByMerchantNo(merchantNo, 1);
        return list.stream().findFirst().orElse(null);
    }
}
