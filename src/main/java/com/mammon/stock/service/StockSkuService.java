package com.mammon.stock.service;

import cn.hutool.core.collection.CollUtil;
import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonDeleted;
import com.mammon.enums.CommonStatus;
import com.mammon.exception.CustomException;
import com.mammon.goods.domain.entity.CategoryEntity;
import com.mammon.goods.domain.entity.UnitEntity;
import com.mammon.goods.domain.enums.CategoryLevel;
import com.mammon.goods.domain.enums.SpuCountedType;
import com.mammon.goods.domain.query.CategoryListQuery;
import com.mammon.goods.domain.vo.SkuSpecVo;
import com.mammon.goods.service.CategoryService;
import com.mammon.goods.service.SkuSpecService;
import com.mammon.goods.service.UnitService;
import com.mammon.stock.dao.StockSkuDao;
import com.mammon.stock.dao.StockSpuDao;
import com.mammon.stock.domain.dto.StockSkuDto;
import com.mammon.stock.domain.dto.StockStoreExpendDto;
import com.mammon.stock.domain.dto.StockStoreReplenishDto;
import com.mammon.stock.domain.entity.StockSkuEntity;
import com.mammon.stock.domain.entity.StockSpuEntity;
import com.mammon.stock.domain.query.*;
import com.mammon.stock.domain.vo.*;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import com.mammon.utils.StockUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/5/29 15:33
 */
@Service
public class StockSkuService {

    @Resource
    private StockSkuDao stockSkuDao;

    @Resource
    private SkuSpecService skuSpecService;

    @Resource
    private UnitService unitService;

    @Resource
    private StockSpuService stockSpuService;
    private StockSpuDao stockSpuDao;
    @Autowired
    private CategoryService categoryService;

    /**
     * 批量保存商品规格信息到门店商品规格表中
     *
     * @param merchantNo
     * @param storeNo
     * @param stockSpuId
     * @param syncStoreNo
     * @param skuDtos
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchEdit(long merchantNo, long storeNo, String stockSpuId,
                          long syncStoreNo, List<StockSkuDto> skuDtos) {
        List<StockSkuEntity> stockSkus = stockSkuDao.findListByStockSpuId(stockSpuId);
        skuDtos.forEach(dto -> {
            StockSkuEntity stockSku = stockSkus.stream().filter(x -> x.getSkuId().equals(dto.getSkuId()))
                    .findFirst().orElse(null);
            if (stockSku == null) {
                save(merchantNo, storeNo, stockSpuId, syncStoreNo, dto);
            } else {
                edit(stockSku.getId(), dto);
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(long merchantNo, long storeNo, String stockSpuId,
                     long syncStoreNo, StockSkuDto dto) {
        StockSkuEntity entity = new StockSkuEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setStockSpuId(stockSpuId);
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setDeleted(CommonDeleted.NOT_DELETED.getCode());
        entity.setSellStock(0);
        // 同步库存
        if (storeNo == syncStoreNo) {
            entity.setSellStock(dto.getSellStock());
        }
        stockSkuDao.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(String id, StockSkuDto dto) {
        StockSkuEntity entity = new StockSkuEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        stockSkuDao.edit(entity);
    }

    /**
     * 批量入库
     *
     * @param merchantNo
     * @param storeNo
     * @param replenishes
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchSellStockReplenish(long merchantNo, long storeNo, List<StockStoreReplenishDto> replenishes) {
        replenishes.forEach(x -> {
            editSellStockReplenish(merchantNo, storeNo, x.getSkuId(), x.getSellStock());
        });
    }

    /**
     * 批量出库
     *
     * @param merchantNo
     * @param storeNo
     * @param expends
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchSellStockExpend(long merchantNo, long storeNo, List<StockStoreExpendDto> expends) {
        expends.forEach(x -> {
            editSellStockExpend(merchantNo, storeNo, x.getSkuId(), x.getSellStock());
        });
    }

    /**
     * 入库
     *
     * @param sellStock
     * @return
     */
    public void editSellStockReplenish(long merchantNo, long storeNo, String skuId, BigDecimal sellStock) {
        StockSkuEntity stockSku = stockSkuDao.findBySkuId(merchantNo, storeNo, skuId);
        if (stockSku == null) {
            throw new CustomException("商品信息错误");
        }
        long replenishStock = StockUtil.parse(sellStock);
        stockSkuDao.editSellStockIn(merchantNo, storeNo, skuId, replenishStock);
    }

