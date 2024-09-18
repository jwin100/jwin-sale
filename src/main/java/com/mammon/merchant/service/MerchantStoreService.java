package com.mammon.merchant.service;

import com.mammon.common.*;
import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import com.mammon.exception.CustomException;
import com.mammon.goods.domain.query.SpuQuery;
import com.mammon.goods.domain.vo.SkuVo;
import com.mammon.goods.domain.vo.SpuListVo;
import com.mammon.goods.service.SkuService;
import com.mammon.goods.service.SpuService;
import com.mammon.merchant.dao.MerchantStoreBindQuotaLogDao;
import com.mammon.merchant.dao.MerchantStoreDao;
import com.mammon.merchant.domain.dto.MerchantStoreDto;
import com.mammon.merchant.domain.query.MerchantStoreQuery;
import com.mammon.merchant.domain.entity.MerchantStoreBindQuotaLogEntity;
import com.mammon.merchant.domain.entity.MerchantStoreEntity;
import com.mammon.merchant.domain.enums.BasicConfigConst;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.domain.vo.RegionPathVo;
import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.domain.entity.AccountRoleMapEntity;
import com.mammon.clerk.domain.entity.RoleEntity;
import com.mammon.clerk.service.AccountRoleMapService;
import com.mammon.clerk.service.AccountService;
import com.mammon.clerk.service.RoleService;
import com.mammon.stock.domain.dto.StockSkuDto;
import com.mammon.stock.domain.dto.StockSpuDto;
import com.mammon.stock.service.StockSpuService;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import com.mammon.utils.StockUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MerchantStoreService {

    @Resource
    private MerchantStoreDao merchantStoreDao;

    @Resource
    private AccountService accountService;

    @Resource
    private AccountRoleMapService accountRoleMapService;

    @Resource
    private RoleService roleService;

    @Resource
    private RegionService regionService;

    @Resource
    private MerchantStoreBindQuotaLogDao merchantStoreBindQuotaLogDao;

    @Resource
    private SpuService spuService;

    @Resource
    private SkuService skuService;

    @Resource
    private StockSpuService stockSpuService;

    public void register(long merchantNo, long storeNo, String accountId, String phone) {
        MerchantStoreEntity entity = new MerchantStoreEntity();
        entity.setStoreNo(storeNo);
        entity.setMerchantNo(merchantNo);
        entity.setStoreName("智慧门店");
        entity.setStorePhone(phone);
        entity.setAccountId(accountId);
        entity.setMain(true);
        save(entity);
    }

    @Transactional(rollbackFor = CustomException.class)
    public void create(long merchantNo, MerchantStoreDto dto) {
        long storeNo = generateNextStoreNo(merchantNo);
        accountService.editStoreNo(dto.getAccountId(), storeNo, dto.getRoleId());

        LocalDate now = LocalDate.now();
        MerchantStoreEntity entity = new MerchantStoreEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setEndDate(now.plusDays(15));
        save(entity);
    }

    private long generateNextStoreNo(long merchantNo) {
        int storeCount = countByMerchantNo(merchantNo);
        return BasicConfigConst.STORE_START_NO + storeCount + 1;
    }

    public void save(MerchantStoreEntity entity) {
        entity.setId(Generate.generateUUID());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setStatus(CommonStatus.ENABLED.getCode());
        merchantStoreDao.save(entity);
        asyncGoods(entity.getMerchantNo(), entity.getStoreNo());
    }

    /**
     * 同步商品信息到门店
     *
     * @param merchantNo
     * @param storeNo
     */
    public void asyncGoods(long merchantNo, long storeNo) {
        SpuQuery spuQuery = new SpuQuery();
        List<SpuListVo> spus = spuService.list(merchantNo, spuQuery);
        spus.forEach(spu -> {
            StockSpuDto spuDto = new StockSpuDto();
            BeanUtils.copyProperties(spu, spuDto);
            spuDto.setSpuId(spu.getId());
            spuDto.setPictures(JsonUtil.toJSONString(spu.getPictures()));
            List<SkuVo> skus = skuService.findAllBySpuId(spu.getId());
            List<StockSkuDto> stockSkus = skus.stream().map(sku -> {
                StockSkuDto skuDto = new StockSkuDto();
                BeanUtils.copyProperties(sku, skuDto);
                skuDto.setSkuId(sku.getId());
                skuDto.setPurchaseAmount(AmountUtil.parse(sku.getPurchaseAmount()));
                skuDto.setReferenceAmount(AmountUtil.parse(sku.getReferenceAmount()));
                skuDto.setSkuWeight(StockUtil.parse(sku.getSkuWeight()));
                return skuDto;
            }).collect(Collectors.toList());
            spuDto.setSkus(stockSkus);
            stockSpuService.batchEdit(merchantNo, storeNo, spuDto);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(String id, MerchantStoreDto dto) {
        MerchantStoreEntity store = merchantStoreDao.findById(id);
        if (store == null) {
            return;
        }
        accountService.editStoreNo(dto.getAccountId(), store.getStoreNo(), dto.getRoleId());

        MerchantStoreEntity entity = new MerchantStoreEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUpdateTime(LocalDateTime.now());
        merchantStoreDao.edit(entity);
    }

    public void editStatus(String id, int status) {
        if (IEnum.getByCode(status, CommonStatus.class) == null) {
            throw new CustomException("状态信息错误");
        }
        merchantStoreDao.editStatusById(id, status);
    }

    @Transactional(rollbackFor = CustomException.class)
    public void bindQuota(long merchantNo, long addMonth, long bindStoreNo) {
        MerchantStoreEntity store = merchantStoreDao.findByStoreNo(merchantNo, bindStoreNo);
        if (store == null) {
            throw new CustomException("门店信息错误");
        }
        LocalDate now = LocalDate.now();
        if (store.getEndDate() != null) {
            if (store.getEndDate().isAfter(now)) {
                now = store.getEndDate();
            }
        }
        LocalDate endDate = now.plusMonths(addMonth);
        merchantStoreDao.editEndDateById(store.getId(), endDate);
        long addDays = Period.between(now, endDate).getDays();

        MerchantStoreBindQuotaLogEntity log = new MerchantStoreBindQuotaLogEntity();
        log.setId(Generate.generateUUID());
        log.setMerchantNo(merchantNo);
        log.setStoreNo(bindStoreNo);
        log.setBeforeEndDate(store.getEndDate());
        log.setAddDays(addDays);
        log.setAfterEndDate(endDate);
        log.setCreateTime(LocalDateTime.now());
        merchantStoreBindQuotaLogDao.save(log);
    }

    public void delete(String id) {
        MerchantStoreEntity entity = merchantStoreDao.findById(id);
        if (entity == null) {
            return;
        }
        if (entity.isMain()) {
            throw new CustomException("主门店不允许直接删除");
        }
        merchantStoreDao.delete(id);
    }

    public MerchantStoreVo findById(String id) {
        MerchantStoreEntity entity = merchantStoreDao.findById(id);
        if (entity == null) {
            return null;
        }
        return convertStore(entity);
    }

    public MerchantStoreEntity findMain(long merchantNo) {
        return merchantStoreDao.findMain(merchantNo);
    }

    public MerchantStoreVo findByStoreNo(long merchantNo, long storeNo) {
        MerchantStoreEntity entity = merchantStoreDao.findByStoreNo(merchantNo, storeNo);
        if (entity == null) {
            return null;
        }
        return convertStore(entity);
    }

    public List<MerchantStoreEntity> findAll() {
        return merchantStoreDao.findAll();
    }


    public List<MerchantStoreVo> findAllByStoreNos(long merchantNo, List<Long> storeNos) {
        List<MerchantStoreEntity> stores = merchantStoreDao.findAllByStoreNos(merchantNo, storeNos);
        return stores.stream().map(this::convertStore).collect(Collectors.toList());
    }

    public List<MerchantStoreEntity> findAllByMerchantNo(long merchantNo) {
        return merchantStoreDao.findAllByMerchantNo(merchantNo);
    }

    public int countByMerchantNo(long merchantNo) {
        return merchantStoreDao.countByMerchantNo(merchantNo);
    }

    public PageVo<MerchantStoreVo> page(long merchantNo, MerchantStoreQuery query) {
        int total = merchantStoreDao.countPage(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();

        }
        List<MerchantStoreEntity> list = merchantStoreDao.findPage(merchantNo, query);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<MerchantStoreVo> vos = list.stream().map(this::convertStore).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public List<MerchantStoreVo> list(long merchantNo) {
        List<MerchantStoreEntity> stores = merchantStoreDao.findAllByMerchantNo(merchantNo);
        return stores.stream().map(this::convertStore).collect(Collectors.toList());
    }

    private MerchantStoreVo convertStore(MerchantStoreEntity entity) {
        MerchantStoreVo vo = new MerchantStoreVo();
        BeanUtils.copyProperties(entity, vo);
        if (StringUtils.isNotBlank(vo.getArea())) {
            RegionPathVo region = regionService.findById(vo.getArea());
            if (region != null) {
                vo.setProvinceName(region.getProvinceName());
                vo.setCityName(region.getCityName());
                vo.setAreaName(region.getAreaName());
            }
        }
        AccountEntity account = accountService.findById(entity.getAccountId());
        if (account != null) {
            vo.setAccountName(account.getName());
            vo.setAccountPhone(account.getPhone());
        }
        AccountRoleMapEntity accountRoleMap = accountRoleMapService.findByAccountId(entity.getAccountId());
        if (accountRoleMap != null) {
            vo.setRoleId(accountRoleMap.getRoleId());
            RoleEntity role = roleService.info(entity.getMerchantNo(), accountRoleMap.getRoleId());
            if (role != null) {
                vo.setRoleName(role.getName());
            }
        }
        return vo;
    }
}
