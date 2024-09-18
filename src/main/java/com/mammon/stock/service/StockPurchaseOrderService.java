package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.exception.CustomException;
import com.mammon.goods.domain.enums.SpuCountedType;
import com.mammon.goods.domain.enums.UnitType;
import com.mammon.leaf.enums.DocketType;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.clerk.service.AccountService;
import com.mammon.stock.dao.StockPurchaseOrderDao;
import com.mammon.stock.domain.entity.StockSpuEntity;
import com.mammon.stock.domain.vo.*;
import com.mammon.stock.domain.dto.*;
import com.mammon.stock.domain.entity.StockPurchaseOrderEntity;
import com.mammon.stock.domain.entity.StockPurchaseOrderSkuEntity;
import com.mammon.stock.domain.entity.StockSettingEntity;
import com.mammon.stock.domain.enums.PurchaseOrderStatus;
import com.mammon.stock.domain.enums.StockIoType;
import com.mammon.stock.domain.enums.StockRecordType;
import com.mammon.stock.domain.query.StockPurchaseOrderQuery;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.StockUtil;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockPurchaseOrderService {

    @Resource
    private StockPurchaseOrderDao stockPurchaseOrderDao;

    @Resource
    private StockPurchaseOrderSkuService stockPurchaseOrderSkuService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private AccountService accountService;

    @Resource
    private StockRecordService stockRecordService;

    @Resource
    private StockSkuService stockSkuService;

    @Resource
    private StockSettingService stockSettingService;

    @Resource
    private LeafCodeService leafCodeService;
    @Autowired
    private StockSpuService stockSpuService;

    @Transactional(rollbackFor = CustomException.class)
    public void create(long merchantNo, long storeNo, String accountId, StockPurchaseOrderDto dto) {
        String purchaseNo = leafCodeService.generateDocketNo(DocketType.PURCHASE_ORDER);
        if (StringUtils.isBlank(purchaseNo)) {
            throw new CustomException("创建异常:采购单号异常");
        }
        List<String> spuIds = dto.getSkus().stream().map(StockPurchaseOrderSkuDto::getSpuId).distinct().collect(Collectors.toList());
        List<StockSpuEntity> spus = stockSpuService.findListBySpuIds(merchantNo, storeNo, spuIds);
        if (spus.stream().anyMatch(x -> x.getCountedType() == SpuCountedType.CAN.getCode())) {
            throw new CustomException("服务商品不能参与采购操作");
        }

        int status = PurchaseOrderStatus.WAIT_WARE_HOUSE.getCode();
        StockSettingEntity setting = stockSettingService.findByMerchantNo(merchantNo);
        if (setting != null && setting.getPurchaseOrderExamine() == CommonStatus.ENABLED.getCode()) {
            status = PurchaseOrderStatus.WAIT_EXAMINE.getCode();
        }
        StockPurchaseOrderEntity entity = new StockPurchaseOrderEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setPurchaseNo(purchaseNo);
        entity.setPurchaseStoreNo(dto.getStoreNo());
        entity.setDeliveryTime(dto.getDeliveryTime());
        entity.setStatus(status);
        entity.setOperationId(accountId);
        entity.setRemark(dto.getRemark());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        stockPurchaseOrderDao.save(entity);
        stockPurchaseOrderSkuService.batchSave(entity.getId(), dto.getSkus());
    }

    @Transactional(rollbackFor = CustomException.class)
    public void edit(long merchantNo, long storeNo, String accountId, String id, StockPurchaseOrderDto dto) {
        StockPurchaseOrderEntity purchaseOrder = stockPurchaseOrderDao.findById(merchantNo, id);
        if (purchaseOrder == null) {
            throw new CustomException("采购订单错误");
        }
        if (purchaseOrder.getStatus() != PurchaseOrderStatus.REJECT_EXAMINE.getCode() &&
                purchaseOrder.getStatus() != PurchaseOrderStatus.WAIT_EXAMINE.getCode()) {
            throw new CustomException("当前采购单状态不允许修改");
        }
        stockPurchaseOrderDao.update(merchantNo, id, dto.getStoreNo(), dto.getDeliveryTime(), dto.getRemark());
        stockPurchaseOrderSkuService.deleteAllByPurchaseId(id);
        stockPurchaseOrderSkuService.batchSave(id, dto.getSkus());
        if (purchaseOrder.getStatus() == PurchaseOrderStatus.REJECT_EXAMINE.getCode()) {
            int status = PurchaseOrderStatus.WAIT_WARE_HOUSE.getCode();
            StockSettingEntity setting = stockSettingService.findByMerchantNo(merchantNo);
            if (setting != null && setting.getPurchaseOrderExamine() == CommonStatus.ENABLED.getCode()) {
                status = PurchaseOrderStatus.WAIT_EXAMINE.getCode();
            }
            stockPurchaseOrderDao.updateOrderStatus(merchantNo, id, status, "");
        }
    }

    public void examine(long merchantNo, String id, StockPurchaseOrderExamineDto dto) {
        if (dto.getStatus() != PurchaseOrderStatus.WAIT_WARE_HOUSE.getCode() &&
                dto.getStatus() != PurchaseOrderStatus.REJECT_EXAMINE.getCode()) {
            throw new CustomException("审核状态错误");
        }
        stockPurchaseOrderDao.updateOrderStatus(merchantNo, id, dto.getStatus(), dto.getErrDesc());
    }

    public void close(long merchantNo, String id) {
        StockPurchaseOrderEntity entity = findById(merchantNo, id);
        if (entity == null || entity.getStatus() == PurchaseOrderStatus.CLOSE.getCode()) {
            return;
        }
        if (entity.getStatus() == PurchaseOrderStatus.SOME_WARE_HOUSE.getCode() ||
                entity.getStatus() == PurchaseOrderStatus.WARE_HOUSED.getCode()) {
            throw new CustomException("采购单已有入库信息，不能关闭");
        }
        stockPurchaseOrderDao.updateOrderStatus(merchantNo, id, PurchaseOrderStatus.CLOSE.getCode(), "");
    }

    /**
     * 采购入库操作
     *
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public void replenish(long merchantNo, long storeNo, String accountId, String id,
                          List<StockPurchaseReplenishDto> dtos) {
        StockPurchaseOrderVo info = info(merchantNo, id);
        if (info == null) {
            throw new CustomException("采购单信息不存在");
        }
        replenishRecord(dtos, info);
        List<StockStoreReplenishDto> replenishDtos = info.getSkus().stream().map(x -> {
            // 待入库数量=采购数量-已入库数量-已退货数量
            long waitReplenish = StockUtil.parse(x.getPurchaseQuantity()) - StockUtil.parse(x.getReplenishQuantity()) - StockUtil.parse(x.getRefundQuantity());
            StockPurchaseReplenishDto replenishDto = dtos.stream()
                    .filter(y -> y.getSkuId().equals(x.getSkuId()))
                    .findFirst().orElse(null);
            if (replenishDto != null) {
                if (StockUtil.parse(replenishDto.getReplenishQuantity()) > waitReplenish) {
                    throw new CustomException("入库数量不能大于采购数量");
                }
                StockStoreReplenishDto stockStoreReplenishDto = new StockStoreReplenishDto();
                stockStoreReplenishDto.setId(x.getId());
                stockStoreReplenishDto.setSkuId(x.getSkuId());
                stockStoreReplenishDto.setSellStock(replenishDto.getReplenishQuantity());
                return stockStoreReplenishDto;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(replenishDtos)) {
            stockPurchaseOrderSkuService.batchReplenish(id, replenishDtos);
            stockSkuService.batchSellStockReplenish(merchantNo, info.getPurchaseStoreNo(), replenishDtos);
            purchaseReplenishStatus(merchantNo, id);
        }
    }

    private void purchaseReplenishStatus(long merchantNo, String id) {
        StockPurchaseOrderVo info = info(merchantNo, id);
        if (info == null) {
            return;
        }
        boolean bool = true;
        for (StockPurchaseOrderSkuVo sku : info.getSkus()) {
            BigDecimal total = sku.getReplenishQuantity().add(sku.getRefundQuantity());
            if (sku.getPurchaseQuantity().compareTo(total) != 0) {
                bool = false;
                break;
            }
        }
        int status = PurchaseOrderStatus.WARE_HOUSED.getCode();
        if (!bool) {
            status = PurchaseOrderStatus.SOME_WARE_HOUSE.getCode();
        }
        stockPurchaseOrderDao.updateOrderStatus(merchantNo, id, status, null);
    }

    public void replenishRecord(List<StockPurchaseReplenishDto> dtos, StockPurchaseOrderVo info) {
        StockRecordDto stockRecord = new StockRecordDto();
        stockRecord.setOperationTime(LocalDateTime.now());
        stockRecord.setType(StockRecordType.PURCHASE_ORDER.getCode());
        stockRecord.setIoType(StockIoType.IN.getCode());
        List<StockRecordSkuDto> products = info.getSkus().stream().map(x -> {
            StockPurchaseReplenishDto replenishDto = dtos.stream().filter(y -> y.getSkuId().equals(x.getSkuId()))
                    .findFirst().orElse(null);
            if (replenishDto == null) {
                return null;
            }
            StockRecordSkuDto stockRecordProduct = new StockRecordSkuDto();
            stockRecordProduct.setSpuId(x.getSpuId());
            stockRecordProduct.setSkuId(x.getSkuId());
            stockRecordProduct.setSkuName(x.getSkuName());
            stockRecordProduct.setRecordQuantity(replenishDto.getReplenishQuantity());
            stockRecordProduct.setRecordAmount(x.getPurchaseAmount());
            return stockRecordProduct;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        stockRecord.setProducts(products);
        stockRecordService.create(info.getMerchantNo(), info.getPurchaseStoreNo(), info.getOperationId(), stockRecord);
    }

    @Transactional(rollbackFor = CustomException.class)
    public void updateOrderRefund(long merchantNo, String id, int refundMark) {
        stockPurchaseOrderDao.updateOrderRefund(merchantNo, id, refundMark);
    }

    public StockPurchaseOrderEntity findById(long merchantNo, String id) {
        return stockPurchaseOrderDao.findById(merchantNo, id);
    }

    public StockPurchaseOrderVo info(long merchantNo, String id) {
        StockPurchaseOrderEntity entity = findById(merchantNo, id);
        if (entity == null) {
            return null;
        }
        List<Long> storeNos = new ArrayList<Long>() {{
            add(entity.getStoreNo());
            add(entity.getPurchaseStoreNo());
        }}.stream().distinct().collect(Collectors.toList());
        List<MerchantStoreVo> stores = merchantStoreService.findAllByStoreNos(merchantNo, storeNos);
        List<String> accountIds = new ArrayList<String>() {{
            add(entity.getOperationId());
        }};
        List<UserVo> accounts = accountService.findAllByIds(merchantNo, accountIds);
        return convertPurchase(entity, stores, accounts);
    }

    public List<StockPurchaseOrderVo> list(long merchantNo, long storeNo, String accountId) {
        List<StockPurchaseOrderEntity> list = stockPurchaseOrderDao.list(merchantNo, storeNo, accountId);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return convertPurchaseList(merchantNo, list);
    }

    public PageVo<StockPurchaseOrderVo> page(long merchantNo, long storeNo, String accountId, StockPurchaseOrderQuery query) {
        // 判断storeNo是不是主店，同时判断是不是主店管理
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(merchantNo, storeNo);
        if (storeVo == null) {
            return PageResult.of();
        }
        if (!storeVo.isMain() || !accountId.equals(storeVo.getAccountId()) || query.getStoreNo() == null) {
            query.setStoreNo(storeNo);
        }
        int total = stockPurchaseOrderDao.count(merchantNo, storeNo, accountId, query);
        if (total <= 0) {
            return null;
        }
        List<StockPurchaseOrderEntity> list = stockPurchaseOrderDao.page(merchantNo, storeNo, accountId, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, convertPurchaseList(merchantNo, list));
    }

    public List<StockPurchaseOrderVo> convertPurchaseList(long merchantNo, List<StockPurchaseOrderEntity> list) {
        List<Long> purchaseStoreNos = list.stream().map(StockPurchaseOrderEntity::getPurchaseStoreNo).distinct().collect(Collectors.toList());
        List<Long> operationStoreNos = list.stream().map(StockPurchaseOrderEntity::getStoreNo).distinct().collect(Collectors.toList());
        List<Long> storeNos = new ArrayList<>();
        storeNos.addAll(purchaseStoreNos);
        storeNos.addAll(operationStoreNos);
        storeNos = storeNos.stream().distinct().collect(Collectors.toList());
        List<MerchantStoreVo> stores = merchantStoreService.findAllByStoreNos(merchantNo, storeNos);
        List<String> accountIds = list.stream().map(StockPurchaseOrderEntity::getOperationId).distinct().collect(Collectors.toList());
        List<UserVo> accounts = accountService.findAllByIds(merchantNo, accountIds);
        return list.stream().map(x -> convertPurchase(x, stores, accounts))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    public StockPurchaseOrderVo convertPurchase(StockPurchaseOrderEntity entity, List<MerchantStoreVo> stores, List<UserVo> accounts) {
        StockPurchaseOrderVo vo = new StockPurchaseOrderVo();
        List<StockPurchaseOrderSkuEntity> products = stockPurchaseOrderSkuService.findAllByPurchaseId(entity.getId());
        if (products.isEmpty()) {
            return null;
        }
        BeanUtils.copyProperties(entity, vo);
        vo.setPurchaseTotal(products.size());
        vo.setPurchaseSkuTotal(getSkuTotal(vo.getMerchantNo(), vo.getStoreNo(), products));

        stores.stream().filter(x -> x.getStoreNo() == vo.getStoreNo()).findFirst()
                .ifPresent(merchantStoreVo -> vo.setStoreName(merchantStoreVo.getStoreName()));
        stores.stream()
                .filter(x -> x.getStoreNo() == vo.getPurchaseStoreNo()).findFirst()
                .ifPresent(merchantStoreVo -> vo.setPurchaseStoreName(merchantStoreVo.getStoreName()));
        accounts.stream()
                .filter(x -> x.getId().equals(vo.getOperationId())).findFirst()
                .ifPresent(userVo -> vo.setOperationName(userVo.getName()));

        List<String> spuIds = products.stream()
                .map(StockPurchaseOrderSkuEntity::getSpuId).distinct()
                .collect(Collectors.toList());
        List<StockSkuVo> skus = stockSkuService.findListBySpuIds(entity.getMerchantNo(), entity.getPurchaseStoreNo(), spuIds);

        List<StockPurchaseOrderSkuVo> productVos = products.stream().map(x -> {
            StockPurchaseOrderSkuVo product = new StockPurchaseOrderSkuVo();
            BeanUtils.copyProperties(x, product);
            product.setPurchaseAmount(AmountUtil.parseBigDecimal(x.getPurchaseAmount()));
            product.setPurchaseQuantity(StockUtil.parseBigDecimal(x.getPurchaseQuantity()));
            product.setReplenishQuantity(StockUtil.parseBigDecimal(x.getReplenishQuantity()));
            product.setRefundQuantity(StockUtil.parseBigDecimal(x.getRefundQuantity()));
            skus.stream()
                    .filter(y -> StringUtils.isNotBlank(y.getSkuId()) && y.getSkuId().equals(x.getSkuId()))
                    .findFirst().ifPresent(sku -> {
                        product.setSkuName(sku.getSkuName());
                        product.setSellStock(sku.getSellStock());
                        product.setUnitId(sku.getUnitId());
                        product.setUnitName(sku.getUnitName());
                        product.setReferenceAmount(sku.getReferenceAmount());
                    });
            return product;
        }).collect(Collectors.toList());
        vo.setSkus(productVos);
        return vo;
    }

    private long getSkuTotal(long merchantNo, long storeNo, List<StockPurchaseOrderSkuEntity> products) {
        List<String> skuIds = products.stream().map(StockPurchaseOrderSkuEntity::getSkuId).collect(Collectors.toList());
        List<StockSkuVo> stockSkus = stockSkuService.findListBySkuIds(merchantNo, storeNo, skuIds);
        long weightCount = stockSkus.stream()
                .filter(x -> x.getUnitType() == UnitType.WEIGHT.getCode())
                .count();

        List<String> numberSkuIds = stockSkus.stream()
                .filter(x -> x.getUnitType() == UnitType.NUMBER.getCode())
                .map(StockSkuVo::getSkuId).collect(Collectors.toList());
        long numberCount = products.stream()
                .filter(x -> numberSkuIds.contains(x.getSkuId()))
                .mapToLong(StockPurchaseOrderSkuEntity::getPurchaseQuantity).sum();
        BigDecimal amount = StockUtil.parseBigDecimal(numberCount);
        return weightCount + amount.longValue();
    }
}
