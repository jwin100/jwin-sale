package com.mammon.member.service;

import com.mammon.common.Generate;
import com.mammon.member.dao.MemberLevelDao;
import com.mammon.member.domain.dto.MemberLevelDto;
import com.mammon.member.domain.entity.MemberLevelEntity;
import com.mammon.member.domain.vo.MemberInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MemberLevelService {

    @Resource
    private MemberLevelDao memberLevelDao;

    @Resource
    private MemberService memberService;

    @Transactional(rollbackFor = Exception.class)
    public void batchEdit(long merchantNo, List<MemberLevelDto> list) {
        memberLevelDao.deleteByMerchantNo(merchantNo);
        list.forEach(x -> {
            save(merchantNo, x);
        });
        // 计算会员等级
        memberService.syncMemberLevel(merchantNo);
    }

    public void save(long merchantNo, MemberLevelDto dto) {
        MemberLevelEntity entity = new MemberLevelEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setName(dto.getName());
        entity.setStartIntegral(dto.getStartIntegral());
        entity.setEndIntegral(dto.getEndIntegral());
        entity.setDiscount(dto.getDiscount());
        entity.setCreateTime(LocalDateTime.now());
        memberLevelDao.save(entity);
    }

    public List<MemberLevelEntity> findAllByMerchantNo(long merchantNo) {
        return memberLevelDao.findAllByMerchantNo(merchantNo);
    }
}
