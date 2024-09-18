package com.mammon.stock.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.exception.CustomException;
import com.mammon.goods.domain.enums.SpuCountedType;
import com.mammon.leaf.enums.DocketType;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.service.AccountService;
import com.mammon.stock.dao.StockInventoryDao;
import com.mammon.stock.domain.dto.StockInventoryProductDto;
import com.mammon.stock.domain.entity.StockSpuEntity;
import com.mammon.stock.domain.vo.StockInventoryDetailVo;
import com.mammon.stock.domain.vo.StockInventoryPageVo;
import com.mammon.stock.domain.dto.StockInventoryCreateDto;
import com.mammon.stock.domain.dto.StockInventoryFinishDto;
import com.mammon.stock.domain.entity.StockInventoryEntity;
import com.mammon.stock.domain.enums.StockInventoryStatus;
import com.mammon.stock.domain.query.StockInventoryQuery;
import com.mammon.utils.JsonUtil;
import com.mammon.utils.StockUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 盘点
 *
 * @author dcl
 * @since 2024/4/1 16:26
 */
@Service
public class StockInventoryService {

    @Resource
    private StockInventoryDao stockInventoryDao;

    @Resource
    private StockInventoryProductService stockInventoryProductService;

    @Resource
    private LeafCodeService leafCodeService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private AccountService accountService;
    @Autowired
    private StockSpuService stockSpuService;

    public String create(long merchantNo, long storeNo, String accountId, StockInventoryCreateDto dto) {
        String inventoryNo = leafCodeService.generateDocketNo(DocketType.STOCK_INVENTORY);
        StockInventoryEntity entity = new StockInventoryEntity();

        entity.setId(Generate.generateUUID());
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setInventoryNo(inventoryNo);
        entity.setRange(dto.getRange());
        entity.setCategories(JsonUtil.toJSONString(dto.getCategories()));
        entity.setInventoryStartTime(LocalDateTime.now());
        entity.setOperationId(accountId);
        entity.setStatus(StockInventoryStatus.INVENTORYING.getCode());
        entity.setRemark(dto.getRemark());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        stockInventoryDao.save(entity);
        return entity.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void finish(long merchantNo, long storeNo, String id, StockInventoryFinishDto dto) {
        List<String> spuIds = dto.getProducts().stream().map(StockInventoryProductDto::getSpuId).distinct().collect(Collectors.toList());
        List<StockSpuEntity> spus = stockSpuService.findListBySpuIds(merchantNo, storeNo, spuIds);
        if (spus.stream().anyMatch(x -> x.getCountedType() == SpuCountedType.CAN.getCode())) {
            throw new CustomException("服务商品不能参与盘点");
        }

        stockInventoryProductService.batchCreate(id, dto.getProducts());

        long productNum = dto.getProducts().size();

        long errorProductNum = dto.getProducts().stream()
                .filter(x -> StockUtil.parse(x.getRealityStock()) - StockUtil.parse(x.getBeforeStock()) < 0)
                .count();

        StockInventoryEntity entity = new StockInventoryEntity();
        entity.setId(id);
        entity.setProductNum(productNum);
        entity.setErrorProductNum(errorProductNum);
        entity.setInventoryEndTime(LocalDateTime.now());
        entity.setStatus(StockInventoryStatus.INVENTORY_END.getCode());
        entity.setUpdateTime(LocalDateTime.now());
        stockInventoryDao.edit(entity);
    }

    public void cancel(String id) {
        stockInventoryDao.editStatus(id, StockInventoryStatus.INVENTORY_CANCEL.getCode());
    }

    public StockInventoryDetailVo findById(String id) {
        StockInventoryEntity entity = stockInventoryDao.findById(id);
        if (entity == null) {
            return null;
        }
        return convertDetail(entity);
    }

    public PageVo<StockInventoryPageVo> page(long merchantNo, long storeNo, String accountId, StockInventoryQuery query) {
        MerchantStoreVo storeVo = merchantStoreService.findByStoreNo(merchantNo, storeNo);
        if (storeVo == null) {
            return PageResult.of();
        }
        if (!storeVo.isMain() || !accountId.equals(storeVo.getAccountId()) || query.getStoreNo() == null) {
            query.setStoreNo(storeNo);
        }

        int total = stockInventoryDao.countPage(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<StockInventoryEntity> list = stockInventoryDao.findPage(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<StockInventoryPageVo> pageVos = list.stream().map(x -> {
            StockInventoryPageVo vo = new StockInventoryPageVo();
            BeanUtils.copyProperties(x, vo);
            MerchantStoreVo store = merchantStoreService.findByStoreNo(vo.getMerchantNo(), vo.getStoreNo());
            if (store != null) {
                vo.setStoreName(store.getStoreName());
            }
            AccountEntity account = accountService.findById(x.getOperationId());
            if (account != null) {
                vo.setOperationName(account.getName());
            }
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, pageVos);
    }

    private StockInventoryDetailVo convertDetail(StockInventoryEntity entity) {
        StockInventoryDetailVo vo = new StockInventoryDetailVo();
        BeanUtils.copyProperties(entity, vo);
        vo.setCategories(JsonUtil.toList(entity.getCategories(), String.class));
        AccountEntity account = accountService.findById(vo.getOperationId());
        MerchantStoreVo store = merchantStoreService.findByStoreNo(vo.getMerchantNo(), vo.getStoreNo());

        if (store != null) {
            vo.setStoreName(store.getStoreName());
        }
        if (account != null) {
            vo.setOperationName(account.getName());
        }
        vo.setProducts(stockInventoryProductService.findAllByInventoryId(vo.getId()));
        return vo;
    }
}
