package com.mammon.office.order.service;

import com.mammon.common.Generate;
import com.mammon.office.edition.domain.vo.PackageSkuListVo;
import com.mammon.office.edition.domain.vo.PackageSkuVo;
import com.mammon.office.edition.service.PackageSkuService;
import com.mammon.office.order.dao.OfficeOrderItemDao;
import com.mammon.office.order.domain.dto.OfficeOrderCreateDto;
import com.mammon.office.order.domain.entity.OfficeOrderItemEntity;
import com.mammon.office.order.domain.enums.OfficeOrderItemStatus;
import com.mammon.office.order.domain.vo.OfficeOrderItemVo;
import com.mammon.utils.AmountUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2023-02-02 13:24:57
 */
@Service
public class OfficeOrderItemService {

    @Resource
    private OfficeOrderItemDao officeOrderItemDao;

    @Resource
    private PackageSkuService packageSkuService;

    @Transactional(rollbackFor = Exception.class)
    public List<OfficeOrderItemEntity> create(String orderId, OfficeOrderCreateDto dto, PackageSkuVo sku) {
        OfficeOrderItemEntity item = new OfficeOrderItemEntity();
        item.setId(Generate.generateUUID());
        item.setOrderId(orderId);
        item.setSpuId(sku.getSpuId());
        item.setSkuId(sku.getId());
        item.setQuantity(sku.getQuantity());
        item.setType(sku.getType());
        item.setUnit(sku.getUnit());
        item.setPayableAmount(AmountUtil.parse(sku.getNowAmount()));
        item.setStatus(OfficeOrderItemStatus.waitActive.getCode());
        if (dto.getBindStoreNo() != null) {
            item.setBindStoreNo(dto.getBindStoreNo());
        }
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        officeOrderItemDao.save(item);

        List<OfficeOrderItemEntity> list = new ArrayList<>();
        list.add(item);
        return list;
    }

    public void editStatusById(String id, int status, String activeMessage, LocalDateTime activeTime) {
        officeOrderItemDao.editStatusById(id, status, activeMessage, activeTime);
    }

    public List<OfficeOrderItemVo> findAllByOrderId(String orderId) {
        List<OfficeOrderItemEntity> list = officeOrderItemDao.findAllByOrderId(orderId);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<String> skuIds = list.stream().map(OfficeOrderItemEntity::getSkuId).collect(Collectors.toList());
        List<PackageSkuListVo> skuVos = packageSkuService.findAllByIds(skuIds);
        return list.stream().map(x -> {
            OfficeOrderItemVo vo = new OfficeOrderItemVo();
            BeanUtils.copyProperties(x, vo);
            vo.setPayableAmount(AmountUtil.parseBigDecimal(x.getPayableAmount()));
            skuVos.stream()
                    .filter(y -> y.getId().equals(x.getSkuId()))
                    .findFirst()
                    .ifPresent(skuVo -> vo.setSkuName(skuVo.getName()));
            return vo;
        }).collect(Collectors.toList());
    }
}
