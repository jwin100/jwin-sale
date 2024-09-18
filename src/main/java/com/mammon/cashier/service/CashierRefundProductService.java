package com.mammon.cashier.service;

import com.mammon.cashier.domain.vo.CashierRefundSkuComputeVo;
import com.mammon.common.Generate;
import com.mammon.exception.CustomException;
import com.mammon.cashier.dao.CashierRefundProductDao;
import com.mammon.cashier.domain.entity.CashierRefundProductEntity;
import com.mammon.cashier.domain.entity.CashierOrderProductEntity;
import com.mammon.cashier.domain.enums.OrderProductStatusConst;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.StockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CashierRefundProductService {

    @Resource
    private CashierOrderProductService cashierOrderProductService;


    @Resource
    private CashierRefundProductDao cashierRefundProductDao;

    /**
     * 初始化退款商品
     *
     * @param orderId  订单ID
     * @param refundId 退款ID
     * @param skus     退款商品计算信息列表
     * @return 退款商品实体列表
     * @throws CustomException 自定义异常
     */
    @Transactional(rollbackFor = CustomException.class)
    public List<CashierRefundProductEntity> initRefundProduct(String orderId, String refundId,
                                                              List<CashierRefundSkuComputeVo> skus) {
        List<CashierOrderProductEntity> orderProducts = cashierOrderProductService.findAllByOrderId(orderId);
        return skus.stream().map(x -> {
            CashierOrderProductEntity orderProduct = orderProducts.stream()
                    .filter(y -> y.getId().equals(x.getId()))
                    .findFirst().orElse(null);
            if (orderProduct != null && x.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                String skuName = orderProduct.getSkuName();
                CashierRefundProductEntity entity = new CashierRefundProductEntity();
                entity.setId(Generate.generateUUID());
                entity.setRefundId(refundId);
                entity.setOrderProductId(orderProduct.getId());
                entity.setSpuId(orderProduct.getSpuId());
                entity.setSkuId(orderProduct.getSkuId());
                entity.setSkuName(skuName);
                entity.setPicture(orderProduct.getPicture());
                entity.setRefundQuantity(StockUtil.parse(x.getQuantity()));
                entity.setReferenceAmount(AmountUtil.parse(x.getReferenceAmount()));
                entity.setAdjustAmount(AmountUtil.parse(x.getAdjustAmount()));
                entity.setPayableAmount(AmountUtil.parse(x.getPayableAmount()));
                entity.setStatus(OrderProductStatusConst.退货);
                entity.setCreateTime(LocalDateTime.now());
                entity.setUpdateTime(LocalDateTime.now());
                cashierRefundProductDao.save(entity);
                return entity;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<CashierRefundProductEntity> findAllByRefundIds(List<String> refundIds) {
        return cashierRefundProductDao.findAllByRefundIds(refundIds);
    }

    public List<CashierRefundProductEntity> findAllByRefundId(String refundId) {
        return cashierRefundProductDao.findAllByRefundId(refundId);
    }

    public List<CashierRefundProductEntity> findAllByOrderProductId(String orderProductId) {
        return cashierRefundProductDao.findAllByOrderProductId(orderProductId);
    }
}
