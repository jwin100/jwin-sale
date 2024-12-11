package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.goods.dao.SkuSpecDao;
import com.mammon.goods.domain.entity.CategoryEntity;
import com.mammon.goods.domain.entity.UnitEntity;
import com.mammon.goods.domain.enums.CategoryLevel;
import com.mammon.goods.domain.query.CategoryListQuery;
import com.mammon.goods.domain.vo.SkuSpecVo;
import com.mammon.goods.service.CategoryService;
import com.mammon.goods.service.SkuSpecService;
import com.mammon.goods.service.UnitService;
import com.mammon.merchant.domain.entity.MerchantStoreEntity;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.stock.dao.StockSpuDao;
import com.mammon.stock.domain.dto.StockSpuDto;
import com.mammon.stock.domain.entity.StockSkuEntity;
import com.mammon.stock.domain.entity.StockSpuEntity;
import com.mammon.stock.domain.query.StockSpuPageQuery;
import com.mammon.stock.domain.query.StockSpuQuery;
import com.mammon.stock.domain.vo.*;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import com.mammon.utils.StockUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/5/29 15:33
 */
@Service
public class StockSpuService {

    @Resource
    private SkuSpecService skuSpecService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private StockSpuDao stockSpuDao;

    @Resource
    @Lazy
    private StockSkuService stockSkuService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private UnitService unitService;
    @Autowired
    private SkuSpecDao skuSpecDao;

    /**
     * 同步商品信息到各门店
     *
     * @param merchantNo
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchEdit(long merchantNo, StockSpuDto dto) {
        List<MerchantStoreEntity> stores = merchantStoreService.findAllByMerchantNo(merchantNo);
        stores.forEach(store -> {
            batchEdit(merchantNo, store.getStoreNo(), dto);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchEdit(long merchantNo, long storeNo, StockSpuDto dto) {
        StockSpuEntity stockSpu = stockSpuDao.findBySpuId(merchantNo, storeNo, dto.getSpuId());
        String stockSpuId;
        if (stockSpu != null) {
            stockSpuId = stockSpu.getId();
            edit(merchantNo, storeNo, stockSpuId, dto);
        } else {
            StockSpuEntity entity = save(merchantNo, storeNo, dto);
            stockSpuId = entity.getId();
        }
        stockSkuService.batchEdit(merchantNo, storeNo, stockSpuId,
                dto.getSyncStoreNo(), dto.getSkus());
    }

    /**
     * 保存商品信息到门店spu表中
     *
     * @param merchantNo
     * @param storeNo
     * @param dto
     * @return
     */
    public StockSpuEntity save(long merchantNo, long storeNo, StockSpuDto dto) {
        StockSpuEntity entity = new StockSpuEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        stockSpuDao.save(entity);
        return entity;
    }

    /**
     * 保存商品信息到门店spu表中
     *
     * @param merchantNo
     * @param storeNo
     * @param dto
     * @return
     */
    public StockSpuEntity edit(long merchantNo, long storeNo, String id, StockSpuDto dto) {
        StockSpuEntity entity = new StockSpuEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        stockSpuDao.edit(entity);
        return entity;
    }

