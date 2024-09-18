package com.mammon.member.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.member.dao.MemberTimeCardLogDao;
import com.mammon.member.domain.dto.MemberTimeCardLogQuery;
import com.mammon.member.domain.entity.MemberTimeCardLogEntity;
import com.mammon.member.domain.vo.MemberTimeCardLogPageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberTimeCardLogService {

    @Resource
    private MemberTimeCardLogDao memberTimeCardLogDao;

    public int create(String accountId, String memberId, String orderNo, int changeType,
                      long timeTotal, long changeAfter, String remark) {
        MemberTimeCardLogEntity entity = new MemberTimeCardLogEntity();
        entity.setId(Generate.generateUUID());
        entity.setMemberId(memberId);
        entity.setChangeType(changeType);
        entity.setChangeTotal(timeTotal);
        entity.setChangeAfter(changeAfter);
        entity.setRemark(remark);
        entity.setOrderNo(orderNo);
        entity.setAccountId(accountId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return memberTimeCardLogDao.save(entity);
    }

    public List<MemberTimeCardLogEntity> findAllByMemberId(String memberId) {
        return memberTimeCardLogDao.findAllByMemberId(memberId);
    }

    public PageVo<MemberTimeCardLogPageVo> page(MemberTimeCardLogQuery dto) {
        int total = memberTimeCardLogDao.countPage(dto);
        if (total <= 0) {
            return null;
        }
        List<MemberTimeCardLogEntity> members = memberTimeCardLogDao.findPage(dto);
        List<MemberTimeCardLogPageVo> vos = members.stream().map(x -> {
            MemberTimeCardLogPageVo vo = new MemberTimeCardLogPageVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(dto.getPageIndex(), dto.getPageSize(), total, vos);
    }
}
