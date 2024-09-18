package com.mammon.member.service;

import com.mammon.common.Generate;
import com.mammon.member.dao.MemberAttrDao;
import com.mammon.member.domain.dto.MemberAttrDto;
import com.mammon.member.domain.entity.MemberAttrEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class MemberAttrService {

    @Resource
    private MemberAttrDao memberAttrDao;

    public void save(long merchantNo, MemberAttrDto dto) {
        MemberAttrEntity attr = new MemberAttrEntity();
        attr.setId(Generate.generateUUID());
        attr.setMerchantNo(merchantNo);
        attr.setNewReward(dto.getNewReward());
        attr.setRewardModel(dto.getRewardModel());
        attr.setRewardNumber(dto.getRewardNumber());
        attr.setConvertAmount(dto.getConvertAmount());
        attr.setConvertIntegral(dto.getConvertIntegral());
        memberAttrDao.save(attr);
    }

    public void edit(long merchantNo, MemberAttrDto dto) {
        MemberAttrEntity attr = findByMerchantNo(merchantNo);
        if (attr != null) {
            attr.setNewReward(dto.getNewReward());
            attr.setRewardModel(dto.getRewardModel());
            attr.setRewardNumber(dto.getRewardNumber());
            attr.setConvertAmount(dto.getConvertAmount());
            attr.setConvertIntegral(dto.getConvertIntegral());
            memberAttrDao.edit(attr);
        } else {
            save(merchantNo, dto);
        }
    }

    public MemberAttrEntity findByMerchantNo(long merchantNo) {
        return memberAttrDao.findByMerchantNo(merchantNo);
    }
}