    /**
     * 修改门店库存商品库商品状态
     *
     * @param merchantNo
     * @param storeNo
     * @param spuId
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    public void editGoodsStatus(long merchantNo, long storeNo, String spuId, int status) {
        StockSpuEntity stockSpu = stockSpuDao.findBySpuId(merchantNo, storeNo, spuId);
        if (stockSpu == null) {
            return;
        }
        stockSpuDao.editGoodsStatus(stockSpu.getId(), status);
    }

    /**
     * 修改门店商品信息状态
     *
     * @param merchantNo
     * @param storeNo
     * @param spuId
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    public void editStatus(long merchantNo, long storeNo, String spuId, int status) {
        StockSpuEntity stockSpu = stockSpuDao.findBySpuId(merchantNo, storeNo, spuId);
        if (stockSpu == null) {
            return;
        }
        stockSpuDao.editStatus(stockSpu.getId(), status);
    }

    /**
     * 删除商品库触发-删除所有门店下商品信息
     *
     * @param merchantNo
     * @param spuId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletedBySpuId(long merchantNo, String spuId) {
        List<StockSkuEntity> skus = stockSkuService.findBaseListBySpuId(merchantNo, spuId);
        stockSpuDao.deleteBySpuId(merchantNo, spuId);
        // 删除所有门店下sku
        skus.stream().map(StockSkuEntity::getSkuId).distinct().forEach(x -> {
            stockSkuService.deleteBySkuId(x);
        });
    }

    public StockSpuEntity findById(String id) {
        return stockSpuDao.findById(id);
    }

    /**
     * 获取门店商品详情
     *
     * @param merchantNo 商户编号
     * @param storeNo    门店编号
     * @param spuId      SPU ID
     * @return 返回库存SPU信息，如果找不到则返回null
     */
    public StockSpuVo findDetailBySpuId(long merchantNo, long storeNo, String spuId) {
        StockSpuEntity stockSpu = stockSpuDao.findBySpuId(merchantNo, storeNo, spuId);
        if (stockSpu == null) {
            return null;
        }
        UnitEntity unit = unitService.findById(merchantNo, stockSpu.getUnitId());

        StockSpuVo stockVo = new StockSpuVo();
        BeanUtils.copyProperties(stockSpu, stockVo);
        stockVo.setPictures(JsonUtil.toList(stockSpu.getPictures(), String.class));
        stockVo.setStoreName(getStoreName(merchantNo, storeNo));
        stockVo.setCategoryName(getCategoryName(merchantNo, stockSpu.getCategoryId()));
        stockVo.setLastCategoryName(getLastCategoryName(merchantNo, stockSpu.getCategoryId()));
        if (unit != null) {
            stockVo.setUnitName(unit.getName());
        }
        stockVo.setSkus(stockSkuService.findListDetail(stockSpu));
        long sellStock = stockVo.getSkus().stream().mapToLong(x -> StockUtil.parse(x.getSellStock())).sum();
        stockVo.setSellStock(StockUtil.parseBigDecimal(sellStock));
        return stockVo;
    }

