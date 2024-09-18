package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.exception.CustomException;
import com.mammon.goods.domain.enums.SpuCountedType;
import com.mammon.leaf.enums.DocketType;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.clerk.service.AccountService;
import com.mammon.stock.dao.StockAllocateDao;
import com.mammon.stock.domain.entity.StockSpuEntity;
import com.mammon.stock.domain.vo.StockAllocateSkuVo;
import com.mammon.stock.domain.vo.StockAllocateVo;
import com.mammon.stock.domain.vo.StockSkuAllocateVo;
import com.mammon.stock.domain.dto.*;
import com.mammon.stock.domain.entity.StockAllocateEntity;
import com.mammon.stock.domain.entity.StockAllocateProductEntity;
import com.mammon.stock.domain.entity.StockSettingEntity;
import com.mammon.stock.domain.enums.AllocateStatus;
import com.mammon.stock.domain.enums.StockIoType;
import com.mammon.stock.domain.enums.StockRecordType;
import com.mammon.stock.domain.query.StockAllocateQuery;
import com.mammon.stock.domain.query.StockAllocateGateQuery;
import com.mammon.utils.StockUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockAllocateService {

    @Resource
    private StockAllocateDao stockAllocateDao;

    @Resource
    private StockAllocateProductService stockAllocateProductService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private AccountService accountService;

    @Resource
    private StockSkuService stockSkuService;

    @Resource
    private StockRecordService stockRecordService;

    @Resource
    private StockSettingService stockSettingService;

    @Resource
    private LeafCodeService leafCodeService;
    @Autowired
    private StockSpuService stockSpuService;

    @Transactional(rollbackFor = CustomException.class)
    public String create(long merchantNo, long storeNo, String accountId, StockAllocateDto dto) {
        String allocateNo = leafCodeService.generateDocketNo(DocketType.ALLOCATE);
        if (StringUtils.isBlank(allocateNo)) {
            throw new CustomException("调拨单号创建异常");
        }
        List<String> spuIds = dto.getSkus().stream().map(StockAllocateSkuDto::getSpuId).collect(Collectors.toList());
        List<StockSpuEntity> spus = stockSpuService.findListBySpuIds(merchantNo, dto.getOutStoreNo(), spuIds);
        if (spus.stream().anyMatch(x -> x.getCountedType() == SpuCountedType.CAN.getCode())) {
            throw new CustomException("服务商品不能参与库存调拨");
        }

        int status = AllocateStatus.WAIT_OUT.getCode();
        StockSettingEntity setting = stockSettingService.findByMerchantNo(merchantNo);
        if (setting != null && setting.getAllocateExamine() == CommonStatus.ENABLED.getCode()) {
            status = AllocateStatus.WAIT_EXAMINE.getCode();
        }
        StockAllocateEntity entity = new StockAllocateEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setAllocateNo(allocateNo);
        entity.setInStoreNo(dto.getInStoreNo());
        entity.setOutStoreNo(dto.getOutStoreNo());
        entity.setStatus(status);
        entity.setRemark(dto.getRemark());
        entity.setOperationId(accountId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        stockAllocateDao.save(entity);
        stockAllocateProductService.batchCreate(allocateNo, dto.getSkus());
        return entity.getId();
    }

    @Transactional(rollbackFor = CustomException.class)
    public void edit(long merchantNo, long storeNo, String accountId, String id, StockAllocateDto dto) {
        StockAllocateEntity allocate = stockAllocateDao.findById(merchantNo, id);
        if (allocate == null) {
            throw new CustomException("调拨信息错误");
        }
        if (allocate.getStatus() != AllocateStatus.REJECT_EXAMINE.getCode() &&
                allocate.getStatus() != AllocateStatus.WAIT_EXAMINE.getCode()) {
            throw new CustomException("当前调拨状态不允许修改");
        }
        int status = AllocateStatus.WAIT_OUT.getCode();
        StockSettingEntity setting = stockSettingService.findByMerchantNo(merchantNo);
        if (setting != null && setting.getPurchaseOrderExamine() == CommonStatus.ENABLED.getCode()) {
            status = AllocateStatus.WAIT_EXAMINE.getCode();
        }
        allocate.setInStoreNo(dto.getInStoreNo());
        allocate.setOutStoreNo(dto.getOutStoreNo());
        allocate.setRemark(dto.getRemark());
        stockAllocateDao.update(allocate);
        stockAllocateDao.updateStatus(merchantNo, id, status, null);
        stockAllocateProductService.batchEdit(allocate.getAllocateNo(), dto.getSkus());
    }

    public void examine(long merchantNo, long storeNo, String accountId, String id, StockAllocateExamineDto dto) {
        StockAllocateEntity allocate = stockAllocateDao.findById(merchantNo, id);
        if (allocate == null) {
            throw new CustomException("调拨信息异常");
        }
        if (dto.getStatus() != AllocateStatus.WAIT_OUT.getCode() && dto.getStatus() != AllocateStatus.REJECT_EXAMINE.getCode()) {
            throw new CustomException("调拨信息审核状态错误");
        }
        stockAllocateDao.updateStatus(merchantNo, id, dto.getStatus(), dto.getErrDesc());
    }

    public void getClose(long merchantNo, long storeNo, String accountId, String id) {
        StockAllocateEntity allocate = stockAllocateDao.findById(merchantNo, id);
        if (allocate == null) {
            throw new CustomException("调拨信息错误");
        }
        if (allocate.getStatus() == AllocateStatus.CLOSE.getCode()) {
            return;
        }
        if (allocate.getStatus() != AllocateStatus.WAIT_EXAMINE.getCode() &&
                allocate.getStatus() != AllocateStatus.REJECT_EXAMINE.getCode() &&
                allocate.getStatus() != AllocateStatus.WAIT_OUT.getCode()) {
            throw new CustomException("调拨信息状态错误");
        }
        stockAllocateDao.updateStatus(merchantNo, id, AllocateStatus.CLOSE.getCode(), null);
    }

    @Transactional(rollbackFor = CustomException.class)
    public void outStock(long merchantNo, long storeNo, String accountId, String id, StockAllocateDto dto) {
        StockAllocateEntity allocate = stockAllocateDao.findById(merchantNo, id);
        if (allocate == null) {
            throw new CustomException("调拨信息异常");
        }
        if (allocate.getStatus() != AllocateStatus.WAIT_OUT.getCode()) {
            throw new CustomException("调出异常，当前状态不允许调出");
        }
        List<StockAllocateProductEntity> products = stockAllocateProductService.findAllByAllocateNo(allocate.getAllocateNo());
        if (products.isEmpty()) {
            throw new CustomException("调拨异常，调拨商品信息异常");
        }
        products.forEach(x -> {
            StockAllocateSkuDto productDto = dto.getSkus().stream().filter(y -> y.getSkuId().equals(x.getSkuId())).findFirst().orElse(null);
            if (productDto != null) {
                long outQuantity = StockUtil.parse(productDto.getOutQuantity());
                if (outQuantity > x.getAllocateQuantity()) {
                    throw new CustomException("商品调出数量错误，出库商品数量不能大于调拨商品数量");
                }
                x.setOutQuantity(outQuantity);
                stockAllocateProductService.outStock(x.getId(), outQuantity);
                stockSkuService.editSellStockExpend(merchantNo, storeNo, x.getSkuId(), productDto.getOutQuantity());
            }
        });
        //创建出入库记录入库，修改采购单状态
        StockRecordDto stockRecord = new StockRecordDto();
        stockRecord.setOperationTime(LocalDateTime.now());
        stockRecord.setType(StockRecordType.ALLOCATE_OUT.getCode());
        stockRecord.setIoType(StockIoType.OUT.getCode());
        List<StockRecordSkuDto> recordProducts = products.stream()
                .filter(x -> x.getOutQuantity() > 0)
                .map(x -> {
                    StockRecordSkuDto stockRecordProduct = new StockRecordSkuDto();
                    stockRecordProduct.setSpuId(x.getSpuId());
                    stockRecordProduct.setSkuId(x.getSkuId());
                    stockRecordProduct.setRecordQuantity(StockUtil.parseBigDecimal(x.getOutQuantity()));
                    return stockRecordProduct;
                }).collect(Collectors.toList());
        stockRecord.setProducts(recordProducts);
        stockRecordService.create(merchantNo, allocate.getOutStoreNo(), accountId, stockRecord);
        stockAllocateDao.updateStatus(merchantNo, allocate.getId(), AllocateStatus.WAIT_IN.getCode(), null);
    }

    public void inStock(long merchantNo, long storeNo, String accountId, String id, StockAllocateDto dto) {
        StockAllocateEntity allocate = stockAllocateDao.findById(merchantNo, id);
        if (allocate == null) {
            throw new CustomException("调拨信息异常");
        }
        if (allocate.getStatus() != AllocateStatus.WAIT_IN.getCode()) {
            throw new CustomException("调拨异常，当前状态不允许调调入");
        }
        List<StockAllocateProductEntity> products = stockAllocateProductService.findAllByAllocateNo(allocate.getAllocateNo());
        if (products.isEmpty()) {
            throw new CustomException("调拨异常，调拨商品信息异常");
        }
        products.forEach(x -> {
            StockAllocateSkuDto productDto = dto.getSkus().stream().filter(y -> y.getId().equals(x.getId())).findFirst().orElse(null);
            if (productDto != null) {
                long inQuantity = StockUtil.parse(productDto.getInQuantity());
                long outQuantity = StockUtil.parse(productDto.getOutQuantity());
                if (inQuantity > outQuantity) {
                    throw new CustomException("调入数量错误，不能大于调出数量");
                }
                x.setInQuantity(inQuantity);
                stockAllocateProductService.inStock(x.getId(), inQuantity);
                stockSkuService.editSellStockReplenish(merchantNo, storeNo, x.getSkuId(), productDto.getInQuantity());
            }
        });
        //创建出入库记录入库，修改采购单状态
        StockRecordDto stockRecord = new StockRecordDto();
        stockRecord.setOperationTime(LocalDateTime.now());
        stockRecord.setType(StockRecordType.ALLOCATE_IN.getCode());
        stockRecord.setIoType(StockIoType.IN.getCode());
        List<StockRecordSkuDto> recordProducts = products.stream()
                .filter(x -> x.getInQuantity() > 0)
                .map(x -> {
                    StockRecordSkuDto stockRecordProduct = new StockRecordSkuDto();
                    stockRecordProduct.setSpuId(x.getSpuId());
                    stockRecordProduct.setSkuId(x.getSkuId());
                    stockRecordProduct.setRecordQuantity(StockUtil.parseBigDecimal(x.getInQuantity()));
                    return stockRecordProduct;
                }).collect(Collectors.toList());
        stockRecord.setProducts(recordProducts);
        stockRecordService.create(merchantNo, allocate.getInStoreNo(), accountId, stockRecord);
        stockAllocateDao.updateStatus(merchantNo, allocate.getId(), AllocateStatus.FINISH.getCode(), null);
    }

    public StockAllocateVo info(long merchantNo, long storeNo, String accountId, String id) {
        StockAllocateEntity allocate = stockAllocateDao.findById(merchantNo, id);
        if (allocate == null) {
            return null;
        }
        List<Long> storeNos = new ArrayList<Long>() {{
            add(allocate.getStoreNo());
            add(allocate.getOutStoreNo());
            add(allocate.getInStoreNo());
        }}.stream().distinct().collect(Collectors.toList());
        List<MerchantStoreVo> stores = merchantStoreService.findAllByStoreNos(merchantNo, storeNos);
        List<String> accountIds = new ArrayList<String>() {{
            add(allocate.getOperationId());
        }};
        List<UserVo> accounts = accountService.findAllByIds(merchantNo, accountIds);
        StockAllocateVo vo = convertStockRecord(allocate, stores, accounts);
        convertProductStock(merchantNo, vo.getOutStoreNo(), vo.getInStoreNo(), vo.getSkus());
        return vo;
    }

    public PageVo<StockAllocateVo> page(long merchantNo, long storeNo, String accountId, StockAllocateQuery query) {
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(merchantNo, storeNo);
        if (storeVo == null) {
            return PageResult.of();
        }
        if (!storeVo.isMain() || !accountId.equals(storeVo.getAccountId()) || query.getStoreNo() == null) {
            query.setStoreNo(storeNo);
        }

        int total = stockAllocateDao.count(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<StockAllocateEntity> list = stockAllocateDao.page(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<Long> storeNos = list.stream().map(StockAllocateEntity::getStoreNo).collect(Collectors.toList());
        storeNos.addAll(list.stream().map(StockAllocateEntity::getInStoreNo).collect(Collectors.toList()));
        storeNos.addAll(list.stream().map(StockAllocateEntity::getOutStoreNo).collect(Collectors.toList()));
        storeNos = storeNos.stream().distinct().collect(Collectors.toList());
        List<MerchantStoreVo> stores = merchantStoreService.findAllByStoreNos(merchantNo, storeNos);
        List<String> accountIds = list.stream().map(StockAllocateEntity::getOperationId).distinct().collect(Collectors.toList());
        List<UserVo> accounts = accountService.findAllByIds(merchantNo, accountIds);
        List<StockAllocateVo> vos = list.stream().map(x -> convertStockRecord(x, stores, accounts)).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    private StockAllocateVo convertStockRecord(StockAllocateEntity entity, List<MerchantStoreVo> stores, List<UserVo> accounts) {
        StockAllocateVo vo = new StockAllocateVo();
        BeanUtils.copyProperties(entity, vo);
        stores.stream().filter(x -> x.getStoreNo() == entity.getStoreNo()).findFirst().ifPresent(store -> vo.setStoreName(store.getStoreName()));
        stores.stream().filter(x -> x.getStoreNo() == entity.getInStoreNo()).findFirst().ifPresent(store -> vo.setInStoreName(store.getStoreName()));
        stores.stream().filter(x -> x.getStoreNo() == entity.getOutStoreNo()).findFirst().ifPresent(store -> vo.setOutStoreName(store.getStoreName()));
        accounts.stream().filter(x -> x.getId().equals(entity.getOperationId())).findFirst().ifPresent(account -> vo.setOperationName(account.getName()));
        List<StockAllocateProductEntity> products = stockAllocateProductService.findAllByAllocateNo(vo.getAllocateNo());
        List<StockAllocateSkuVo> productVos = products.stream().map(x -> {
            StockAllocateSkuVo product = new StockAllocateSkuVo();
            BeanUtils.copyProperties(x, product);
            product.setAllocateQuantity(StockUtil.parseBigDecimal(x.getAllocateQuantity()));
            product.setOutQuantity(StockUtil.parseBigDecimal(x.getOutQuantity()));
            product.setInQuantity(StockUtil.parseBigDecimal(x.getInQuantity()));
            return product;
        }).collect(Collectors.toList());
        vo.setSkus(productVos);
        return vo;
    }

    private void convertProductStock(long merchantNo, long outStoreNo, long inStoreNo, List<StockAllocateSkuVo> products) {
        products.forEach(x -> {
            StockAllocateGateQuery query = new StockAllocateGateQuery();
            query.setOutStoreNo(outStoreNo);
            query.setInStoreNo(inStoreNo);
            List<StockSkuAllocateVo> skus = stockSkuService.findAllocateBySpuId(merchantNo, outStoreNo, x.getSpuId(), query);
            skus.stream().filter(y -> y.getSkuId().equals(x.getSkuId()))
                    .findFirst()
                    .ifPresent(y -> {
                        x.setOutSellStock(y.getOutSellStock());
                        x.setInSellStock(y.getInSellStock());
                    });
        });
    }
}
