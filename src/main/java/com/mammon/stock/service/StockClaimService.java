package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.exception.CustomException;
import com.mammon.goods.domain.entity.UnitEntity;
import com.mammon.goods.domain.enums.SpuCountedType;
import com.mammon.goods.service.UnitService;
import com.mammon.stock.dao.StockClaimDao;
import com.mammon.stock.domain.dto.*;
import com.mammon.stock.domain.entity.StockClaimEntity;
import com.mammon.stock.domain.entity.StockClaimSkuEntity;
import com.mammon.stock.domain.entity.StockSettingEntity;
import com.mammon.stock.domain.entity.StockSpuEntity;
import com.mammon.stock.domain.enums.StockRecordType;
import com.mammon.stock.domain.query.StockClaimPageQuery;
import com.mammon.stock.domain.query.StockClaimRuleQuery;
import com.mammon.stock.domain.vo.*;
import com.mammon.utils.StockUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/3/12 15:09
 */
@Service
public class StockClaimService {

    @Resource
    private StockClaimDao stockClaimDao;

    @Resource
    private StockClaimSkuService stockClaimSkuService;

    @Resource
    private UnitService unitService;

    @Resource
    private StockSkuService stockSkuService;

    @Resource
    private StockRecordService stockRecordService;

    @Resource
    private StockSettingService stockSettingService;
    @Autowired
    private StockSpuService stockSpuService;

