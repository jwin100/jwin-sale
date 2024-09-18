package com.mammon.member.service;

import com.mammon.common.Generate;
import com.mammon.member.dao.MemberTagMapDao;
import com.mammon.member.domain.entity.MemberTagEntity;
import com.mammon.member.domain.entity.MemberTagMapEntity;
import com.mammon.member.domain.vo.MemberTagMapVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MemberTagMapService {

    @Resource
    private MemberTagMapDao memberTagMapDao;

    @Resource
    private MemberTagService memberTagService;

    @Transactional(rollbackFor = Exception.class)
    public void batchSave(String memberId, List<String> tagIds) {
        deleteByMemberId(memberId);
        tagIds.forEach(x -> {
            MemberTagMapEntity entity = new MemberTagMapEntity();
            entity.setId(Generate.generateUUID());
            entity.setMemberId(memberId);
            entity.setTagId(x);
            entity.setCreateTime(LocalDateTime.now());
            memberTagMapDao.save(entity);
        });
    }

    public void deleteByMemberId(String memberId) {
        memberTagMapDao.deleteByMemberId(memberId);
    }

    public void deleteByTagId(String memberId, String tagId) {
        memberTagMapDao.deleteByTagId(memberId, tagId);
    }

    public List<MemberTagMapVo> findAllByMemberId(long merchantNo, String memberId) {
        List<MemberTagMapEntity> tagMaps = memberTagMapDao.findAllByMemberId(memberId);
        if (CollectionUtils.isEmpty(tagMaps)) {
            return Collections.emptyList();
        }
        List<String> tagIds = tagMaps.stream().map(MemberTagMapEntity::getTagId).collect(Collectors.toList());
        List<MemberTagEntity> memberTags = memberTagService.findAllByIds(merchantNo, tagIds);
        return tagMaps.stream().map(x -> {
            MemberTagEntity memberTag = memberTags.stream()
                    .filter(y -> y.getId().equals(x.getTagId()))
                    .findFirst().orElse(null);
            if (memberTag == null) {
                return null;
            }
            MemberTagMapVo vo = new MemberTagMapVo();
            vo.setId(memberTag.getId());
            vo.setName(memberTag.getName());
            vo.setColor(memberTag.getColor());
            return vo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
