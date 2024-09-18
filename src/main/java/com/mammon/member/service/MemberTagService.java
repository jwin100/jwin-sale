package com.mammon.member.service;

import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.member.dao.MemberTagDao;
import com.mammon.member.domain.dto.MemberTagDto;
import com.mammon.member.domain.entity.MemberTagEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class MemberTagService {

    @Resource
    private MemberTagDao memberTagDao;

    public MemberTagEntity create(long merchantNo, MemberTagDto dto) {
        MemberTagEntity existsTag = memberTagDao.findByName(merchantNo, dto.getName());
        if (existsTag != null) {
            throw new CustomException("标签已存在");
        }

        MemberTagEntity entity = new MemberTagEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        memberTagDao.save(entity);
        return entity;
    }

    public void edit(long merchantNo, String id, MemberTagDto dto) {
        MemberTagEntity tag = memberTagDao.findById(merchantNo, id);
        if (tag == null) {
            throw new CustomException("标签信息错误");
        }
        MemberTagEntity existsTag = memberTagDao.findByName(merchantNo, dto.getName());
        if (existsTag != null && !existsTag.getId().equals(tag.getId())) {
            throw new CustomException("标签已存在");
        }
        MemberTagEntity entity = new MemberTagEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setMerchantNo(merchantNo);
        entity.setId(id);
        memberTagDao.edit(entity);
    }

    public void delete(long merchantNo, String id) {
        memberTagDao.delete(merchantNo, id);
    }

    public MemberTagEntity findByName(long merchantNo, String name) {
        return memberTagDao.findByName(merchantNo, name);
    }

    public List<MemberTagEntity> findAllByIds(long merchantNo, List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return memberTagDao.findAllByIds(merchantNo, ids);
    }

    public List<MemberTagEntity> findAll(long merchantNo) {
        return memberTagDao.findAll(merchantNo);
    }
}
