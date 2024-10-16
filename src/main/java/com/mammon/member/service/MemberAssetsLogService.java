package com.mammon.member.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.member.dao.MemberAssetsLogDao;
import com.mammon.member.domain.dto.MemberAssetsLogDto;
import com.mammon.member.domain.query.MemberAssetsLogQuery;
import com.mammon.member.domain.entity.MemberAssetsLogEntity;
import com.mammon.member.domain.entity.MemberEntity;
import com.mammon.member.domain.vo.MemberAssetsLogVo;
import com.mammon.member.domain.vo.MemberInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2023-04-04 11:32:41
 */
@Service
public class MemberAssetsLogService {

    @Resource
    private MemberAssetsLogDao memberAssetsLogDao;

    @Resource
    private MemberService memberService;

    public void create(MemberAssetsLogDto dto) {
        MemberAssetsLogEntity entity = new MemberAssetsLogEntity();
        entity.setId(Generate.generateUUID());
        entity.setMemberId(dto.getMemberId());
        entity.setOrderNo(dto.getOrderNo());
        entity.setType(dto.getType());
        entity.setCategory(dto.getCategory());
        entity.setBeforeAssets(dto.getBeforeAssets());
        entity.setChangeAssets(dto.getChangeAssets());
        entity.setAfterAssets(dto.getAfterAssets());
        entity.setRemark(dto.getRemark());
        entity.setCreateTime(LocalDateTime.now());
        memberAssetsLogDao.save(entity);
    }

    public List<MemberAssetsLogEntity> findAllByOrderNo(String memberId, String orderNo) {
        return memberAssetsLogDao.findAllByOrderNo(memberId, orderNo);
    }

    public PageVo<MemberAssetsLogVo> page(MemberAssetsLogQuery query) {
        int total = memberAssetsLogDao.countPage(query);
        if (total <= 0) {
            return null;
        }
        List<MemberAssetsLogEntity> assetsLogs = memberAssetsLogDao.findPage(query);
        List<MemberAssetsLogVo> vos = assetsLogs.stream().map(x -> {
            MemberAssetsLogVo logVo = new MemberAssetsLogVo();
            BeanUtils.copyProperties(x, logVo);
            MemberInfoVo member = memberService.findById(x.getMemberId());
            if (member != null) {
                logVo.setMemberName(member.getName());
            }
            return logVo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }
}
