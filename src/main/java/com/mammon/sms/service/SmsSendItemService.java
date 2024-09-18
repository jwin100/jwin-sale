package com.mammon.sms.service;

import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.member.domain.vo.MemberInfoVo;
import com.mammon.member.service.MemberService;
import com.mammon.sms.dao.SmsSendItemDao;
import com.mammon.sms.domain.dto.SmsSendUserDto;
import com.mammon.sms.domain.entity.SmsSendItemEntity;
import com.mammon.sms.domain.vo.SmsSendItemPageVo;
import com.mammon.sms.enums.SmsSendItemStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SmsSendItemService {

    @Resource
    private SmsSendItemDao smsSendItemDao;

    @Resource
    private MemberService memberService;

    @Transactional(rollbackFor = Exception.class)
    public void batchSave(String sendId, List<SmsSendUserDto> users, String content) {
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        List<SmsSendItemEntity> items = users.stream().map(x -> {
            SmsSendItemEntity entity = new SmsSendItemEntity();
            entity.setSendId(sendId);
            entity.setId(Generate.generateUUID());
            entity.setPhone(x.getPhone());
            entity.setUserId(x.getUserId());
            entity.setContent(content);
            entity.setStatus(SmsSendItemStatusEnum.WAITING.getCode());
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            return entity;
        }).collect(Collectors.toList());
        smsSendItemDao.batchSave(items);
    }

    public int updateStatus(String smsSendId, String phone, int beforeStatus, int afterStatus, String errorDesc) {
        return smsSendItemDao.updateStatus(smsSendId, phone, beforeStatus, afterStatus, errorDesc);
    }

    public List<SmsSendItemEntity> findAllBySmsSendId(String smsSendId) {
        return smsSendItemDao.findAllBySmsSendId(smsSendId);
    }

    public List<SmsSendItemPageVo> findAllBySmsSendId(long merchantNo, long storeNo, String accountId, String id) {
        List<SmsSendItemEntity> items = smsSendItemDao.findAllBySmsSendId(id);
        if (items.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> userIds = items.stream().map(SmsSendItemEntity::getUserId)
                .filter(StringUtils::isNotBlank).distinct()
                .collect(Collectors.toList());
        List<MemberInfoVo> members = new ArrayList<>();
        if (!userIds.isEmpty()) {
            members = memberService.findAllByIds(merchantNo, userIds);
        }
        List<MemberInfoVo> finalMembers = members;
        return items.stream().map(x -> {
            SmsSendItemPageVo vo = new SmsSendItemPageVo();
            BeanUtils.copyProperties(x, vo);
            MemberInfoVo member = finalMembers.stream()
                    .filter(y -> y.getId().equals(x.getUserId()))
                    .findFirst().orElse(null);
            if (member != null) {
                vo.setUserName(member.getName());
            }
            return vo;
        }).collect(Collectors.toList());
    }
}
