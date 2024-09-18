package com.mammon.market.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.exception.CustomException;
import com.mammon.market.dao.MarketRechargeRuleDao;
import com.mammon.market.domain.dto.MarketRechargeRuleDto;
import com.mammon.market.domain.entity.MarketRechargeRuleEntity;
import com.mammon.market.domain.query.MarketRechargeRuleQuery;
import com.mammon.market.domain.vo.MarketRechargeRuleVo;
import com.mammon.utils.AmountUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketRechargeRuleService {

    @Resource
    private MarketRechargeRuleDao marketRechargeRuleDao;

    public void create(long merchantNo, long storeNo, String accountId, MarketRechargeRuleDto dto) {

        MarketRechargeRuleEntity entity = new MarketRechargeRuleEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setRealAmount(AmountUtil.parse(dto.getRealAmount()));
        entity.setGiveAmount(AmountUtil.parse(dto.getGiveAmount()));
        entity.setPrepaidAmount(entity.getRealAmount() + entity.getGiveAmount());
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setRemark(dto.getRemark());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        marketRechargeRuleDao.save(entity);
    }

    public void edit(long merchantNo, long storeNo, String accountId, String id, MarketRechargeRuleDto dto) {
        MarketRechargeRuleEntity entity = marketRechargeRuleDao.findById(id);
        if (entity == null) {
            return;
        }
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUpdateTime(LocalDateTime.now());
        entity.setRealAmount(AmountUtil.parse(dto.getRealAmount()));
        entity.setGiveAmount(AmountUtil.parse(dto.getGiveAmount()));
        entity.setPrepaidAmount(entity.getRealAmount() + entity.getGiveAmount());
        marketRechargeRuleDao.edit(entity);
    }

    public void editStatus(long merchantNo, long storeNo, String accountId, String id, int status) {
        if (status != CommonStatus.ENABLED.getCode() && status != CommonStatus.DISABLED.getCode()) {
            throw new CustomException("设置状态不正确");
        }
        marketRechargeRuleDao.editStatus(merchantNo, id, status);
    }

    public void deleted(long merchantNo, String id) {
        marketRechargeRuleDao.delete(id, merchantNo);
    }

    public MarketRechargeRuleVo findById(String id) {
        MarketRechargeRuleEntity entity = marketRechargeRuleDao.findById(id);
        if (entity == null) {
            return null;
        }
        MarketRechargeRuleVo vo = new MarketRechargeRuleVo();
        BeanUtils.copyProperties(entity, vo);
        vo.setPrepaidAmount(AmountUtil.parseBigDecimal(entity.getPrepaidAmount()));
        vo.setRealAmount(AmountUtil.parseBigDecimal(entity.getRealAmount()));
        vo.setGiveAmount(AmountUtil.parseBigDecimal(entity.getGiveAmount()));
        return vo;
    }

    public PageVo<MarketRechargeRuleVo> page(long merchantNo, long storeNo, String accountId,
                                             MarketRechargeRuleQuery query) {
        query.setDeleted(0);
        int total = marketRechargeRuleDao.countPage(merchantNo, query);
        if (total <= 0) {
            return null;
        }
        List<MarketRechargeRuleEntity> list = marketRechargeRuleDao.findPage(merchantNo, query);
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, convertList(list));
    }

    public List<MarketRechargeRuleVo> findAll(long merchantNo, long storeNo, String accountId) {
        List<MarketRechargeRuleEntity> list = marketRechargeRuleDao.findAll(merchantNo);
        list = list.stream().filter(x -> x.getStatus() == CommonStatus.ENABLED.getCode()).collect(Collectors.toList());
        return convertList(list);
    }

    private List<MarketRechargeRuleVo> convertList(List<MarketRechargeRuleEntity> list) {
        return list.stream().map(entity -> {
            MarketRechargeRuleVo vo = new MarketRechargeRuleVo();
            BeanUtils.copyProperties(entity, vo);
            vo.setPrepaidAmount(AmountUtil.parseBigDecimal(entity.getPrepaidAmount()));
            vo.setRealAmount(AmountUtil.parseBigDecimal(entity.getRealAmount()));
            vo.setGiveAmount(AmountUtil.parseBigDecimal(entity.getGiveAmount()));
            return vo;
        }).collect(Collectors.toList());
    }
}
