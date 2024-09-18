package com.mammon.office.order.service;

import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.enums.IEnum;
import com.mammon.exception.CustomException;
import com.mammon.leaf.enums.DocketType;
import com.mammon.leaf.service.LeafCodeService;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.office.edition.domain.enums.IndustryType;
import com.mammon.office.edition.domain.vo.PackageSkuVo;
import com.mammon.office.edition.service.PackageSkuService;
import com.mammon.office.edition.service.PackageSpuService;
import com.mammon.office.order.channel.factory.BaseTradePayChannel;
import com.mammon.office.order.channel.factory.TradePayChannelFactory;
import com.mammon.office.order.channel.factory.model.TradePayVo;
import com.mammon.office.order.common.WorkerPool;
import com.mammon.office.order.dao.OfficeOrderDao;
import com.mammon.office.order.domain.dto.OfficeOrderCreateDto;
import com.mammon.office.order.domain.dto.OfficeOrderPayDto;
import com.mammon.office.order.domain.entity.OfficeOrderEntity;
import com.mammon.office.order.domain.entity.OfficeOrderItemEntity;
import com.mammon.office.order.domain.entity.OfficePayChannelEntity;
import com.mammon.office.order.domain.enums.OfficeOrderPayType;
import com.mammon.office.order.domain.enums.OfficeOrderStatus;
import com.mammon.office.order.domain.query.OfficeOrderQuery;
import com.mammon.office.order.domain.vo.OfficeOrderCreateVo;
import com.mammon.office.order.domain.vo.OfficeOrderDetailVo;
import com.mammon.office.order.domain.vo.OfficeOrderItemVo;
import com.mammon.office.order.domain.vo.OfficeOrderVo;
import com.mammon.utils.AmountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2023-02-02 13:24:42
 */
@Service
@Slf4j
public class OfficeOrderService {

    @Resource
    private OfficeOrderDao officeOrderDao;

    @Resource
    private OfficeOrderItemService officeOrderItemService;

    @Resource
    private PackageSpuService packageSpuService;

    @Resource
    private PackageSkuService packageSkuService;

    @Resource
    private OfficeOrderActiveService officeOrderActiveService;


    @Resource
    private OfficePayChannelService officePayChannelService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private LeafCodeService leafCodeService;

    public OfficeOrderCreateVo create(long merchantNo, long storeNo, String accountId, OfficeOrderCreateDto dto) {
        PackageSkuVo sku = packageSkuService.findBySkuId(dto.getSkuId());
        if (sku == null) {
            throw new CustomException("商品信息错误");
        }
        validStore(sku, merchantNo, dto.getBindStoreNo());
        String orderId = Generate.generateUUID();
        String orderNo = leafCodeService.generateDocketNo(DocketType.CASHIER_ORDER);
        String subject = sku.getName();

        List<OfficeOrderItemEntity> items = officeOrderItemService.create(orderId, dto, sku);
        long payableAmount = items.stream().mapToLong(OfficeOrderItemEntity::getPayableAmount).sum();

        OfficeOrderEntity order = new OfficeOrderEntity();
        order.setId(orderId);
        order.setMerchantNo(merchantNo);
        order.setStoreNo(storeNo);
        order.setAccountId(accountId);
        order.setOrderNo(orderNo);
        order.setSubject(subject);
        order.setPayableAmount(payableAmount);
        order.setStatus(OfficeOrderStatus.waitPay.getCode());
        order.setSource(dto.getSource());
        order.setRemark(dto.getRemark());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        officeOrderDao.save(order);

        OfficeOrderCreateVo vo = new OfficeOrderCreateVo();
        vo.setOrderId(orderId);
        vo.setOrderNo(orderNo);
        vo.setSubject(subject);
        vo.setPayableAmount(AmountUtil.parseBigDecimal(payableAmount));
        return vo;
    }

    private void validStore(PackageSkuVo sku, long merchantNo, Long bindStoreNo) {
        if (sku.getType() == IndustryType.storeQuota.getCode()) {
            if (bindStoreNo == null) {
                throw new CustomException("购买门店额度必须选择绑定门店");
            }
            MerchantStoreVo store = merchantStoreService.findByStoreNo(merchantNo, bindStoreNo);
            if (store == null) {
                throw new CustomException("门店信息错误");
            }
            if (store.isMain()) {
                throw new CustomException("主店不能绑定门店额度");
            }
        }
    }

    public String tradePrePay(long merchantNo, String orderId, OfficeOrderPayDto dto) {
        OfficeOrderEntity order = validPayOrder(merchantNo, orderId);
        OfficePayChannelEntity payChannel = buildPayChannel(dto.getPayType());
        if (payChannel == null) {
            throw new CustomException("支付方式错误");
        }
        officeOrderDao.editPayTypeById(order.getId(), payChannel.getId(), dto.getPayType());
        BaseTradePayChannel factory = TradePayChannelFactory.get(payChannel.getCode());
        if (factory == null) {
            throw new CustomException("支付通道信息错误");
        }
        return factory.tradeNative(payChannel.getCode(), payChannel.getConfigStr(), order);
    }

    private OfficeOrderEntity validPayOrder(long merchantNo, String orderId) {
        OfficeOrderEntity order = officeOrderDao.findById(merchantNo, orderId);
        if (order == null) {
            throw new CustomException("订单错误");
        }
        if (order.getStatus() == OfficeOrderStatus.paySuccess.getCode()) {
            throw new CustomException("订单已支付成功，不可重复支付");
        }
        if (order.getStatus() == OfficeOrderStatus.payCancel.getCode()) {
            throw new CustomException("此订单已关闭，不可继续支付");
        }
        if (order.getStatus() == OfficeOrderStatus.refunding.getCode()
                || order.getStatus() == OfficeOrderStatus.refunded.getCode()) {
            throw new CustomException("此订单正在进行退款操作，不可支付");
        }
        return order;
    }

