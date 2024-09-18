package com.mammon.clerk.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.exception.CustomException;
import com.mammon.clerk.dao.CommissionRuleDao;
import com.mammon.clerk.domain.dto.CommissionRuleDto;
import com.mammon.clerk.domain.entity.CommissionRuleEntity;
import com.mammon.clerk.domain.query.CommissionRuleQuery;
import com.mammon.clerk.domain.vo.CommissionRuleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/4/7 16:49
 */
@Service
public class CommissionRuleService {

    @Resource
    private CommissionRuleDao commissionRuleDao;

    public void create(long merchantNo, CommissionRuleDto dto) {
        CommissionRuleEntity entity = new CommissionRuleEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        commissionRuleDao.save(entity);
    }

    public void edit(String id, CommissionRuleDto dto) {
        CommissionRuleEntity entity = new CommissionRuleEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUpdateTime(LocalDateTime.now());
        commissionRuleDao.edit(entity);
    }

    public void editStatus(String id, int status) {
        if (IEnum.getByCode(status, CommonStatus.class) == null) {
            throw new CustomException("状态信息错误");
        }
        commissionRuleDao.editStatus(id, status);
    }

    public void delete(String id) {
        commissionRuleDao.delete(id);
    }

    public CommissionRuleVo findById(String id) {
        CommissionRuleEntity entity = commissionRuleDao.findById(id);
        if (entity == null) {
            return null;
        }
        CommissionRuleVo vo = new CommissionRuleVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    public List<CommissionRuleVo> list(long merchantNo, CommissionRuleQuery query) {
        List<CommissionRuleEntity> list = commissionRuleDao.findList(merchantNo, query);
        return list.stream().map(x -> {
            CommissionRuleVo vo = new CommissionRuleVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public PageVo<CommissionRuleVo> page(long merchantNo, CommissionRuleQuery query) {
        int total = commissionRuleDao.countPage(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<CommissionRuleEntity> list = commissionRuleDao.findPage(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<CommissionRuleVo> vos = list.stream().map(x -> {
            CommissionRuleVo vo = new CommissionRuleVo();
            BeanUtils.copyProperties(x, vo);
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }
}
