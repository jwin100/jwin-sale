package com.mammon.market.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.exception.CustomException;
import com.mammon.market.dao.MarketTimeCardRuleDao;
import com.mammon.market.domain.dto.MarketTimeCardRuleDto;
import com.mammon.market.domain.entity.MarketTimeCardRuleEntity;
import com.mammon.market.domain.query.MarketTimeCardRuleQuery;
import com.mammon.market.domain.vo.MarketTimeCardRuleListVo;
import com.mammon.market.domain.vo.MarketTimeCardRuleVo;
import com.mammon.market.domain.vo.MarketTimeCardSpuVo;
import com.mammon.stock.domain.query.StockSpuQuery;
import com.mammon.stock.domain.vo.StockSpuListVo;
import com.mammon.stock.service.StockSpuService;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketTimeCardRuleService {

    @Resource
    private MarketTimeCardRuleDao marketTimeCardRuleDao;

    @Resource
    private StockSpuService stockSpuService;

    public void create(long merchantNo, long storeNo, String accountId, MarketTimeCardRuleDto dto) {
        MarketTimeCardRuleEntity entity = new MarketTimeCardRuleEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setAccountId(accountId);
        entity.setRealAmount(AmountUtil.parse(dto.getRealAmount()));
        entity.setSpuIds(JsonUtil.toJSONString(dto.getSpuIds()));
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        marketTimeCardRuleDao.save(entity);
    }

    public void edit(long merchantNo, long storeNo, String accountId, String id, MarketTimeCardRuleDto dto) {
        MarketTimeCardRuleEntity entity = marketTimeCardRuleDao.findById(id);
        if (entity == null) {
            return;
        }
        BeanUtils.copyProperties(dto, entity);
        entity.setSpuIds(JsonUtil.toJSONString(dto.getSpuIds()));
        entity.setUpdateTime(LocalDateTime.now());
        entity.setRealAmount(AmountUtil.parse(dto.getRealAmount()));
        marketTimeCardRuleDao.edit(entity);
    }

    public void editStatus(long merchantNo, long storeNo, String accountId, String id, int status) {
        if (IEnum.getByCode(status, CommonStatus.class) == null) {
            throw new CustomException("设置状态不正确");
        }
        marketTimeCardRuleDao.editStatus(merchantNo, id, status);
    }

    public void deleted(long merchantNo, String id) {
        marketTimeCardRuleDao.delete(id, merchantNo);
    }

    public MarketTimeCardRuleVo findBaseById(String id) {
        MarketTimeCardRuleEntity rule = marketTimeCardRuleDao.findById(id);
        if (rule == null) {
            return null;
        }
        MarketTimeCardRuleVo vo = new MarketTimeCardRuleVo();
        BeanUtils.copyProperties(rule, vo);
        vo.setRealAmount(AmountUtil.parseBigDecimal(rule.getRealAmount()));
        vo.setSpuIds(JsonUtil.toList(rule.getSpuIds(), String.class));
        return vo;
    }

    public MarketTimeCardRuleVo findById(String id) {
        MarketTimeCardRuleEntity rule = marketTimeCardRuleDao.findById(id);
        if (rule == null) {
            return null;
        }
        MarketTimeCardRuleVo vo = new MarketTimeCardRuleVo();
        BeanUtils.copyProperties(rule, vo);
        vo.setRealAmount(AmountUtil.parseBigDecimal(rule.getRealAmount()));
        vo.setSpuIds(JsonUtil.toList(rule.getSpuIds(), String.class));
        StockSpuQuery query = new StockSpuQuery();
        query.setSpuIds(vo.getSpuIds());
        List<StockSpuListVo> stockSpus = stockSpuService.findList(rule.getMerchantNo(), rule.getStoreNo(), query);
        vo.setSpus(
                stockSpus.stream().map(x -> {
                    MarketTimeCardSpuVo spuVo = new MarketTimeCardSpuVo();
                    spuVo.setSpuId(x.getSpuId());
                    spuVo.setSpuName(x.getName());
                    spuVo.setSellStock(x.getSellStock());
                    return spuVo;
                }).collect(Collectors.toList())
        );
        return vo;
    }

    public List<MarketTimeCardRuleListVo> findAllByIds(List<String> ids) {
        List<MarketTimeCardRuleEntity> rules = marketTimeCardRuleDao.findAllByIds(ids);
        return convertList(rules);
    }

    public PageVo<MarketTimeCardRuleListVo> page(long merchantNo, long storeNo, String accountId, MarketTimeCardRuleQuery query) {
        query.setDeleted(0);
        int total = marketTimeCardRuleDao.countPage(merchantNo, query);
        if (total <= 0) {
            return null;
        }
        List<MarketTimeCardRuleEntity> list = marketTimeCardRuleDao.findPage(merchantNo, query);
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, convertList(list));
    }

    public List<MarketTimeCardRuleListVo> list(long merchantNo, long storeNo, String accountId) {
        List<MarketTimeCardRuleEntity> list = marketTimeCardRuleDao.findAll(merchantNo);
        list = list.stream().filter(x -> x.getStatus() == CommonStatus.ENABLED.getCode()).collect(Collectors.toList());
        return convertList(list);
    }

    private List<MarketTimeCardRuleListVo> convertList(List<MarketTimeCardRuleEntity> list) {
        return list.stream().map(rule -> {
            MarketTimeCardRuleListVo vo = new MarketTimeCardRuleListVo();
            BeanUtils.copyProperties(rule, vo);
            vo.setRealAmount(AmountUtil.parseBigDecimal(rule.getRealAmount()));
            vo.setSpuIds(JsonUtil.toList(rule.getSpuIds(), String.class));
            StockSpuQuery query = new StockSpuQuery();
            query.setSpuIds(vo.getSpuIds());
            List<StockSpuListVo> stockSpus = stockSpuService.findList(rule.getMerchantNo(), rule.getStoreNo(), query);
            List<MarketTimeCardSpuVo> spus = stockSpus.stream().map(x -> {
                MarketTimeCardSpuVo spuVo = new MarketTimeCardSpuVo();
                spuVo.setSpuId(x.getSpuId());
                spuVo.setSpuName(x.getName());
                spuVo.setSellStock(x.getSellStock());
                return spuVo;
            }).collect(Collectors.toList());
            vo.setSpus(spus);
            return vo;
        }).collect(Collectors.toList());
    }
}
