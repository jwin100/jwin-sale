package com.mammon.goods.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mammon.common.Generate;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.goods.dao.SkuDao;
import com.mammon.goods.dao.SpuDao;
import com.mammon.goods.domain.dto.*;
import com.mammon.goods.domain.entity.*;
import com.mammon.goods.domain.enums.CategoryLevel;
import com.mammon.goods.domain.enums.UnitType;
import com.mammon.goods.domain.query.CategoryListQuery;
import com.mammon.goods.domain.query.SpuPageQuery;
import com.mammon.goods.domain.vo.*;
import com.mammon.common.PageResult;
import com.mammon.common.ResultCode;
import com.mammon.exception.CustomException;
import com.mammon.goods.domain.query.SpuQuery;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.merchant.domain.entity.MerchantStoreEntity;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.service.RedisService;
import com.mammon.stock.dao.StockSkuDao;
import com.mammon.stock.domain.dto.StockSkuDto;
import com.mammon.stock.domain.dto.StockSpuDto;
import com.mammon.stock.domain.entity.StockSkuEntity;
import com.mammon.stock.domain.vo.StockSkuVo;
import com.mammon.stock.service.StockSkuService;
import com.mammon.stock.service.StockSpuService;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.ExcelUtils;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SpuService {

    @Resource
    private SpuDao spuDao;

    @Resource
    private CategoryService categoryService;

    @Resource
    private UnitService unitService;

    @Resource
    private SkuService skuService;

    @Resource
    private SpecService specService;

    @Resource
    private SkuSpecService skuSpecService;

    @Resource
    private LeafCodeService leafCodeService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private StockSpuService stockSpuService;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private StockSkuService stockSkuService;
    @Autowired
    private StockSkuDao stockSkuDao;
    @Autowired
    private RedisService redisService;

    @Transactional(rollbackFor = Exception.class)
    public void create(long merchantNo, String accountId, SpuDto dto) {
        if (StringUtils.isNotBlank(dto.getSpuNo())) {
            SpuEntity spu = spuDao.findBySpuNo(merchantNo, dto.getSpuNo());
            if (spu != null) {
                throw new CustomException("条码商品信息已存在，不能重复添加");
            }
        }
        if (StringUtils.isBlank(dto.getSpuCode())) {
            dto.setSpuCode(leafCodeService.generateSpuCode(merchantNo));
        }
        if (StringUtils.isBlank(dto.getSpuNo())) {
            dto.setSpuNo(dto.getSpuCode());
        }
        if (dto.getSkus().size() == 1) {
            dto.getSkus().forEach(x -> {
                if (StringUtils.isBlank(x.getSkuCode())) {
                    x.setSkuCode(dto.getSpuCode());
                }
                if (StringUtils.isBlank(x.getSkuNo())) {
                    x.setSkuNo(x.getSkuCode());
                }
            });
        }
        SpuEntity entity = new SpuEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setPictures(JsonUtil.toJSONString(dto.getPictures()));
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        spuDao.save(entity);

        if (CollUtil.isNotEmpty(dto.getSkus())) {
            // 批量保存sku信息
            List<StockSkuDto> skus = skuService.batchEdit(merchantNo, entity.getId(), dto.getSkus());

            // 商品资料，门店库存更新到库存表中
            StockSpuDto stockSpuDto = new StockSpuDto();
            BeanUtils.copyProperties(entity, stockSpuDto);
            stockSpuDto.setSpuId(entity.getId());
            stockSpuDto.setSyncStoreNo(dto.getSyncStoreNo());
            stockSpuDto.setStatus(entity.getStatus());
            stockSpuDto.setSkus(skus);
            stockSpuService.batchEdit(merchantNo, stockSpuDto);
        }
        String key = "spu:syncStoreNo:" + entity.getId();
        redisService.set(key, String.valueOf(dto.getSyncStoreNo()), 8, TimeUnit.HOURS);
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(long merchantNo, String accountId, String id, SpuDto dto) {
        SpuEntity spu = spuDao.findById(merchantNo, id);
        if (spu == null) {
            throw new CustomException("商品信息错误");
        }
        SpuEntity entity = new SpuEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setMerchantNo(merchantNo);
        entity.setPictures(JsonUtil.toJSONString(dto.getPictures()));
        entity.setUpdateTime(LocalDateTime.now());
        spuDao.update(entity);
        List<StockSkuDto> skus = skuService.batchEdit(merchantNo, id, dto.getSkus());

        // 商品资料，门店库存更新到库存表中
        StockSpuDto stockSpuDto = new StockSpuDto();
        BeanUtils.copyProperties(entity, stockSpuDto);
        stockSpuDto.setSpuId(entity.getId());
        stockSpuDto.setSyncStoreNo(dto.getSyncStoreNo());
        stockSpuDto.setStatus(spu.getStatus());
        stockSpuDto.setSkus(skus);
        stockSpuService.batchEdit(merchantNo, stockSpuDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editStatus(long merchantNo, String accountId, String id, int status) {
        if (IEnum.getByCode(status, CommonStatus.class) == null) {
            throw new CustomException("状态信息错误");
        }
        spuDao.updateState(merchantNo, id, status);

        List<MerchantStoreEntity> stores = merchantStoreService.findAllByMerchantNo(merchantNo);
        stores.forEach(store -> {
            stockSpuService.editGoodsStatus(merchantNo, store.getStoreNo(), id, status);
            if (status == CommonStatus.DISABLED.getCode()) {
                stockSpuService.editStatus(merchantNo, store.getStoreNo(), id, status);
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void importCreate(long merchantNo, String accountId, SpuDto dto) {
        SpuEntity spuEntity = spuDao.findByName(merchantNo, dto.getName());
        if (spuEntity != null) {
            importEdit(merchantNo, accountId, spuEntity.getId(), dto);
            return;
        }
        create(merchantNo, accountId, dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void importEdit(long merchantNo, String accountId, String id, SpuDto dto) {
        SpuEntity entity = new SpuEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setPictures(JsonUtil.toJSONString(dto.getPictures()));
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        spuDao.update(entity);
        skuService.deleteBySpuId(id);
        skuSpecService.deleteBySpuId(id);
        List<StockSkuDto> skus = skuService.batchEdit(merchantNo, id, dto.getSkus());

        // 商品资料，门店库存更新到库存表中
        StockSpuDto stockSpuDto = new StockSpuDto();
        BeanUtils.copyProperties(entity, stockSpuDto);
        stockSpuDto.setSpuId(entity.getId());
        stockSpuDto.setSyncStoreNo(dto.getSyncStoreNo());
        stockSpuDto.setStatus(entity.getStatus());
        stockSpuDto.setSkus(skus);
        stockSpuService.batchEdit(merchantNo, stockSpuDto);
    }

    /**
     * 商品导入
     *
     * @param merchantNo
     * @param accountId
     * @param syncStoreNo 默认库存同步门店号
     * @param file
     */
    @Transactional(rollbackFor = Exception.class)
    public void spuImport(long merchantNo, String accountId, long syncStoreNo, MultipartFile file) {
        List<StorageSpuImportDto> imports = ExcelUtils.read(file, StorageSpuImportDto.class);
        valid(imports);
        convertImportDto(imports);

        List<SpuDto> spus = new ArrayList<>();
        for (StorageSpuImportDto dto : imports) {
            // 规格信息
            List<SkuSpecDto> specs = initSkuImportSpecName(merchantNo, dto.getSpecKeyOne(),
                    dto.getSpecValueOne(), dto.getSpecKeyTwo(), dto.getSpecValueTwo(),
                    dto.getSpecKeyThree(), dto.getSpecValueThree(), dto.getSpecKeyFour(),
                    dto.getSpecValueFour());

            if (CollectionUtils.isEmpty(spus)) {
                SpuDto spuDto = new SpuDto();
                convertSpuDto(merchantNo, dto, spuDto, syncStoreNo);
                spus.add(spuDto);
            } else {
                SpuDto spuDto = spus.get(spus.size() - 1);
                if (!dto.getName().equals(spuDto.getName())) {
                    spuDto = new SpuDto();
                    convertSpuDto(merchantNo, dto, spuDto, syncStoreNo);
                    spus.add(spuDto);
                }
            }

            SpuDto spuDto = spus.get(spus.size() - 1);
            if (StringUtils.isBlank(dto.getSpecName())) {
                dto.setSpecName(spuDto.getName());
                if (!CollectionUtils.isEmpty(specs)) {
                    String specName = specs.stream().map(SkuSpecDto::getSpecValueName).collect(Collectors.joining("_"));
                    dto.setSpecName(spuDto.getName() + "_" + specName);
                }
            }
            SkuDto skuDto = new SkuDto();
            skuDto.setSkuCode(dto.getSkuCode());
            skuDto.setSkuNo(dto.getSkuNo());
            skuDto.setSkuName(dto.getSpecName());
            skuDto.setPurchaseAmount(dto.getPurchaseAmount());
            skuDto.setReferenceAmount(dto.getReferenceAmount());
            skuDto.setSkuWeight(dto.getSkuWeight());
            skuDto.setSellStock(dto.getSellStock());
            skuDto.setSpecs(specs);

            List<SkuDto> skus = spuDto.getSkus();
            skus.add(skuDto);
            spuDto.setSkus(skus);
        }
        spus.forEach(x -> {
            importCreate(merchantNo, accountId, x);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleted(long merchantNo, String id) {
        SpuEntity entity = spuDao.findById(merchantNo, id);
        if (entity == null) {
            throw new CustomException(ResultCode.BAD_REQUEST, "商品信息为空");
        }
        if (entity.getMerchantNo() == 0 || entity.getMerchantNo() != merchantNo) {
            throw new CustomException(ResultCode.BAD_REQUEST, "用户信息错误");
        }
        spuDao.delete(merchantNo, id);
        skuService.deleteBySpuId(id);
    }

    public SpuBaseVo findBaseById(long merchantNo, String id) {
        SpuEntity spu = spuDao.findById(merchantNo, id);
        if (spu == null) {
            return null;
        }
        return convertBaseInfo(spu);
    }

    /**
     * 获取基础信息
     *
     * @param merchantNo
     * @param id
     * @return
     */
    public SpuDetailVo findDetailById(long merchantNo, String id) {
        SpuEntity spu = spuDao.findById(merchantNo, id);
        if (spu == null) {
            return null;
        }
        return convertDetail(spu);
    }

    /**
     * 获取详细信息
     *
     * @param merchantNo
     * @param spuNo
     * @return
     */
    public SpuDetailVo findDetailBySpuNo(long merchantNo, String spuNo) {
        SpuEntity spu = spuDao.findBySpuNo(merchantNo, spuNo);
        if (spu != null) {
            return convertDetail(spu);
        }
        GoodsEntity entity = goodsService.findByBarcode(spuNo);
        SpuDetailVo vo = new SpuDetailVo();
        vo.setSpuNo(entity.getBarcode());
        vo.setSpuCode(entity.getBarcode());
        vo.setName(entity.getName());
        vo.setPictures(JsonUtil.toList(entity.getImage(), String.class));
        SkuVo skuVo = new SkuVo();
        skuVo.setSkuNo(entity.getBarcode());
        skuVo.setSkuCode(entity.getBarcode());
        skuVo.setSkuName(entity.getName() + entity.getSpecification());
        skuVo.setReferenceAmount(AmountUtil.parseBigDecimal(entity.getPrice()));
        vo.setSkus(Collections.singletonList(skuVo));
        return vo;
    }

    public List<SpuBaseVo> findBaseByKeyword(long merchantNo, String keyword) {
        SpuQuery query = new SpuQuery();
        query.setSearchKey(keyword);
        query.setStatus(CommonStatus.ENABLED.getCode());
        List<SpuEntity> spus = spuDao.findAllByQuery(merchantNo, query);
        return spus.stream().map(this::convertBaseInfo).collect(Collectors.toList());
    }

    /**
     * 获取分页列表
     *
     * @param merchantNo
     * @param query
     * @return
     */
    public PageVo<SpuListVo> page(long merchantNo, SpuPageQuery query) {
        query.setCategoryIds(convertCategoryIds(merchantNo, query.getCategoryIds(), query.getCategoryId()));
        int total = spuDao.countPage(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<SpuEntity> list = spuDao.findPage(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, convertList(merchantNo, list));
    }

    public List<SpuListVo> list(long merchantNo, SpuQuery query) {
        query.setCategoryIds(convertCategoryIds(merchantNo, query.getCategoryIds(), query.getCategoryId()));
        List<SpuEntity> spus = spuDao.findAllByQuery(merchantNo, query);
        if (CollectionUtils.isEmpty(spus)) {
            return Collections.emptyList();
        }
        return convertList(merchantNo, spus);
    }

    public void repairSpec() {
        List<SkuEntity> skus = skuService.findAll();
        skus.forEach(sku -> {
            List<SkuSpecVo> skuSpecs = skuSpecService.findAllBySkuId(sku.getId());
            if (CollUtil.isNotEmpty(skuSpecs)) {
                String joinSpec = skuSpecs.stream()
                        .map(SkuSpecVo::getSpecValueId)
                        .sorted(Comparator.comparing(x -> x))
                        .collect(Collectors.joining("_"));

                sku.setJoinSpec(joinSpec);
                skuDao.update(sku);

                List<StockSkuEntity> stockSkuVos = stockSkuService.findListBySkuId(sku.getId());
                stockSkuVos.forEach(stockSku -> {
                    stockSku.setJoinSpec(joinSpec);
                    stockSkuDao.edit(stockSku);
                });
            }
        });
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
        categoryIds.addAll(list.stream().map(CategoryEntity::getId).collect(Collectors.toList()));
        return categoryIds;
    }

    private void valid(List<StorageSpuImportDto> imports) {
        if (CollectionUtils.isEmpty(imports)) {
            throw new CustomException("没有获取到文件内容");
        }
        StorageSpuImportDto dto = imports.get(0);
        if (StringUtils.isBlank(dto.getName())) {
            throw new CustomException("首行商品名不能为空");
        }
        if (StringUtils.isBlank(dto.getCategoryOne())) {
            throw new CustomException("首行一级分类不能为空");
        }
        if (StringUtils.isBlank(dto.getUnitName())) {
            throw new CustomException("首行单位名不能为空");
        }
    }

    private void convertImportDto(List<StorageSpuImportDto> imports) {
        StorageSpuImportDto tempDto = imports.stream().findFirst().orElse(null);
        for (StorageSpuImportDto dto : imports) {
            if (StringUtils.isBlank(dto.getName()) || dto.getName().equals(tempDto.getName())) {
                dto.setName(tempDto.getName());
                dto.setSpuCode(tempDto.getSpuCode());
                dto.setSpuNo(tempDto.getSpuNo());
                dto.setManyBarCode(tempDto.getManyBarCode());
            }
            if (StringUtils.isBlank(dto.getCategoryOne())) {
                dto.setCategoryOne(tempDto.getCategoryOne());
            }
            if (StringUtils.isBlank(dto.getCategoryTwo())) {
                dto.setCategoryTwo(tempDto.getCategoryTwo());
            }
            if (StringUtils.isBlank(dto.getUnitName())) {
                dto.setUnitName(tempDto.getUnitName());
                dto.setUnitType(tempDto.getUnitType());
            }
            if (StringUtils.isBlank(dto.getPictures())) {
                dto.setPictures(tempDto.getPictures());
            }
            if (StringUtils.isBlank(dto.getSpecKeyOne())) {
                dto.setSpecKeyOne(tempDto.getSpecKeyOne());
            }
            if (StringUtils.isBlank(dto.getSpecValueOne())) {
                dto.setSpecValueOne(tempDto.getSpecValueOne());
            }
            if (StringUtils.isBlank(dto.getSpecKeyTwo())) {
                dto.setSpecKeyTwo(tempDto.getSpecKeyTwo());
            }
            if (StringUtils.isBlank(dto.getSpecValueTwo())) {
                dto.setSpecValueTwo(tempDto.getSpecValueTwo());
            }
            if (StringUtils.isBlank(dto.getSpecKeyThree())) {
                dto.setSpecKeyThree(tempDto.getSpecKeyThree());
            }
            if (StringUtils.isBlank(dto.getSpecValueThree())) {
                dto.setSpecValueThree(tempDto.getSpecValueThree());
            }
            if (StringUtils.isBlank(dto.getSpecKeyFour())) {
                dto.setSpecKeyFour(tempDto.getSpecKeyFour());
            }
            if (StringUtils.isBlank(dto.getSpecValueFour())) {
                dto.setSpecValueFour(tempDto.getSpecValueFour());
            }
            tempDto = dto;
        }
    }

    private void convertSpuDto(long merchantNo, StorageSpuImportDto dto, SpuDto spuDto, long syncStoreNo) {
        String categoryId = initImportCategory(merchantNo, dto.getCategoryOne(), dto.getCategoryTwo());
        String unitId = initImportSpuUnit(merchantNo, dto.getUnitName(), dto.getUnitType());
        if (StringUtils.isBlank(unitId)) {
            throw new CustomException("单位名称不能为空");
        }
        spuDto.setName(dto.getName());
        spuDto.setSpuCode(dto.getSpuCode());
        spuDto.setSpuNo(dto.getSpuNo());
        spuDto.setManyCode(dto.getManyBarCode());
        spuDto.setCategoryId(categoryId);
        spuDto.setUnitId(unitId);
        spuDto.setSyncStoreNo(syncStoreNo);
        if (StringUtils.isNotBlank(dto.getPictures())) {
            spuDto.setPictures(Arrays.asList(dto.getPictures().split(",")));
        }
    }

    private String initImportCategory(long merchantNo, String categoryOne, String categoryTwo) {
        if (StringUtils.isBlank(categoryOne)) {
            throw new CustomException("商品分类不能为空");
        }
        String categoryId = initSubCategory(merchantNo, categoryOne, null);
        if (StringUtils.isBlank(categoryTwo)) {
            return categoryId;
        }
        return initSubCategory(merchantNo, categoryTwo, categoryId);
    }

    private String initSubCategory(long merchantNo, String classifyName, String pid) {
        if (StringUtils.isBlank(pid)) {
            CategoryEntity category = categoryService.findByName(merchantNo, classifyName, null);
            if (category == null) {
                CategoryDto createDto = new CategoryDto();
                createDto.setName(classifyName);
                createDto.setSort(1);
                category = categoryService.create(merchantNo, createDto);
            }
            return category.getId();
        }
        CategoryEntity category = categoryService.findByName(merchantNo, classifyName, pid);
        if (category == null) {
            CategoryDto createDto = new CategoryDto();
            createDto.setPid(pid);
            createDto.setName(classifyName);
            createDto.setSort(1);
            category = categoryService.create(merchantNo, createDto);
        }
        return category.getId();
    }

    private String initImportSpuUnit(long merchantNo, String unitName, String unitType) {
        if (StringUtils.isBlank(unitName)) {
            unitName = "件";
        }
        UnitVo unitVo = unitService.findByMerchantNoAndName(merchantNo, unitName);
        if (unitVo != null) {
            return unitVo.getId();
        }
        int type = UnitType.WEIGHT.getCode();
        if (StringUtils.isBlank(unitType) || !"计重".equals(unitType)) {
            type = UnitType.NUMBER.getCode();
        }
        UnitDto dto = new UnitDto();
        dto.setName(unitName);
        dto.setType(type);
        UnitEntity unit = unitService.create(merchantNo, dto);
        if (unit != null) {
            return unit.getId();
        }
        return null;
    }

    private List<SkuSpecDto> initSkuImportSpecName(long merchantNo,
                                                   String specKeyOne, String specValueOne,
                                                   String specKeyTwo, String specValueTwo,
                                                   String specKeyThree, String specValueThree,
                                                   String specKeyFour, String specValueFour) {
        List<SkuSpecDto> list = new ArrayList<>();
        SkuSpecDto dto1 = initSkuImportSpecName(merchantNo, specKeyOne, specValueOne);
        if (dto1 == null) {
            return list;
        }
        list.add(dto1);

        SkuSpecDto dto2 = initSkuImportSpecName(merchantNo, specKeyTwo, specValueTwo);
        if (dto2 == null) {
            return list;
        }
        list.add(dto2);

        SkuSpecDto dto3 = initSkuImportSpecName(merchantNo, specKeyThree, specValueThree);
        if (dto3 == null) {
            return list;
        }
        list.add(dto3);

        SkuSpecDto dto4 = initSkuImportSpecName(merchantNo, specKeyFour, specValueFour);
        if (dto4 == null) {
            return list;
        }
        list.add(dto4);
        return list;
    }

    private SkuSpecDto initSkuImportSpecName(long merchantNo, String specKeyName, String specValueName) {
        if (StringUtils.isAnyBlank(specKeyName, specValueName)) {
            return null;
        }
        SkuSpecDto model = new SkuSpecDto();
        SpecEntity specKey = specService.findChildByName(merchantNo, specKeyName, null);
        if (specKey == null) {
            SpecDto specKeyDto = new SpecDto();
            specKeyDto.setName(specKeyName);
            specKey = specService.create(merchantNo, specKeyDto);
        }
        model.setSpecId(specKey.getId());
        model.setSpecName(specKey.getName());

        SpecEntity specValue = specService.findChildByName(merchantNo, specKeyName, specKey.getId());
        if (specValue == null) {
            SpecDto specValueDto = new SpecDto();
            specValueDto.setName(specValueName);
            specValueDto.setPid(specKey.getId());
            specValue = specService.create(merchantNo, specValueDto);
        }
        model.setSpecValueId(specValue.getId());
        model.setSpecValueName(specValue.getName());
        return model;
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

    private List<String> getCategoryIds(long merchantNo, String categoryId) {
        if (StringUtils.isBlank(categoryId)) {
            return Collections.emptyList();
        }
        List<String> categoryIds = new ArrayList<>();
        CategoryEntity entity = categoryService.findById(merchantNo, categoryId);
        if (entity == null) {
            return Collections.emptyList();
        }
        categoryIds.add(entity.getId());
        if (StringUtils.isNotBlank(entity.getPid())) {
            CategoryEntity parentEntity = categoryService.findById(merchantNo, entity.getPid());
            if (parentEntity == null) {
                return categoryIds;
            }
            categoryIds.add(parentEntity.getId());
        }
        return categoryIds;
    }

    private SpuDetailVo convertDetail(SpuEntity spu) {
        if (spu == null) {
            return null;
        }
        UnitEntity unit = unitService.findById(spu.getMerchantNo(), spu.getUnitId());
        SpuDetailVo vo = new SpuDetailVo();
        BeanUtils.copyProperties(spu, vo);
        vo.setCategoryIds(getCategoryIds(spu.getMerchantNo(), spu.getCategoryId()));
        vo.setCategoryName(getCategoryName(spu.getMerchantNo(), spu.getCategoryId()));
        vo.setLastCategoryName(getLastCategoryName(spu.getMerchantNo(), spu.getCategoryId()));
        vo.setPictures(JsonUtil.toList(spu.getPictures(), String.class));
        vo.setSkus(skuService.findAllBySpuId(spu.getId()));
        if (unit != null) {
            vo.setUnitName(unit.getName());
            vo.setUnitType(unit.getType());
        }
        return vo;
    }

    private SpuBaseVo convertBaseInfo(SpuEntity spu) {
        if (spu == null) {
            return null;
        }
        UnitEntity unit = unitService.findById(spu.getMerchantNo(), spu.getUnitId());
        SpuBaseVo vo = new SpuBaseVo();
        BeanUtils.copyProperties(spu, vo);
        vo.setCategoryIds(getCategoryIds(spu.getMerchantNo(), spu.getCategoryId()));
        vo.setCategoryName(getCategoryName(spu.getMerchantNo(), spu.getCategoryId()));
        vo.setLastCategoryName(getLastCategoryName(spu.getMerchantNo(), spu.getCategoryId()));
        vo.setPictures(JsonUtil.toList(spu.getPictures(), String.class));
        if (unit != null) {
            vo.setUnitName(unit.getName());
            vo.setUnitType(unit.getType());
        }
        return vo;
    }

    private List<SpuListVo> convertList(long merchantNo, List<SpuEntity> spus) {
        if (CollectionUtils.isEmpty(spus)) {
            return Collections.emptyList();
        }
        List<String> unitIds = spus.stream().map(SpuEntity::getUnitId).distinct().collect(Collectors.toList());
        List<UnitVo> units = unitService.findAllByIds(merchantNo, unitIds);

        return spus.stream().map(spu -> {
            SpuListVo vo = new SpuListVo();
            BeanUtils.copyProperties(spu, vo);
            vo.setPictures(JsonUtil.toList(spu.getPictures(), String.class));
            vo.setCategoryName(getCategoryName(spu.getMerchantNo(), spu.getCategoryId()));
            vo.setLastCategoryName(getLastCategoryName(spu.getMerchantNo(), spu.getCategoryId()));
            units.stream().filter(x -> x.getId().equals(vo.getUnitId()))
                    .findFirst().ifPresent(x -> vo.setUnitName(x.getName()));
            return vo;
        }).collect(Collectors.toList());
    }
}
