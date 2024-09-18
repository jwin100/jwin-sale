package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.common.PageVo;
import com.mammon.enums.CommonStatus;
import com.mammon.common.PageResult;
import com.mammon.leaf.enums.DocketType;
import com.mammon.exception.CustomException;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.clerk.domain.vo.UserVo;
import com.mammon.clerk.service.AccountService;
import com.mammon.stock.dao.StockPurchaseRefundDao;
import com.mammon.stock.domain.dto.*;
import com.mammon.stock.domain.entity.StockPurchaseRefundEntity;
import com.mammon.stock.domain.entity.StockPurchaseRefundSkuEntity;
import com.mammon.stock.domain.entity.StockSettingEntity;
import com.mammon.stock.domain.enums.*;
import com.mammon.stock.domain.query.StockPurchaseRefundQuery;
import com.mammon.stock.domain.vo.*;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.StockUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StockPurchaseRefundService {

    @Resource
    private StockPurchaseRefundDao stockPurchaseRefundDao;

    @Resource
    private StockRecordService stockRecordService;

    @Resource
    private StockPurchaseOrderService stockPurchaseOrderService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private AccountService accountService;

    @Resource
    private StockPurchaseOrderSkuService stockPurchaseOrderSkuService;

    @Resource
    private StockPurchaseRefundSkuService stockPurchaseRefundSkuService;

    @Resource
    private StockSettingService stockSettingService;

    @Resource
    private StockRecordReasonService stockRecordReasonService;

    @Resource
    private LeafCodeService leafCodeService;

    @Resource
    private StockSkuService stockSkuService;

    /**
     * 创建采购退货单
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     */
    @Transactional(rollbackFor = CustomException.class)
    public void create(long merchantNo, long storeNo, String accountId, StockPurchaseRefundDto dto) {
        String refundNo = leafCodeService.generateDocketNo(DocketType.PURCHASE_REFUND);
        if (StringUtils.isBlank(refundNo)) {
            throw new CustomException("创建异常:采购退货单号异常");
        }
        StockPurchaseOrderVo purchaseOrder = stockPurchaseOrderService.info(merchantNo, dto.getPurchaseId());
        if (purchaseOrder == null) {
            throw new CustomException("创建异常:采购单错误");
        }
        if (purchaseOrder.getStatus() != PurchaseOrderStatus.WARE_HOUSED.getCode()) {
            throw new CustomException("采购单状态错误无法退货");
        }
        int status = PurchaseRefundStatus.WAIT_EX_WARE_HOUSE.getCode();
        StockSettingEntity setting = stockSettingService.findByMerchantNo(merchantNo);
        if (setting != null && setting.getPurchaseRefundExamine() == CommonStatus.ENABLED.getCode()) {
            status = PurchaseRefundStatus.WAIT_EXAMINE.getCode();
        }

        StockPurchaseRefundEntity entity = new StockPurchaseRefundEntity();
        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setRefundNo(refundNo);
        entity.setPurchaseId(dto.getPurchaseId());
        entity.setStatus(status);
        entity.setOperationId(accountId);
        entity.setReasonId(dto.getReasonId());
        entity.setRemark(dto.getRemark());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        stockPurchaseRefundDao.save(entity);
        stockPurchaseRefundSkuService.batchSave(entity.getId(), dto.getSkus());
    }

    /**
     * 修改采购退货单
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @param dto
     */
    @Transactional(rollbackFor = CustomException.class)
    public void edit(long merchantNo, long storeNo, String accountId, String id, StockPurchaseRefundDto dto) {
        StockPurchaseRefundEntity purchaseRefund = stockPurchaseRefundDao.findById(merchantNo, id);
        if (purchaseRefund == null) {
            throw new CustomException("采购退货单信息错误");
        }
        if (purchaseRefund.getStatus() != PurchaseRefundStatus.REJECT_EXAMINE.getCode() &&
                purchaseRefund.getStatus() != PurchaseRefundStatus.WAIT_EXAMINE.getCode()) {
            throw new CustomException("当前采购退货单状态不允许修改");
        }

        StockPurchaseRefundEntity entity = new StockPurchaseRefundEntity();
        //可修改状态,确认后进入待入库状态(往后则不可修改采购单
        entity.setId(id);
        entity.setMerchantNo(merchantNo);
        entity.setReasonId(dto.getReasonId());
        entity.setRemark(dto.getRemark());
        entity.setUpdateTime(LocalDateTime.now());
        stockPurchaseRefundDao.update(entity);
        stockPurchaseRefundSkuService.deleteAllByRefundId(id);
        stockPurchaseRefundSkuService.batchSave(entity.getId(), dto.getSkus());
        if (purchaseRefund.getStatus() == PurchaseRefundStatus.REJECT_EXAMINE.getCode()) {
            int status = PurchaseRefundStatus.WAIT_EX_WARE_HOUSE.getCode();
            StockSettingEntity setting = stockSettingService.findByMerchantNo(merchantNo);
            if (setting != null && setting.getPurchaseRefundExamine() == CommonStatus.ENABLED.getCode()) {
                status = PurchaseRefundStatus.WAIT_EXAMINE.getCode();
            }
            stockPurchaseRefundDao.updateOrderStatus(merchantNo, id, status, "");
        }
    }

    /**
     * 退货单审核
     *
     * @param merchantNo
     * @param id
     * @param dto
     */
    @Transactional(rollbackFor = CustomException.class)
    public void examine(long merchantNo, String id, StockPurchaseRefundExamineDto dto) {
        if (dto.getStatus() != PurchaseRefundStatus.WAIT_EX_WARE_HOUSE.getCode() &&
                dto.getStatus() != PurchaseRefundStatus.REJECT_EXAMINE.getCode()) {
            throw new CustomException("审核状态错误");
        }
        stockPurchaseRefundDao.updateOrderStatus(merchantNo, id, dto.getStatus(), dto.getErrDesc());
    }

    /**
     * 退货单关闭
     *
     * @param merchantNo
     * @param id
     */
    public void close(long merchantNo, String id) {
        StockPurchaseRefundEntity entity = stockPurchaseRefundDao.findById(merchantNo, id);
        if (entity == null || entity.getStatus() == PurchaseRefundStatus.CLOSE.getCode()) {
            return;
        }
        if (entity.getStatus() == PurchaseRefundStatus.EX_WARE_HOUSED.getCode()) {
            throw new CustomException("采购退货单单已有出库信息，不能关闭");
        }
        stockPurchaseRefundDao.updateOrderStatus(merchantNo, id, PurchaseRefundStatus.CLOSE.getCode(), "");
    }

    /**
     * 采购退货出库操作
     *
     * @return
     */
    @Transactional(rollbackFor = CustomException.class)
    public void expand(long merchantNo, long storeNo, String accountId, String id) {
        //创建出入库记录入库，修改采购单状态
        StockPurchaseRefundVo purchaseRefund = info(merchantNo, id);
        if (purchaseRefund == null) {
            throw new CustomException("退货单信息错误");
        }
        StockPurchaseOrderVo purchaseOrder = stockPurchaseOrderService.info(merchantNo, purchaseRefund.getPurchaseId());
        if (purchaseOrder == null) {
            throw new CustomException("退货单信息错误");
        }
        expandRecord(purchaseRefund);
        List<StockPurchaseRefundExpendDto> purchaseRefundExpend = purchaseRefund.getSkus().stream().map(x -> {
            StockPurchaseOrderSkuVo orderSku = purchaseOrder.getSkus().stream().filter(sku -> sku.getSkuId().equals(x.getSkuId()))
                    .findFirst().orElse(null);
            if (orderSku != null) {
                long waitRefundQuantity = StockUtil.parse(orderSku.getReplenishQuantity()) - StockUtil.parse(orderSku.getRefundQuantity());
                if (StockUtil.parse(x.getRefundQuantity()) > waitRefundQuantity) {
                    throw new CustomException(String.format("%s，退货数量不能大于可退数量", x.getSkuName()));
                }
                StockPurchaseRefundExpendDto stock = new StockPurchaseRefundExpendDto();
                stock.setPurchaseOrderSkuId(orderSku.getId());
                stock.setSkuId(x.getSkuId());
                stock.setSellStock(x.getRefundQuantity());
                return stock;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        List<StockStoreExpendDto> expendDtos = purchaseRefundExpend.stream().map(x -> {
            StockStoreExpendDto dto = new StockStoreExpendDto();
            dto.setSkuId(x.getSkuId());
            dto.setSellStock(x.getSellStock());
            return dto;
        }).collect(Collectors.toList());
        // 同步库存出库
        stockSkuService.batchSellStockExpend(merchantNo, storeNo, expendDtos);
        // 修改采购单退货状态和退货数量
        stockPurchaseOrderService.updateOrderRefund(merchantNo, purchaseRefund.getPurchaseId(), StockPurchaseRefundMark.REFUNDED.getCode());
        stockPurchaseOrderSkuService.batchRefund(purchaseOrder.getId(), purchaseRefundExpend);
        // 修改退货单状态为已出库
        stockPurchaseRefundDao.updateOrderStatus(merchantNo, id, PurchaseRefundStatus.EX_WARE_HOUSED.getCode(), null);
    }

    public void expandRecord(StockPurchaseRefundVo info) {
        StockRecordDto stockRecord = new StockRecordDto();
        stockRecord.setOperationTime(LocalDateTime.now());
        stockRecord.setType(StockRecordType.PURCHASE_REFUND.getCode());
        stockRecord.setIoType(StockIoType.OUT.getCode());
        stockRecord.setReasonId(info.getReasonId());
        List<StockRecordSkuDto> products = info.getSkus().stream().map(x -> {
            StockRecordSkuDto skuDto = new StockRecordSkuDto();
            skuDto.setSpuId(x.getSpuId());
            skuDto.setSkuId(x.getSkuId());
            skuDto.setRecordQuantity(x.getRefundQuantity());
            skuDto.setRecordAmount(x.getPurchaseAmount());
            return skuDto;
        }).collect(Collectors.toList());
        stockRecord.setProducts(products);
        stockRecordService.create(info.getMerchantNo(), info.getStoreNo(), info.getOperationId(), stockRecord);
    }

    public StockPurchaseRefundVo info(long merchantNo, String id) {
        StockPurchaseRefundEntity entity = stockPurchaseRefundDao.findById(merchantNo, id);
        if (entity == null) {
            return null;
        }
        List<Long> storeNos = new ArrayList<Long>() {{
            add(entity.getStoreNo());
        }};
        List<MerchantStoreVo> stores = merchantStoreService.findAllByStoreNos(merchantNo, storeNos);
        List<String> accountIds = new ArrayList<String>() {{
            add(entity.getOperationId());
        }};
        List<UserVo> accounts = accountService.findAllByIds(merchantNo, accountIds);
        return convertPurchase(entity, stores, accounts);
    }

    public PageVo<StockPurchaseRefundVo> page(long merchantNo, long storeNo, String accountId, StockPurchaseRefundQuery query) {
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(merchantNo, storeNo);
        if (storeVo == null) {
            return PageResult.of();
        }
        if (!storeVo.isMain() || !accountId.equals(storeVo.getAccountId()) || query.getStoreNo() == null) {
            query.setStoreNo(storeNo);
        }
        int total = stockPurchaseRefundDao.count(merchantNo, storeNo, accountId, query);
        if (total <= 0) {
            return null;
        }
        List<StockPurchaseRefundEntity> list = stockPurchaseRefundDao.page(merchantNo, storeNo, accountId, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<Long> storeNos = list.stream().map(StockPurchaseRefundEntity::getStoreNo).distinct().collect(Collectors.toList());
        List<MerchantStoreVo> stores = merchantStoreService.findAllByStoreNos(merchantNo, storeNos);
        List<String> accountIds = list.stream().map(StockPurchaseRefundEntity::getOperationId).distinct().collect(Collectors.toList());
        List<UserVo> accounts = accountService.findAllByIds(merchantNo, accountIds);
        List<StockPurchaseRefundVo> vos = list.stream().map(x -> convertPurchase(x, stores, accounts))
                .filter(Objects::nonNull).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public StockPurchaseRefundVo convertPurchase(StockPurchaseRefundEntity entity,
                                                 List<MerchantStoreVo> stores, List<UserVo> accounts) {
        StockPurchaseRefundVo vo = new StockPurchaseRefundVo();
        BeanUtils.copyProperties(entity, vo);
        StockPurchaseOrderVo orderVo = stockPurchaseOrderService.info(entity.getMerchantNo(), entity.getPurchaseId());
        if (orderVo == null) {
            throw new CustomException("订单信息错误");
        }
        List<StockPurchaseRefundSkuEntity> products = stockPurchaseRefundSkuService.findAllByRefundId(entity.getId());
        vo.setPurchaseNo(orderVo.getPurchaseNo());
        vo.setRefundTotal(products.size());

        if (StringUtils.isNotBlank(entity.getReasonId())) {
            StockRecordReasonVo reasonVo = stockRecordReasonService.findById(entity.getMerchantNo(), entity.getReasonId());
            if (reasonVo != null) {
                vo.setReasonName(reasonVo.getReasonName());
            }
        }
        stores.stream()
                .filter(x -> x.getStoreNo() == vo.getStoreNo())
                .findFirst()
                .ifPresent(merchantStoreVo -> vo.setStoreName(merchantStoreVo.getStoreName()));
        accounts.stream()
                .filter(x -> x.getId().equals(vo.getOperationId()))
                .findFirst()
                .ifPresent(userVo -> vo.setOperationName(userVo.getName()));

        List<StockPurchaseRefundSkuVo> productVos = products.stream().map(sku -> {
            StockPurchaseRefundSkuVo product = new StockPurchaseRefundSkuVo();
            BeanUtils.copyProperties(sku, product);
            product.setRefundQuantity(StockUtil.parseBigDecimal(sku.getRefundQuantity()));
            product.setRefundAmount(AmountUtil.parseBigDecimal(sku.getRefundAmount()));
            orderVo.getSkus().stream()
                    .filter(x -> x.getSkuId().equals(sku.getSkuId()))
                    .findFirst()
                    .ifPresent(skuVo -> {
                        product.setSkuName(skuVo.getSkuName());
                        product.setUnitId(skuVo.getUnitId());
                        product.setUnitName(skuVo.getUnitName());
                        product.setPurchaseAmount(skuVo.getPurchaseAmount());
                        product.setPurchaseQuantity(skuVo.getPurchaseQuantity());
                    });
            return product;
        }).collect(Collectors.toList());
        vo.setSkus(productVos);
        return vo;
    }
}