    /**
     * 出库
     *
     * @param sellStock
     * @return
     */
    public StockChangeVo editSellStockExpend(long merchantNo, long storeNo, String skuId, BigDecimal sellStock) {
        long expendStock = StockUtil.parse(sellStock);
        StockChangeVo vo = validateSellStock(merchantNo, storeNo, skuId, expendStock);
        if (vo.getCode() == 0) {
            return vo;
        }
        stockSkuDao.editSellStockOut(merchantNo, storeNo, skuId, expendStock);
        return StockChangeVo.builder().code(1).build();
    }

    /**
     * 验证库存
     *
     * @return
     */
    public StockChangeVo validateSellStock(long merchantNo, long storeNo, String skuId, long sellStock) {
        StockSkuEntity stockStore = stockSkuDao.findBySkuId(merchantNo, storeNo, skuId);
        if (stockStore == null) {
            return StockChangeVo.builder().message("商品信息错误").build();
        }
        if (stockStore.getSellStock() < sellStock) {
            return StockChangeVo.builder().message("库存不足").build();
        }
        return StockChangeVo.builder().code(1).build();
    }

    public List<StockSkuDetailListVo> findListDetail(StockSpuEntity stockSpu) {
        List<StockSkuEntity> stockSkus = stockSkuDao.findListByStockSpuId(stockSpu.getId());
        return stockSkus.stream().map(stockSku -> {
            List<SkuSpecVo> specVos = skuSpecService.findAllBySpuId(stockSku.getSpuId(), stockSku.getSkuId());
            UnitEntity unit = unitService.findById(stockSku.getMerchantNo(), stockSpu.getUnitId());
            StockSkuDetailListVo stockSkuVo = new StockSkuDetailListVo();
            BeanUtils.copyProperties(stockSku, stockSkuVo);
            stockSkuVo.setSpecs(specVos);
            stockSkuVo.setPurchaseAmount(AmountUtil.parseBigDecimal(stockSku.getPurchaseAmount()));
            stockSkuVo.setReferenceAmount(AmountUtil.parseBigDecimal(stockSku.getReferenceAmount()));
            stockSkuVo.setSellStock(StockUtil.parseBigDecimal(stockSku.getSellStock()));
            stockSkuVo.setSkuWeight(StockUtil.parseBigDecimal(stockSku.getSkuWeight()));
            if (unit != null) {
                stockSkuVo.setUnitId(unit.getId());
                stockSkuVo.setUnitName(unit.getName());
                stockSkuVo.setUnitType(unit.getType());
            }
            return stockSkuVo;
        }).collect(Collectors.toList());
    }

