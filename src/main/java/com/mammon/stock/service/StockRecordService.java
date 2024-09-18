package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.goods.domain.vo.SpuDetailVo;
import com.mammon.goods.service.SpuService;
import com.mammon.common.PageResult;
import com.mammon.leaf.enums.DocketType;
import com.mammon.exception.CustomException;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.clerk.service.AccountService;
import com.mammon.stock.dao.StockRecordDao;
import com.mammon.stock.domain.vo.StockChangeVo;
import com.mammon.stock.domain.vo.StockRecordReasonVo;
import com.mammon.stock.domain.vo.StockRecordSkuVo;
import com.mammon.stock.domain.vo.StockRecordVo;
import com.mammon.stock.domain.dto.StockRecordDto;
import com.mammon.stock.domain.enums.StockIoType;
import com.mammon.stock.domain.enums.StockRecordType;
import com.mammon.stock.domain.query.StockRecordQuery;
import com.mammon.stock.domain.entity.StockRecordEntity;
import com.mammon.stock.domain.entity.StockRecordSkuEntity;
import com.mammon.utils.StockUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockRecordService {

    @Resource
    private StockRecordSkuService stockRecordSkuService;

    @Resource
    private StockRecordDao stockRecordDao;

    @Resource
    private StockRecordReasonService stockRecordReasonService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private AccountService accountService;

    @Resource
    private SpuService spuService;

    @Resource
    private LeafCodeService leafCodeService;

    @Resource
    private StockSkuService stockSkuService;

    @Transactional(rollbackFor = CustomException.class)
    public void save(long merchantNo, long storeNo, String accountId, StockRecordDto dto) {
        dto.setType(StockRecordType.OTHER_IN.getCode());
        if (dto.getIoType() == StockIoType.OUT.getCode()) {
            dto.setType(StockRecordType.OTHER_OUT.getCode());
        }
        create(merchantNo, storeNo, accountId, dto);
        // 同步库存出入库
        if (dto.getIoType() == StockIoType.IN.getCode()) {
            dto.getProducts().forEach(x -> {
                stockSkuService.editSellStockReplenish(merchantNo, storeNo, x.getSkuId(), x.getRecordQuantity());
            });
        } else {
            dto.getProducts().forEach(x -> {
                StockChangeVo vo = stockSkuService.editSellStockExpend(merchantNo, storeNo, x.getSkuId(), x.getRecordQuantity());
                if (vo != null && vo.getCode() == 0) {
                    throw new CustomException(vo.getMessage());
                }
            });
        }
    }

    @Transactional(rollbackFor = CustomException.class)
    public void create(long merchantNo, long storeNo, String accountId, StockRecordDto dto) {
        String recordNo = leafCodeService.generateDocketNo(DocketType.STOCK_RECORD);
        StockRecordEntity entity = new StockRecordEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setRecordNo(recordNo);
        entity.setOperationTime(dto.getOperationTime());
        entity.setIoType(dto.getIoType());
        entity.setType(dto.getType());
        entity.setReasonId(dto.getReasonId());
        entity.setStatus(CommonStatus.ENABLED.getCode());
        entity.setOperationId(accountId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        stockRecordDao.save(entity);
        stockRecordSkuService.batchCreate(merchantNo, storeNo, accountId, recordNo, dto.getProducts());
    }

    public StockRecordVo info(long merchantNo, long storeNo, String accountId, String id) {
        StockRecordEntity entity = stockRecordDao.findByRecordNo(merchantNo, storeNo, accountId, id);
        if (entity == null) {
            return null;
        }
        List<Long> storeNos = Collections.singletonList(entity.getStoreNo());
        List<MerchantStoreVo> stores = merchantStoreService.findAllByStoreNos(merchantNo, storeNos);
        List<String> accountIds = Collections.singletonList(entity.getOperationId());
        List<UserVo> accounts = accountService.findAllByIds(merchantNo, accountIds);
        List<StockRecordReasonVo> reasons = stockRecordReasonService.findAll(merchantNo);
        return convertStockRecord(entity, stores, accounts, reasons);
    }

    public PageVo<StockRecordVo> page(long merchantNo, long storeNo, String accountId, StockRecordQuery query) {
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(merchantNo, storeNo);
        if (storeVo == null) {
            return PageResult.of();
        }
        if (!storeVo.isMain() || !accountId.equals(storeVo.getAccountId()) || query.getStoreNo() == null) {
            query.setStoreNo(storeNo);
        }

        int total = stockRecordDao.count(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<StockRecordEntity> list = stockRecordDao.page(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<Long> storeNos = list.stream().map(StockRecordEntity::getStoreNo).distinct().collect(Collectors.toList());
        List<MerchantStoreVo> stores = merchantStoreService.findAllByStoreNos(merchantNo, storeNos);
        List<String> accountIds = list.stream().map(StockRecordEntity::getOperationId).distinct().collect(Collectors.toList());
        List<UserVo> accounts = accountService.findAllByIds(merchantNo, accountIds);
        List<StockRecordReasonVo> reasons = stockRecordReasonService.findAll(merchantNo);
        List<StockRecordVo> vos = list.stream().map(x -> convertStockRecord(x, stores, accounts, reasons)).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    private StockRecordVo convertStockRecord(StockRecordEntity entity, List<MerchantStoreVo> stores,
                                             List<UserVo> accounts, List<StockRecordReasonVo> reasons) {
        StockRecordVo vo = new StockRecordVo();
        BeanUtils.copyProperties(entity, vo);
        stores.stream().filter(x -> x.getStoreNo() == entity.getStoreNo()).findFirst().ifPresent(store -> vo.setStoreName(store.getStoreName()));
        accounts.stream().filter(x -> x.getId().equals(entity.getOperationId())).findFirst().ifPresent(account -> vo.setOperationName(account.getName()));
        reasons.stream().filter(x -> StringUtils.isNotBlank(entity.getReasonId()) && x.getId().equals(entity.getReasonId()))
                .findFirst().ifPresent(reason -> vo.setReasonName(reason.getReasonName()));
        List<StockRecordSkuEntity> products = stockRecordSkuService.findAllByRecordNo(vo.getRecordNo());
        List<StockRecordSkuVo> productVos = products.stream().map(x -> {
            StockRecordSkuVo product = new StockRecordSkuVo();
            BeanUtils.copyProperties(x, product);
            product.setName(x.getSkuName());
            product.setRecordQuantity(StockUtil.parseBigDecimal(x.getRecordQuantity()));
            SpuDetailVo spuDetailVo = spuService.findDetailById(entity.getMerchantNo(), x.getSpuId());
            if (spuDetailVo != null) {
                product.setUnitId(spuDetailVo.getUnitId());
                product.setUnitName(spuDetailVo.getUnitName());
                if (!CollectionUtils.isEmpty(spuDetailVo.getSkus())) {
                    spuDetailVo.getSkus().stream().filter(y -> y.getId().equals(x.getSkuId()))
                            .findFirst()
                            .ifPresent(y -> product.setName(y.getSkuName()));
                }
            }
            return product;
        }).collect(Collectors.toList());
        vo.setProducts(productVos);
        return vo;
    }
}