    @Transactional(rollbackFor = Exception.class)
    public void save(long merchantNo, StockClaimDto dto) {
        StockSpuEntity largeSpu = stockSpuService.findById(dto.getLargeSpuId());
        StockSpuEntity smallSpu = stockSpuService.findById(dto.getSmallSpuId());
        if (largeSpu == null || smallSpu == null) {
            throw new CustomException("商品信息错误");
        }
        if (largeSpu.getCountedType() == SpuCountedType.YES.getCode() || smallSpu.getCountedType() == SpuCountedType.YES.getCode()) {
            throw new CustomException("服务商品不能参与组装拆包操作");
        }

        StockClaimEntity largeClaim = stockClaimDao.findByLargeSpuId(merchantNo, dto.getLargeSpuId());
        if (largeClaim != null) {
            throw new CustomException("此商品拆分规则已存在，不能重复添加");
        }
        StockClaimEntity smallClaim = stockClaimDao.findBySmallSpuId(merchantNo, dto.getSmallSpuId());
        if (smallClaim != null) {
            throw new CustomException("此商品拆分规则已存在，不能重复添加");
        }
        StockClaimEntity entity = new StockClaimEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        stockClaimDao.save(entity);
        stockClaimSkuService.batchSave(entity.getId(), dto.getSkus());
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(long merchantNo, String id, StockClaimDto dto) {
        StockSpuEntity largeSpu = stockSpuService.findById(dto.getLargeSpuId());
        StockSpuEntity smallSpu = stockSpuService.findById(dto.getSmallSpuId());
        if (largeSpu == null || smallSpu == null) {
            throw new CustomException("商品信息错误");
        }
        if (largeSpu.getCountedType() == SpuCountedType.YES.getCode() || smallSpu.getCountedType() == SpuCountedType.YES.getCode()) {
            throw new CustomException("服务商品不能参与组装拆包操作");
        }

        StockClaimEntity largeClaim = stockClaimDao.findByLargeSpuId(merchantNo, dto.getLargeSpuId());
        if (largeClaim != null && !largeClaim.getId().equals(id)) {
            throw new CustomException("此商品拆分规则已存在");
        }
        StockClaimEntity smallClaim = stockClaimDao.findBySmallSpuId(merchantNo, dto.getSmallSpuId());
        if (smallClaim != null && !smallClaim.getId().equals(id)) {
            throw new CustomException("此商品拆分规则已存在");
        }
        StockClaimEntity entity = new StockClaimEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUpdateTime(LocalDateTime.now());
        stockClaimDao.edit(entity);
        stockClaimSkuService.deleteBySplitId(id);
        stockClaimSkuService.batchSave(id, dto.getSkus());
    }

    public void editStatus(String id, int status) {
        CommonStatus enumStatus = IEnum.getByCode(status, CommonStatus.class);
        if (enumStatus == null) {
            throw new CustomException("商品组装拆包信息错误");
        }
        stockClaimDao.editStatus(id, status);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        stockClaimDao.deleteById(id);
        stockClaimSkuService.deleteBySplitId(id);
    }

    /**
     * 组合，小包装商品出库，组合大包装后入库
     * <p>
     * 要求：组合数量必须是规则系数的倍数
     *
     * @param merchantNo
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public StockClaimAutomaticVo make(long merchantNo, long storeNo, String accountId, StockClaimMakeDto dto) {
        // 根据小包装spuId获取到组合拆包规则
        // 根据规则系数，换算组合后大包数量
        StockClaimEntity claim = stockClaimDao.findBySmallSpuId(merchantNo, dto.getSmallSpuId());
        if (claim == null || claim.getStatus() == CommonStatus.DISABLED.getCode()) {
            throw new CustomException("商品组装规则不存在");
        }
        StockClaimMakeValidVo makeValidVo = makeValid(merchantNo, storeNo, dto.getSmallSkuId(), dto.getMakeQuantity(), claim);
        if (!makeValidVo.isResult()) {
            throw new CustomException(makeValidVo.getMsg());
        }
        StockClaimSkuEntity claimSku = makeValidVo.getClaimSku();
        // 出库数量
        BigDecimal outQuantity = dto.getMakeQuantity();
        // 组合后入库数量
        BigDecimal inQuantity = StockUtil.parseBigDecimal(StockUtil.parse(dto.getMakeQuantity()) / claim.getMultiple());

        // 出入库操作
        stockSkuService.editSellStockExpend(merchantNo, storeNo, dto.getSmallSkuId(), outQuantity);
        stockSkuService.editSellStockReplenish(merchantNo, storeNo, dto.getLargeSkuId(), inQuantity);

        // 出入库记录
        stockRecord(merchantNo, storeNo, accountId, StockRecordType.CLAIM_OUT, dto.getSmallSpuId(), dto.getSmallSkuId(), outQuantity);
        stockRecord(merchantNo, storeNo, accountId, StockRecordType.CLAIM_IN, dto.getLargeSpuId(), dto.getLargeSkuId(), inQuantity);

        UnitEntity largeUnit = unitService.findById(merchantNo, claim.getLargeUnitId());
        UnitEntity smallUnit = unitService.findById(merchantNo, claim.getSmallUnitId());

        StockClaimAutomaticVo vo = new StockClaimAutomaticVo();
        vo.setType(1);
        vo.setSmallSpuId(claimSku.getSmallSpuId());
        vo.setSmallSkuId(claimSku.getSmallSkuId());
        vo.setSmallSkuName(claimSku.getSmallSkuName());
        vo.setSmallQuantity(outQuantity);
        vo.setLargeSpuId(claimSku.getLargeSpuId());
        vo.setLargeSkuId(claimSku.getLargeSkuId());
        vo.setLargeSkuName(claimSku.getLargeSkuName());
        vo.setLargeQuantity(inQuantity);

        vo.setLargeUnitId(claim.getLargeUnitId());
        vo.setSmallUnitId(claim.getSmallUnitId());
        if (largeUnit != null) {
            vo.setLargeUnitName(largeUnit.getName());
        }
        if (smallUnit != null) {
            vo.setSmallUnitName(smallUnit.getName());
        }
        return vo;
    }

    /**
     * 拆分，大包装商品出库，拆分小包装后入库
     *
     * @param merchantNo
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public StockClaimAutomaticVo split(long merchantNo, long storeNo, String accountId, StockClaimSplitDto dto) {
        StockClaimEntity claim = stockClaimDao.findByLargeSpuId(merchantNo, dto.getLargeSpuId());
        if (claim == null || claim.getStatus() == CommonStatus.DISABLED.getCode()) {
            throw new CustomException("商品组装规则不存在");
        }
        StockClaimSplitValidVo splitValidVo = splitValid(merchantNo, storeNo, dto.getLargeSkuId(), dto.getSplitQuantity(), claim);
        if (!splitValidVo.isResult()) {
            throw new CustomException(splitValidVo.getMsg());
        }
        StockClaimSkuEntity claimSku = splitValidVo.getClaimSku();
        // 出库数量
        BigDecimal outQuantity = dto.getSplitQuantity();
        // 组合后入库数量
        BigDecimal inQuantity = StockUtil.parseBigDecimal(StockUtil.parse(dto.getSplitQuantity()) * claim.getMultiple());
        // 出入库操作
        stockSkuService.editSellStockExpend(merchantNo, storeNo, dto.getLargeSkuId(), outQuantity);
        stockSkuService.editSellStockReplenish(merchantNo, storeNo, dto.getSmallSkuId(), inQuantity);

        // 出入库记录
        stockRecord(merchantNo, storeNo, accountId, StockRecordType.CLAIM_OUT, dto.getLargeSpuId(), dto.getLargeSkuId(), outQuantity);
        stockRecord(merchantNo, storeNo, accountId, StockRecordType.CLAIM_IN, dto.getSmallSpuId(), dto.getSmallSkuId(), inQuantity);

        UnitEntity largeUnit = unitService.findById(merchantNo, claim.getLargeUnitId());
        UnitEntity smallUnit = unitService.findById(merchantNo, claim.getSmallUnitId());

        StockClaimAutomaticVo vo = new StockClaimAutomaticVo();
        vo.setType(2);
        vo.setLargeSpuId(claimSku.getLargeSpuId());
        vo.setLargeSkuId(claimSku.getLargeSkuId());
        vo.setLargeSkuName(claimSku.getLargeSkuName());
        vo.setLargeQuantity(outQuantity);
        vo.setSmallSpuId(claimSku.getSmallSpuId());
        vo.setSmallSkuId(claimSku.getSmallSkuId());
        vo.setSmallSkuName(claimSku.getSmallSkuName());
        vo.setSmallQuantity(inQuantity);

        vo.setLargeUnitId(claim.getLargeUnitId());
        vo.setSmallUnitId(claim.getSmallUnitId());
        if (largeUnit != null) {
            vo.setLargeUnitName(largeUnit.getName());
        }
        if (smallUnit != null) {
            vo.setSmallUnitName(smallUnit.getName());
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public StockClaimAutomaticVo automatic(long merchantNo, long storeNo, String accountId, StockClaimAutomaticDto dto) {
        StockSettingEntity setting = stockSettingService.findByMerchantNo(merchantNo);
        if (setting == null || setting.getClaim() == CommonStatus.DISABLED.getCode()) {
            throw new CustomException("没有开启自动拆包设置");
        }

        StockClaimMakeValidVo makeValidVo = null;
        StockClaimSplitValidVo splitValidVo = null;
        // 拆包
        // 根据拆包目标spu获取拆包规则
        StockClaimEntity claim = stockClaimDao.findBySmallSpuId(merchantNo, dto.getSpuId());
        if (claim != null && claim.getStatus() != CommonStatus.DISABLED.getCode()) {
            // 拆包数量=拆包规则系数*销售数量
            BigDecimal splitQuantity = BigDecimal.ONE;
            splitValidVo = splitAutomaticValid(merchantNo, storeNo, dto.getSkuId(), splitQuantity, claim);
            if (splitValidVo.isResult()) {
                StockClaimSkuEntity claimSku = splitValidVo.getClaimSku();
                StockClaimSplitDto splitDto = new StockClaimSplitDto();
                splitDto.setSmallSpuId(claimSku.getSmallSpuId());
                splitDto.setSmallSkuId(claimSku.getSmallSkuId());
                splitDto.setLargeSpuId(claimSku.getLargeSpuId());
                splitDto.setLargeSkuId(claimSku.getLargeSkuId());
                splitDto.setSplitQuantity(splitQuantity);
                return split(merchantNo, storeNo, accountId, splitDto);
            }
        }
        // 组装
        // 根据组装目标获取组装规则
        claim = stockClaimDao.findByLargeSpuId(merchantNo, dto.getSpuId());
        if (claim != null && claim.getStatus() != CommonStatus.DISABLED.getCode()) {
            // 组装商品数量 = 组装系数x销售数量
            BigDecimal makeQuantity = BigDecimal.ONE;
            BigDecimal quantity = StockUtil.parseBigDecimal(StockUtil.parse(makeQuantity) * claim.getMultiple());
            makeValidVo = makeAutomaticValid(merchantNo, storeNo, dto.getSkuId(), quantity, claim);
            if (makeValidVo.isResult()) {
                StockClaimSkuEntity claimSku = makeValidVo.getClaimSku();
                StockClaimMakeDto makeDto = new StockClaimMakeDto();
                makeDto.setLargeSpuId(claimSku.getLargeSpuId());
                makeDto.setLargeSkuId(claimSku.getLargeSkuId());
                makeDto.setSmallSpuId(claimSku.getSmallSpuId());
                makeDto.setSmallSkuId(claimSku.getSmallSkuId());
                makeDto.setMakeQuantity(quantity);
                return make(merchantNo, storeNo, accountId, makeDto);
            }
        }

        if (makeValidVo != null) {
            throw new CustomException(makeValidVo.getMsg());
        }
        if (splitValidVo != null) {
            throw new CustomException(splitValidVo.getMsg());
        }
        throw new CustomException("此商品无对应可组装拆包商品");
    }

    public StockClaimDetailVo findDetailById(String id) {
        StockClaimEntity claim = stockClaimDao.findById(id);
        if (claim == null) {
            return null;
        }
        return convertDetail(claim);
    }

    public StockClaimDetailVo findClaimRule(long merchantNo, StockClaimRuleQuery query) {
        StockClaimEntity claim = null;
        if (StringUtils.isNotBlank(query.getLargeSpuId())) {
            claim = stockClaimDao.findByLargeSpuId(merchantNo, query.getLargeSpuId());
        }
        if (StringUtils.isNotBlank(query.getSmallSpuId())) {
            claim = stockClaimDao.findBySmallSpuId(merchantNo, query.getSmallSpuId());
        }
        if (claim == null) {
            return null;
        }
        return convertDetail(claim);
    }

    public PageVo<StockClaimPageVo> page(long merchantNo, StockClaimPageQuery query) {
        int total = stockClaimDao.countPage(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<StockClaimEntity> list = stockClaimDao.findPage(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<StockClaimPageVo> pageVos = list.stream().map(x -> {
            StockClaimPageVo vo = new StockClaimPageVo();
            BeanUtils.copyProperties(x, vo);
            UnitEntity largeUnit = unitService.findById(merchantNo, vo.getLargeUnitId());
            if (largeUnit != null) {
                vo.setLargeUnitName(largeUnit.getName());
            }
            UnitEntity smallUnit = unitService.findById(merchantNo, vo.getSmallUnitId());
            if (smallUnit != null) {
                vo.setSmallUnitName(smallUnit.getName());
            }
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, pageVos);
    }

    private StockClaimMakeValidVo makeValid(long merchantNo, long storeNo, String skuId, BigDecimal makeQuantity,
                                            StockClaimEntity claim) {
        long quantity = StockUtil.parse(makeQuantity);
        StockClaimMakeValidVo vo = new StockClaimMakeValidVo();
        // 获取组装商品sku
        List<StockClaimSkuEntity> claimSkus = stockClaimSkuService.findAllBySplitId(claim.getId());
        StockClaimSkuEntity claimSku = claimSkus.stream()
                .filter(x -> x.getSmallSkuId().equals(skuId))
                .findFirst().orElse(null);
        if (claimSku == null) {
            vo.setMsg("商品组装规则不存在");
            return vo;
        }
        if (quantity % claim.getMultiple() != 0) {
            vo.setMsg("商品组装数量错误，必须是规则系数的倍数");
            return vo;
        }
        // 检查商品库存
        StockSkuVo stock = stockSkuService.findBySkuId(merchantNo, storeNo, claimSku.getSmallSkuId());
        if (stock == null || StockUtil.parse(stock.getSellStock()) < quantity) {
            vo.setMsg("组装商品库存不足");
            return vo;
        }
        vo.setResult(true);
        vo.setClaimSku(claimSku);
        return vo;
    }

    private StockClaimSplitValidVo splitValid(long merchantNo, long storeNo, String skuId, BigDecimal splitQuantity,
                                              StockClaimEntity claim) {
        long quantity = StockUtil.parse(splitQuantity);
        StockClaimSplitValidVo vo = new StockClaimSplitValidVo();
        // 获取组装商品sku
        List<StockClaimSkuEntity> claimSkus = stockClaimSkuService.findAllBySplitId(claim.getId());
        StockClaimSkuEntity claimSku = claimSkus.stream()
                .filter(x -> x.getLargeSkuId().equals(skuId))
                .findFirst().orElse(null);
        if (claimSku == null) {
            vo.setMsg("商品拆包规则不存在");
            return vo;
        }
        // 检查商品库存
        StockSkuVo stock = stockSkuService.findBySkuId(merchantNo, storeNo, claimSku.getLargeSkuId());
        if (stock == null || StockUtil.parse(stock.getSellStock()) < quantity) {
            vo.setMsg("拆包商品库存不足");
            return vo;
        }
        vo.setResult(true);
        vo.setClaimSku(claimSku);
        return vo;
    }

    /**
     * @param merchantNo
     * @param storeNo
     * @param skuId        组装后sku
     * @param makeQuantity 组装后数量
     * @param claim
     * @return
     */
    private StockClaimMakeValidVo makeAutomaticValid(long merchantNo, long storeNo, String skuId, BigDecimal makeQuantity,
                                                     StockClaimEntity claim) {
        long quantity = StockUtil.parse(makeQuantity);
        StockClaimMakeValidVo vo = new StockClaimMakeValidVo();
        // 获取组装商品sku
        List<StockClaimSkuEntity> claimSkus = stockClaimSkuService.findAllBySplitId(claim.getId());
        StockClaimSkuEntity claimSku = claimSkus.stream()
                .filter(x -> x.getLargeSkuId().equals(skuId))
                .findFirst().orElse(null);
        if (claimSku == null) {
            vo.setMsg("商品组装规则不存在");
            return vo;
        }
        if (quantity % claim.getMultiple() != 0) {
            vo.setMsg("商品组装数量错误，必须是规则系数的倍数");
            return vo;
        }
        // 检查商品库存
        StockSkuVo stock = stockSkuService.findBySkuId(merchantNo, storeNo, claimSku.getSmallSkuId());
        if (stock == null || StockUtil.parse(stock.getSellStock()) < quantity) {
            vo.setMsg("组装商品库存不足");
            return vo;
        }
        vo.setResult(true);
        vo.setClaimSku(claimSku);
        return vo;
    }

    /**
     * @param merchantNo
     * @param storeNo
     * @param skuId         拆包后sku
     * @param splitQuantity 拆包后数量
     * @param claim
     * @return
     */
    private StockClaimSplitValidVo splitAutomaticValid(long merchantNo, long storeNo, String skuId, BigDecimal splitQuantity,
                                                       StockClaimEntity claim) {
        long quantity = StockUtil.parse(splitQuantity);
        StockClaimSplitValidVo vo = new StockClaimSplitValidVo();
        // 获取组装商品sku
        List<StockClaimSkuEntity> claimSkus = stockClaimSkuService.findAllBySplitId(claim.getId());
        StockClaimSkuEntity claimSku = claimSkus.stream()
                .filter(x -> x.getSmallSkuId().equals(skuId))
                .findFirst().orElse(null);
        if (claimSku == null) {
            vo.setMsg("商品拆包规则不存在");
            return vo;
        }
        // 检查商品库存
        StockSkuVo stock = stockSkuService.findBySkuId(merchantNo, storeNo, claimSku.getLargeSkuId());
        if (stock == null || StockUtil.parse(stock.getSellStock()) < quantity) {
            vo.setMsg("拆包商品库存不足");
            return vo;
        }
        vo.setResult(true);
        vo.setClaimSku(claimSku);
        return vo;
    }

    private void stockRecord(long merchantNo, long storeNo, String accountId, StockRecordType recordType,
                             String spuId, String skuId, BigDecimal quantity) {
        StockRecordSkuDto outSkuDto = new StockRecordSkuDto();
        outSkuDto.setSpuId(spuId);
        outSkuDto.setSkuId(skuId);
        outSkuDto.setRecordQuantity(quantity);

        StockRecordDto outDto = new StockRecordDto();
        outDto.setOperationTime(LocalDateTime.now());
        outDto.setType(recordType.getCode());
        outDto.setIoType(recordType.getIoType());
        outDto.setProducts(new ArrayList<>(Collections.singletonList(outSkuDto)));
        stockRecordService.create(merchantNo, storeNo, accountId, outDto);
    }

    private StockClaimDetailVo convertDetail(StockClaimEntity claim) {
        List<StockClaimSkuEntity> skus = stockClaimSkuService.findAllBySplitId(claim.getId());
        List<StockClaimSkuVo> skuVos = skus.stream().map(x -> {
            StockClaimSkuVo skuVo = new StockClaimSkuVo();
            BeanUtils.copyProperties(x, skuVo);
            // 补充详细信息
            return skuVo;
        }).collect(Collectors.toList());

        StockClaimDetailVo vo = new StockClaimDetailVo();
        BeanUtils.copyProperties(claim, vo);
        vo.setSkus(skuVos);

        UnitEntity largeUnit = unitService.findById(claim.getMerchantNo(), vo.getLargeUnitId());
        if (largeUnit != null) {
            vo.setLargeUnitName(largeUnit.getName());
        }
        UnitEntity smallUnit = unitService.findById(claim.getMerchantNo(), vo.getSmallUnitId());
        if (smallUnit != null) {
            vo.setSmallUnitName(smallUnit.getName());
        }
        return vo;
    }
}