    public PageVo<StockSkuDetailListVo> page(long merchantNo, long storeNo, String accountId, StockSkuPageQuery query) {
        query.setCategoryIds(convertCategoryIds(merchantNo, query.getCategoryIds(), query.getCategoryId()));
        int total = stockSkuDao.countPage(merchantNo, storeNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<StockSkuEntity> list = stockSkuDao.findPage(merchantNo, storeNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<String> spuIds = list.stream().map(StockSkuEntity::getSpuId).distinct().collect(Collectors.toList());
        List<StockSpuEntity> stockSpus = stockSpuService.findListBySpuIds(merchantNo, storeNo, spuIds);
        List<StockSkuDetailListVo> vos = list.stream().map(stockSku -> {
            StockSpuEntity stockSpu = stockSpus.stream()
                    .filter(x -> x.getId().equals(stockSku.getStockSpuId()))
                    .findFirst().orElse(null);
            if (stockSpu == null) {
                return null;
            }
            List<String> pictures = JsonUtil.toList(stockSpu.getPictures(), String.class);

            List<SkuSpecVo> specVos = skuSpecService.findAllBySpuId(stockSku.getSpuId(), stockSku.getSkuId());
            UnitEntity unit = unitService.findById(stockSku.getMerchantNo(), stockSpu.getUnitId());
            StockSkuDetailListVo stockSkuVo = new StockSkuDetailListVo();
            BeanUtils.copyProperties(stockSku, stockSkuVo);
            stockSkuVo.setSpecs(specVos);
            stockSkuVo.setPicture(pictures.stream().findFirst().orElse(null));
            stockSkuVo.setPurchaseAmount(AmountUtil.parseBigDecimal(stockSku.getPurchaseAmount()));
            stockSkuVo.setReferenceAmount(AmountUtil.parseBigDecimal(stockSku.getReferenceAmount()));
            stockSkuVo.setSellStock(StockUtil.parseBigDecimal(stockSku.getSellStock()));
            stockSkuVo.setSkuWeight(StockUtil.parseBigDecimal(stockSku.getSkuWeight()));
            stockSkuVo.setCountedType(stockSpu.getCountedType());
            // 商品spu的状态
            stockSkuVo.setStatus(stockSpu.getStatus());
            if (unit != null) {
                stockSkuVo.setUnitId(unit.getId());
                stockSkuVo.setUnitName(unit.getName());
                stockSkuVo.setUnitType(unit.getType());
            }
            return stockSkuVo;
        }).collect(Collectors.toList());

        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public List<StockSkuEntity> findListByStockSpuIds(List<String> stockSpuIds) {
        if (CollectionUtils.isEmpty(stockSpuIds)) {
            return Collections.emptyList();
        }
        return stockSkuDao.findListByStockSpuIds(stockSpuIds);
    }

    public StockSkuVo findBySkuNo(long merchantNo, long storeNo, String skuNo) {
        StockSkuEntity stockSku = stockSkuDao.findBySkuNo(merchantNo, storeNo, skuNo);
        return convertStockSku(stockSku);
    }

    /**
     * 根据SkuId查询门店SKU信息
     *
     * @param merchantNo 商户编号
     * @param storeNo    门店编号
     * @param skuId      SKU ID
     * @return 返回库存SKU信息，如果找不到则返回null
     */
    public StockSkuVo findBySkuId(long merchantNo, long storeNo, String skuId) {
        StockSkuEntity stockSku = stockSkuDao.findBySkuId(merchantNo, storeNo, skuId);
        return convertStockSku(stockSku);
    }

    public List<StockSkuVo> findListBySkuIds(long merchantNo, long storeNo, List<String> skuIds) {
        List<StockSkuEntity> stockSkus = stockSkuDao.findListBySkuIds(merchantNo, storeNo, skuIds);
        return stockSkus.stream().map(this::convertStockSku).collect(Collectors.toList());
    }

    /**
     * 根据商户编号、门店编号和SPU ID列表查询库存SKU列表
     *
     * @param merchantNo 商户编号
     * @param storeNo    门店编号
     * @param spuIds     SPU ID列表
     * @return 返回库存SKU列表
     */
    public List<StockSkuVo> findListBySpuIds(long merchantNo, long storeNo, List<String> spuIds) {
        List<StockSkuEntity> stockSkus = stockSkuDao.findListBySpuIds(merchantNo, storeNo, spuIds);
        return stockSkus.stream().map(this::convertStockSku).collect(Collectors.toList());
    }

    /**
     * 根据商户编号、门店编号和SPU ID查询库存SKU列表
     *
     * @param merchantNo 商户编号
     * @param storeNo    门店编号
     * @param spuId      SPU ID
     * @return 返回库存SKU列表
     */
    public List<StockSkuVo> findListBySpuId(long merchantNo, long storeNo, String spuId) {
        List<StockSkuEntity> stockSkus = stockSkuDao.findListBySpuId(merchantNo, storeNo, spuId);
        return stockSkus.stream().map(this::convertStockSku).collect(Collectors.toList());
    }

    public List<StockSkuEntity> findListBySkuId(String skuId) {
        return stockSkuDao.findListBySkuId(skuId);
    }

    /**
     * 根据商户编号、门店编号、SPU ID和库存分配查询条件查询库存SKU分配列表
     *
     * @param merchantNo 商户编号
     * @param storeNo    门店编号
     * @param spuId      SPU ID
     * @param query      库存分配查询条件
     * @return 返回库存SKU分配列表
     */
    public List<StockSkuAllocateVo> findAllocateBySpuId(long merchantNo, long storeNo, String spuId,
                                                        StockAllocateGateQuery query) {
        List<StockSkuEntity> stockSkus = stockSkuDao.findListBySpuId(merchantNo, storeNo, spuId);
        if (stockSkus == null) {
            return Collections.emptyList();
        }
        List<StockSkuEntity> outStockList = stockSkuDao.findListBySpuId(merchantNo, query.getOutStoreNo(), spuId);
        List<StockSkuEntity> inStockList = stockSkuDao.findListBySpuId(merchantNo, query.getInStoreNo(), spuId);

        return stockSkus.stream().map(stockSku -> {
            StockSpuEntity stockSpu = stockSpuService.findById(stockSku.getStockSpuId());
            if (stockSpu == null) {
                return null;
            }
            List<SkuSpecVo> specVos = skuSpecService.findAllBySpuId(stockSku.getSpuId(), stockSku.getSkuId());
            UnitEntity unit = unitService.findById(stockSku.getMerchantNo(), stockSpu.getUnitId());
            List<String> pictures = JsonUtil.toList(stockSpu.getPictures(), String.class);

            StockSkuAllocateVo stockSkuVo = new StockSkuAllocateVo();
            BeanUtils.copyProperties(stockSku, stockSkuVo);
            stockSkuVo.setSpuName(stockSpu.getName());
            stockSkuVo.setSpecs(specVos);
            stockSkuVo.setPicture(pictures.stream().findFirst().orElse(null));
            stockSkuVo.setPurchaseAmount(AmountUtil.parseBigDecimal(stockSku.getPurchaseAmount()));
            stockSkuVo.setReferenceAmount(AmountUtil.parseBigDecimal(stockSku.getReferenceAmount()));
            stockSkuVo.setSkuWeight(StockUtil.parseBigDecimal(stockSku.getSkuWeight()));
            if (unit != null) {
                stockSkuVo.setUnitId(unit.getId());
                stockSkuVo.setUnitName(unit.getName());
                stockSkuVo.setUnitType(unit.getType());
            }

            outStockList.stream().filter(x -> stockSku.getSkuId().equals(x.getSkuId()))
                    .findFirst().ifPresent(x -> {
                        stockSkuVo.setOutSellStock(StockUtil.parseBigDecimal(x.getSellStock()));
                    });
            inStockList.stream().filter(x -> stockSku.getSkuId().equals(x.getSkuId()))
                    .findFirst().ifPresent(x -> {
                        stockSkuVo.setInSellStock(StockUtil.parseBigDecimal(x.getSellStock()));
                    });
            return stockSkuVo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 查询指定商户和门店下，库存有货的SKU列表
     *
     * @param merchantNo 商户编号
     * @param storeNo    门店编号
     * @param query      查询条件
     * @return 库存有货的SKU列表
     */
    public List<StockSkuCountedVo> findCountedList(long merchantNo, long storeNo, StockSkuCountedQuery query) {
        StockSpuQuery spuQuery = new StockSpuQuery();
        spuQuery.setCountedType(SpuCountedType.YES.getCode());
        List<StockSpuListVo> stockSpus = stockSpuService.findList(merchantNo, storeNo, spuQuery);
        if (CollectionUtils.isEmpty(stockSpus)) {
            return Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(query.getSpuIds())) {
            query.setSpuIds(stockSpus.stream().map(StockSpuListVo::getSpuId).collect(Collectors.toList()));
        }
        List<StockSkuEntity> stockSkus = stockSkuDao.findListBySpuIds(merchantNo, storeNo, query.getSpuIds());
        return stockSkus.stream().map(stockSku -> {
            StockSkuCountedVo stockSkuVo = new StockSkuCountedVo();
            BeanUtils.copyProperties(stockSku, stockSkuVo);
            StockSpuListVo stockSpu = stockSpus.stream().filter(x -> x.getSpuId().equals(stockSku.getSpuId()))
                    .findFirst().orElse(null);
            if (stockSpu == null) {
                return null;
            }
            List<SkuSpecVo> specVos = skuSpecService.findAllBySpuId(stockSku.getSpuId(), stockSku.getSkuId());
            UnitEntity unit = unitService.findById(stockSku.getMerchantNo(), stockSpu.getUnitId());

            stockSkuVo.setSpecs(specVos);
            stockSkuVo.setPicture(stockSpu.getPicture());
            stockSkuVo.setPurchaseAmount(AmountUtil.parseBigDecimal(stockSku.getPurchaseAmount()));
            stockSkuVo.setReferenceAmount(AmountUtil.parseBigDecimal(stockSku.getReferenceAmount()));
            stockSkuVo.setSellStock(StockUtil.parseBigDecimal(stockSku.getSellStock()));
            stockSkuVo.setSkuWeight(StockUtil.parseBigDecimal(stockSku.getSkuWeight()));
            if (unit != null) {
                stockSkuVo.setUnitId(unit.getId());
                stockSkuVo.setUnitName(unit.getName());
                stockSkuVo.setUnitType(unit.getType());
            }
            return stockSkuVo;
        }).collect(Collectors.toList());
    }

    private StockSkuVo convertStockSku(StockSkuEntity stockSku) {
        if (stockSku == null) {
            return null;
        }
        StockSpuEntity stockSpu = stockSpuService.findById(stockSku.getStockSpuId());
        if (stockSpu == null) {
            return null;
        }
        List<SkuSpecVo> specVos = skuSpecService.findAllBySpuId(stockSku.getSpuId(), stockSku.getSkuId());
        UnitEntity unit = unitService.findById(stockSku.getMerchantNo(), stockSpu.getUnitId());
        StockSkuVo stockSkuVo = new StockSkuVo();
        BeanUtils.copyProperties(stockSku, stockSkuVo);
        stockSkuVo.setSpecs(specVos);
        stockSkuVo.setPurchaseAmount(AmountUtil.parseBigDecimal(stockSku.getPurchaseAmount()));
        stockSkuVo.setReferenceAmount(AmountUtil.parseBigDecimal(stockSku.getReferenceAmount()));
        stockSkuVo.setSellStock(StockUtil.parseBigDecimal(stockSku.getSellStock()));
        stockSkuVo.setSkuWeight(StockUtil.parseBigDecimal(stockSku.getSkuWeight()));
        List<String> pictures = JsonUtil.toList(stockSpu.getPictures());
        stockSkuVo.setPicture(pictures.stream().findFirst().orElse(null));
        stockSkuVo.setCountedType(stockSpu.getCountedType());
        if (unit != null) {
            stockSkuVo.setUnitId(unit.getId());
            stockSkuVo.setUnitName(unit.getName());
            stockSkuVo.setUnitType(unit.getType());
        }
        return stockSkuVo;
    }

    private List<String> convertCategoryIds(long merchantNo, List<String> categoryIds, String categoryId) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            categoryIds = new ArrayList<>();
        }
        if (StringUtils.isBlank(categoryId)) {
            return categoryIds;
        }
        categoryIds.add(categoryId);

        CategoryListQuery categoryQuery = new CategoryListQuery();
        categoryQuery.setPid(categoryId);
        categoryQuery.setLevel(CategoryLevel.TWO.getCode());
        List<CategoryEntity> list = categoryService.findAll(merchantNo, categoryQuery);
        if (CollUtil.isNotEmpty(list)) {
            categoryIds.addAll(list.stream().map(CategoryEntity::getId).collect(Collectors.toList()));
        }
        return categoryIds;
    }
}
