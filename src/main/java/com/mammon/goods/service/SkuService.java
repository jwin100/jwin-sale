package com.mammon.goods.service;

import cn.hutool.core.util.StrUtil;
import com.mammon.common.Generate;
import com.mammon.enums.CommonStatus;
import com.mammon.exception.CustomException;
import com.mammon.goods.dao.SkuDao;
import com.mammon.goods.domain.dto.SkuDto;
import com.mammon.goods.domain.dto.SkuSingleDto;
import com.mammon.goods.domain.dto.SkuSpecDto;
import com.mammon.goods.domain.entity.SkuEntity;
import com.mammon.goods.domain.entity.SkuSpecEntity;
import com.mammon.goods.domain.vo.SkuSpecVo;
import com.mammon.goods.domain.vo.SkuVo;
import com.mammon.goods.domain.vo.SpuBaseVo;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.service.RedisService;
import com.mammon.stock.domain.dto.StockSkuDto;
import com.mammon.stock.domain.dto.StockSpuDto;
import com.mammon.stock.service.StockSpuService;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.QuantityUtil;
import com.mammon.utils.StockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SkuService {

    @Resource
    private SkuDao skuDao;

    @Resource
    private SkuSpecService skuSpecService;

    @Resource
    private SpuService spuService;

    @Resource
    private LeafCodeService leafCodeService;
    @Resource
    private RedisService redisService;
    @Resource
    private StockSpuService stockSpuService;

    @Transactional(rollbackFor = Exception.class)
    public List<StockSkuDto> batchEdit(long merchantNo, String spuId, List<SkuDto> skuDtos) {
        //获取原sku
        List<SkuEntity> skus = skuDao.findAllBySpuId(spuId);

        // 获取要删除的skuId
        List<String> deleteSkuIds = new ArrayList<>();
        skus.forEach(x -> {
            SkuDto skuDto = skuDtos.stream().filter(y -> y.getId().equals(x.getId())).findFirst().orElse(null);
            if (skuDto == null) {
                deleteSkuIds.add(x.getId());
            }
        });
        // 删除
        deleteSkuIds.forEach(x -> {
            delete(x);
            skuSpecService.deleteBySkuId(x);
        });

        // 编辑未删除sku
        return skuDtos.stream().map(x -> {
            if (StringUtils.isBlank(x.getId())) {
                return save(merchantNo, spuId, x);
            } else {
                return edit(x);
            }
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public StockSkuDto save(long merchantNo, String spuId, SkuDto dto) {
        if (StringUtils.isBlank(dto.getSkuCode())) {
            dto.setSkuCode(leafCodeService.generateSkuCode(merchantNo));
        }
        if (StringUtils.isBlank(dto.getSkuNo())) {
            dto.setSkuNo(dto.getSkuCode());
        }

        // 多规格商品，选中规格值组合后根据此字段进行筛选
        String joinSpec = dto.getSpecs().stream()
                .map(SkuSpecDto::getSpecValueId)
                .sorted(Comparator.comparing(x -> x))
                .collect(Collectors.joining("_"));

        SkuEntity entity = new SkuEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setSpuId(spuId);
        entity.setPurchaseAmount(AmountUtil.parse(dto.getPurchaseAmount()));
        entity.setReferenceAmount(AmountUtil.parse(dto.getReferenceAmount()));
        entity.setSkuWeight(QuantityUtil.parse(dto.getSkuWeight()));
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setJoinSpec(joinSpec);
        skuDao.save(entity);
        if (!CollectionUtils.isEmpty(dto.getSpecs())) {
            skuSpecService.batchSave(spuId, entity.getId(), dto.getSpecs());
        }
        StockSkuDto stockSkuDto = new StockSkuDto();
        BeanUtils.copyProperties(entity, stockSkuDto);
        stockSkuDto.setSkuId(entity.getId());
        stockSkuDto.setSellStock(StockUtil.parse(dto.getSellStock()));
        return stockSkuDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(long merchantNo, SkuSingleDto dto) {
        if (StringUtils.isBlank(dto.getSkuCode())) {
            dto.setSkuCode(leafCodeService.generateSkuCode(merchantNo));
        }
        if (StringUtils.isBlank(dto.getSkuNo())) {
            dto.setSkuNo(dto.getSkuCode());
        }

        // 多规格商品，选中规格值组合后根据此字段进行筛选
        String joinSpec = dto.getSpecs().stream()
                .map(SkuSpecDto::getSpecValueId)
                .sorted(Comparator.comparing(x -> x))
                .collect(Collectors.joining("_"));

        SkuEntity entity = new SkuEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setSpuId(dto.getSpuId());
        entity.setPurchaseAmount(AmountUtil.parse(dto.getPurchaseAmount()));
        entity.setReferenceAmount(AmountUtil.parse(dto.getReferenceAmount()));
        entity.setSkuWeight(QuantityUtil.parse(dto.getSkuWeight()));
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setJoinSpec(joinSpec);
        skuDao.save(entity);
        if (!CollectionUtils.isEmpty(dto.getSpecs())) {
            skuSpecService.batchSave(dto.getSpuId(), entity.getId(), dto.getSpecs());
        }

        long syncStoreNo = 0;
        String key = "spu:syncStoreNo:" + dto.getSpuId();
        String result = redisService.get(key);
        if (StrUtil.isNotBlank(result)) {
            syncStoreNo = Long.parseLong(result);
        }

        StockSkuDto stockSkuDto = new StockSkuDto();
        BeanUtils.copyProperties(entity, stockSkuDto);
        stockSkuDto.setSkuId(entity.getId());
        stockSkuDto.setSellStock(StockUtil.parse(dto.getSellStock()));

        // 商品资料，门店库存更新到库存表中
        StockSpuDto stockSpuDto = new StockSpuDto();
        BeanUtils.copyProperties(entity, stockSpuDto);
        stockSpuDto.setSpuId(entity.getId());
        stockSpuDto.setSyncStoreNo(syncStoreNo);
        stockSpuDto.setStatus(entity.getStatus());
        stockSpuDto.setSkus(Collections.singletonList(stockSkuDto));
        stockSpuService.batchEdit(merchantNo, stockSpuDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public StockSkuDto edit(SkuDto dto) {
        SkuEntity sku = skuDao.findById(dto.getId());
        if (sku == null) {
            throw new CustomException("商品规格信息错误");
        }

        // 多规格商品，选中规格值组合后根据此字段进行筛选
        String joinSpec = dto.getSpecs().stream()
                .map(SkuSpecDto::getSpecValueId)
                .sorted(Comparator.comparing(x -> x))
                .collect(Collectors.joining("_"));

        SkuEntity entity = new SkuEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
        entity.setPurchaseAmount(AmountUtil.parse(dto.getPurchaseAmount()));
        entity.setReferenceAmount(AmountUtil.parse(dto.getReferenceAmount()));
        entity.setSkuWeight(QuantityUtil.parse(dto.getSkuWeight()));
        entity.setJoinSpec(joinSpec);
        skuDao.update(entity);
        if (!CollectionUtils.isEmpty(dto.getSpecs())) {
            skuSpecService.batchSave(sku.getSpuId(), sku.getId(), dto.getSpecs());
        }
        StockSkuDto stockSkuDto = new StockSkuDto();
        BeanUtils.copyProperties(entity, stockSkuDto);
        stockSkuDto.setSpuId(sku.getSpuId());
        stockSkuDto.setSkuId(entity.getId());
        stockSkuDto.setSellStock(StockUtil.parse(dto.getSellStock()));
        stockSkuDto.setStatus(sku.getStatus());
        return stockSkuDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(long merchantNo, String id, SkuSingleDto dto) {
        SkuEntity sku = skuDao.findById(id);
        if (sku == null) {
            throw new CustomException("商品规格信息错误");
        }

        // 多规格商品，选中规格值组合后根据此字段进行筛选
        String joinSpec = dto.getSpecs().stream()
                .map(SkuSpecDto::getSpecValueId)
                .sorted(Comparator.comparing(x -> x))
                .collect(Collectors.joining("_"));

        SkuEntity entity = new SkuEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
        entity.setPurchaseAmount(AmountUtil.parse(dto.getPurchaseAmount()));
        entity.setReferenceAmount(AmountUtil.parse(dto.getReferenceAmount()));
        entity.setSkuWeight(QuantityUtil.parse(dto.getSkuWeight()));
        entity.setJoinSpec(joinSpec);
        skuDao.update(entity);
        if (!CollectionUtils.isEmpty(dto.getSpecs())) {
            skuSpecService.batchSave(sku.getSpuId(), sku.getId(), dto.getSpecs());
        }

        StockSkuDto stockSkuDto = new StockSkuDto();
        BeanUtils.copyProperties(entity, stockSkuDto);
        stockSkuDto.setSpuId(sku.getSpuId());
        stockSkuDto.setSkuId(entity.getId());
        stockSkuDto.setSellStock(0);
        stockSkuDto.setStatus(sku.getStatus());

        // 商品资料，门店库存更新到库存表中
        StockSpuDto stockSpuDto = new StockSpuDto();
        BeanUtils.copyProperties(entity, stockSpuDto);
        stockSpuDto.setSpuId(entity.getId());
        stockSpuDto.setStatus(entity.getStatus());
        stockSpuDto.setSkus(Collections.singletonList(stockSkuDto));
        stockSpuService.batchEdit(merchantNo, stockSpuDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBySpuId(String spuId) {
        List<SkuEntity> skus = skuDao.findAllBySpuId(spuId);
        skus.forEach(x -> {
            skuDao.delete(x.getId());
        });
    }

    public void delete(String id) {
        skuDao.delete(id);
    }

    public SkuVo findById(long merchantNo, String id) {
        SkuEntity sku = skuDao.findById(id);
        if (sku == null) {
            return null;
        }
        SpuBaseVo spu = spuService.findBaseById(merchantNo, sku.getSpuId());
        if (spu == null) {
            return null;
        }

        return convertInfo(spu, sku);
    }

    public List<SkuVo> findAllBySpuId(String spuId) {
        List<SkuEntity> skus = skuDao.findAllBySpuId(spuId);
        return convertList(skus);
    }

    public List<SkuEntity> findAll() {
        return skuDao.findAll();
    }

    private SkuVo convertInfo(SpuBaseVo spu, SkuEntity sku) {
        SkuVo vo = new SkuVo();
        BeanUtils.copyProperties(sku, vo);
        vo.setPurchaseAmount(AmountUtil.parseBigDecimal(sku.getPurchaseAmount()));
        vo.setReferenceAmount(AmountUtil.parseBigDecimal(sku.getReferenceAmount()));
        vo.setSkuWeight(QuantityUtil.parseBigDecimal(sku.getSkuWeight()));
        vo.setSpecs(skuSpecService.findAllBySpuId(sku.getSpuId(), sku.getId()));
        if (!CollectionUtils.isEmpty(spu.getPictures())) {
            vo.setPicture(spu.getPictures().stream().findFirst().orElse(null));
        }
        return vo;
    }

    private List<SkuVo> convertList(List<SkuEntity> skus) {
        if (CollectionUtils.isEmpty(skus)) {
            return Collections.emptyList();
        }
        List<String> skuIds = skus.stream().map(SkuEntity::getId).collect(Collectors.toList());
        List<SkuSpecVo> specs = skuSpecService.findAllBySkuIds(skuIds);
        return skus.stream().map(x -> {
            SkuVo vo = new SkuVo();
            BeanUtils.copyProperties(x, vo);
            vo.setPurchaseAmount(AmountUtil.parseBigDecimal(x.getPurchaseAmount()));
            vo.setReferenceAmount(AmountUtil.parseBigDecimal(x.getReferenceAmount()));
            vo.setSkuWeight(QuantityUtil.parseBigDecimal(x.getSkuWeight()));
            vo.setSpecs(
                    specs.stream()
                            .filter(spec -> x.getSpuId().equals(spec.getSpuId()) && spec.getSkuId().equals(x.getId()))
                            .collect(Collectors.toList())
            );
            return vo;
        }).collect(Collectors.toList());
    }
}