    private OfficePayChannelEntity buildPayChannel(int payType) {
        OfficeOrderPayType orderPayType = IEnum.getByCode(payType, OfficeOrderPayType.class);
        if (orderPayType == null || !orderPayType.isStatus()) {
            return null;
        }
        return officePayChannelService.findByCode(orderPayType.getChannelCode());
    }

    public OfficeOrderVo findById(long merchantNo, String id) {
        OfficeOrderEntity order = officeOrderDao.findById(merchantNo, id);
        if (order == null) {
            throw new CustomException("订单不存在");
        }
        OfficeOrderVo vo = new OfficeOrderVo();
        BeanUtils.copyProperties(order, vo);
        vo.setPayableAmount(AmountUtil.parseBigDecimal(order.getPayableAmount()));
        return vo;
    }

    public OfficeOrderVo findByOrderNo(long merchantNo, String orderNo) {
        OfficeOrderEntity order = officeOrderDao.findByOrderNo(merchantNo, orderNo);
        if (order == null) {
            throw new CustomException("订单不存在");
        }
        OfficeOrderVo vo = new OfficeOrderVo();
        BeanUtils.copyProperties(order, vo);
        vo.setPayableAmount(AmountUtil.parseBigDecimal(order.getPayableAmount()));
        return vo;
    }

    public void loopQuery(long merchantNo, String orderId) {
        WorkerPool.execute(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.error("查单thread异常,{}", e.getMessage(), e);
            }

            int queryTimes = 10;
            while (queryTimes-- > 0) {
                int orderQueryResult = tradeQuery(orderId);
                if (orderQueryResult == OfficeOrderStatus.paySuccess.getCode()
                        || orderQueryResult == OfficeOrderStatus.payCancel.getCode()) {
                    break;
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    log.error("查单thread异常,{}", e.getMessage(), e);
                }
            }
        });
    }

    /**
     * 创建线程池轮询查单
     *
     * @param orderId
     */
    public int tradeQuery(String orderId) {
        OfficeOrderVo order = findById(0, orderId);
        if (order == null) {
            throw new CustomException("订单不存在");
        }
        if (order.getStatus() != OfficeOrderStatus.waitPay.getCode()) {
            return order.getStatus();
        }

        OfficePayChannelEntity payChannel = buildPayChannel(order.getPayType());
        if (payChannel == null) {
            log.error("获取支付通道信息错误,orderNo:{},payType:{}", orderId, order.getPayType());
            return OfficeOrderStatus.waitPay.getCode();
        }

        BaseTradePayChannel factory = TradePayChannelFactory.get(payChannel.getCode());
        TradePayVo payVo = factory.tradeQuery(payChannel.getConfigStr(), order.getOrderNo());

        if (payVo.getStatus() == OfficeOrderStatus.paySuccess.getCode() ||
                payVo.getStatus() == OfficeOrderStatus.payCancel.getCode()) {
            payResult(order.getMerchantNo(), order.getStoreNo(),
                    order.getId(), OfficeOrderStatus.waitPay.getCode(), payVo);
        }
        return payVo.getStatus();
    }

    public void payResult(long merchantNo, long storeNo, String orderId,
                          int originalStatus, TradePayVo payVo) {
        officeOrderDao.editPayStatusById(orderId, originalStatus,
                payVo.getTradeNo(), payVo.getStatus(), payVo.getMessage(), payVo.getPayTime());
        if (payVo.getStatus() == OfficeOrderStatus.paySuccess.getCode()) {
            officeOrderActiveService.active(merchantNo, storeNo, orderId);
            editStatusById(orderId, OfficeOrderStatus.paySuccess.getCode(),
                    OfficeOrderStatus.payFinish.getCode());
        }
    }

    public void editStatusById(String id, int originalStatus, int status) {
        officeOrderDao.editStatusById(id, originalStatus, status);
    }

    public PageVo<OfficeOrderVo> page(long merchantNo, OfficeOrderQuery query) {
        //仅仅返回目录列表
        int total = officeOrderDao.countPage(merchantNo, query);
        if (total <= 0) {
            return PageResult.of();
        }
        List<OfficeOrderEntity> list = officeOrderDao.findPage(merchantNo, query);
        List<OfficeOrderVo> vos = list.stream().map(x -> {
            OfficeOrderVo vo = new OfficeOrderVo();
            BeanUtils.copyProperties(x, vo);
            vo.setPayableAmount(AmountUtil.parseBigDecimal(x.getPayableAmount()));
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(query.getPageIndex(), query.getPageSize(), total, vos);
    }

    public OfficeOrderDetailVo findDetailById(long merchantNo, String id) {
        OfficeOrderEntity order = officeOrderDao.findById(merchantNo, id);
        if (order == null) {
            throw new CustomException("订单不存在");
        }
        OfficeOrderDetailVo vo = new OfficeOrderDetailVo();
        BeanUtils.copyProperties(order, vo);
        vo.setPayableAmount(AmountUtil.parseBigDecimal(order.getPayableAmount()));
        List<OfficeOrderItemVo> items = officeOrderItemService.findAllByOrderId(id);
        vo.setItems(items);
        return vo;
    }
}