    /**
     * 获取门店商品分页列表
     *
     * @param merchantNo 商户编号
     * @param storeNo    门店编号
     * @param accountId  账号ID
     * @param query      查询条件
     * @return 分页查询结果
     */
    public PageVo<StockSpuListVo> page(long merchantNo, long storeNo, String accountId, StockSpuPageQuery query) {
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(merchantNo, storeNo);
        if (storeVo == null) {
            return PageResult.of();
        }
        if (!storeVo.isMain() || !accountId.equals(storeVo.getAccountId()) || query.getStoreNo() == null) {
            query.setStoreNo(storeNo);
        }

        query.setCategoryIds(convertCategoryIds(merchantNo, query.getCategoryIds(), query.getCategoryId()));
        int total = stockSpuDao.countPage(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<StockSpuEntity> list = stockSpuDao.findPage(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, convertStockSpu(merchantNo, storeNo, list));
    }

    /**
     * 根据查询条件查询库存SPU列表
     *
     * @param merchantNo 商户编号
     * @param storeNo    门店编号
     * @param spuIds     查询条件
     * @return 库存SPU列表
     */
    public List<StockSpuEntity> findListBySpuIds(long merchantNo, long storeNo, List<String> spuIds) {
        return stockSpuDao.findListBySpuIds(merchantNo, storeNo, spuIds);
    }

    /**
     * 根据查询条件查询库存SPU列表
     *
     * @param merchantNo 商户编号
     * @param storeNo    门店编号
     * @param query      查询条件
     * @return 库存SPU列表
     */
    public List<StockSpuListVo> findList(long merchantNo, long storeNo, StockSpuQuery query) {
        query.setStatus(CommonStatus.ENABLED.getCode());
        if (query.getStoreNo() == null) {
            query.setStoreNo(storeNo);
        }
        List<StockSpuEntity> list = stockSpuDao.findList(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return convertStockSpu(merchantNo, storeNo, list);
    }

    private String getStoreName(long merchantNo, long storeNo) {
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(merchantNo, storeNo);
        if (storeVo == null) {
            return null;
        }
        return storeVo.getStoreName();
    }

    private String getCategoryName(long merchantNo, String categoryId) {
        if (StringUtils.isBlank(categoryId)) {
            return null;
        }
        String categoryName;
        CategoryEntity entity = categoryService.findById(merchantNo, categoryId);
        if (entity == null) {
            return null;
        }
        categoryName = entity.getName();
        if (StringUtils.isNotBlank(entity.getPid())) {
            CategoryEntity parentEntity = categoryService.findById(merchantNo, entity.getPid());
            if (parentEntity == null) {
                return categoryName;
            }
            categoryName = parentEntity.getName() + ">" + categoryName;
        }
        return categoryName;
    }

    private String getLastCategoryName(long merchantNo, String categoryId) {
        CategoryEntity entity = categoryService.findById(merchantNo, categoryId);
        if (entity == null) {
            return null;
        }
        return entity.getName();
    }

    /**
     * 获取商品分类数组
     *
     * @param merchantNo
     * @param categoryIds
     * @param categoryId
     * @return
     */
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
        categoryIds.addAll(list.stream().map(CategoryEntity::getId).collect(Collectors.toList()));
        return categoryIds;
    }

    /**
     * 商品信息转换
     *
     * @param merchantNo 商户编号
     * @param storeNo    门店编号
     * @param spuVos     SPU列表
     * @return 库存SPU列表
     */
    private List<StockSpuListVo> convertStockSpu(long merchantNo, long storeNo, List<StockSpuEntity> spuVos) {
        List<String> stockSpuIds = spuVos.stream().map(StockSpuEntity::getId).collect(Collectors.toList());
        List<String> spuIds = spuVos.stream().map(StockSpuEntity::getSpuId).collect(Collectors.toList());
        List<StockSkuEntity> stockSkuList = stockSkuService.findListByStockSpuIds(stockSpuIds);
        List<SkuSpecVo> specVos = skuSpecService.findAllBySpuIds(spuIds);

        return spuVos.stream().map(x -> {
            UnitEntity unit = unitService.findById(merchantNo, x.getUnitId());

            StockSpuListVo vo = new StockSpuListVo();
            BeanUtils.copyProperties(x, vo);
            vo.setCategoryName(getCategoryName(merchantNo, x.getCategoryId()));
            vo.setStoreName(getStoreName(merchantNo, storeNo));

            // 门店库存
            long sellStock = stockSkuList.stream()
                    .filter(stock -> stock.getSpuId().equals(x.getSpuId()))
                    .mapToLong(StockSkuEntity::getSellStock).sum();
            vo.setSellStock(StockUtil.parseBigDecimal(sellStock));

            List<SkuSpecVo> tempSpecVos = specVos.stream()
                    .filter(spec -> spec.getSpuId().equals(x.getSpuId())).collect(Collectors.toList());
            vo.setSpecs(tempSpecVos.size());
            vo.setSkuSpecs(getSkuSpec(tempSpecVos));

            // 零售价
            stockSkuList.stream().filter(sku -> sku.getSpuId().equals(x.getSpuId()))
                    .findFirst()
                    .ifPresent(sku -> vo.setReferenceAmount(AmountUtil.parseBigDecimal(sku.getReferenceAmount())));

            // 商品图
            List<String> pictures = JsonUtil.toList(x.getPictures(), String.class);
            vo.setPicture(pictures.stream().filter(Objects::nonNull).findFirst().orElse(null));
            // 单位
            if (unit != null) {
                vo.setUnitName(unit.getName());
                vo.setUnitType(unit.getType());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    private List<StockSkuSpecVo> getSkuSpec(List<SkuSpecVo> tempSpecVos) {
        if (CollectionUtils.isEmpty(tempSpecVos)) {
            return Collections.emptyList();
        }
        List<StockSkuSpecVo> skuSpecVos = new ArrayList<>();
        tempSpecVos.forEach(y -> {
            if (skuSpecVos.stream().noneMatch(z -> z.getSpecId().equals(y.getSpecId()))) {
                StockSkuSpecVo skuSpecVo = new StockSkuSpecVo();
                skuSpecVo.setSpecId(y.getSpecId());
                skuSpecVo.setSpecName(y.getSpecName());

                List<SkuSpecVo> tempSkuSpecValueVos = tempSpecVos.stream().filter(spec -> spec.getSpecId().equals(y.getSpecId()))
                        .collect(Collectors.toList());
                List<StockSkuSpecValueVo> skuSpecValueVos = new ArrayList<>();
                tempSkuSpecValueVos.forEach(value -> {
                    if (skuSpecValueVos.stream().noneMatch(z -> z.getSpecValueId().equals(value.getSpecValueId()))) {
                        StockSkuSpecValueVo skuSpecValueVo = new StockSkuSpecValueVo();
                        skuSpecValueVo.setSpecValueId(value.getSpecValueId());
                        skuSpecValueVo.setSpecValueName(value.getSpecValueName());
                        skuSpecValueVos.add(skuSpecValueVo);
                    }
                });
                skuSpecVo.setSpecValues(skuSpecValueVos);
                skuSpecVos.add(skuSpecVo);
            }
        });
        return skuSpecVos;
    }
}
