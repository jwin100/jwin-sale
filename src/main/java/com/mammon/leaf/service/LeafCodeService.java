package com.mammon.leaf.service;

import com.mammon.exception.CustomException;
import com.mammon.leaf.dao.LeafCodeDao;
import com.mammon.leaf.domain.entity.LeafCodeEntity;
import com.mammon.leaf.enums.DocketType;
import com.mammon.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author dcl
 * @since 2024/1/25 10:54
 */
@Service
public class LeafCodeService {

    public static final LocalDateTime BEGIN_DATE = LocalDateTime.of(2024, 1, 1, 0, 0);
    private static final String DOCKET_NO = "CASHIER_CUSTOMER_NO_%s";
    private static final String LAST_DOCKET_NO = "LAST_DOCKET_NO";
    public static long sequence = 0;
    // sequence掩码，确保sequnce不会超出上限
    private static final long sequenceMask = 9999;

    @Resource
    private LeafCodeDao leafCodeDao;

    @Resource
    private RedisService redisService;

    public String generateSpuCode(long merchantNo) {
        validLeafCode(merchantNo);
        long diffMillIs = generateNo();
        LeafCodeEntity entity = new LeafCodeEntity();
        entity.setMerchantNo(merchantNo);
        entity.setGoodsSpuCode(1);
        LeafCodeEntity leafCode = leafCodeDao.edit(entity);
        if (leafCode == null) {
            throw new CustomException("商品编号生成错误");
        }
        return DocketType.GOODS_SPU_CODE.getPrefix() + diffMillIs + leafCode.getGoodsSpuCode();
    }

    public String generateSkuCode(long merchantNo) {
        validLeafCode(merchantNo);
        long diffMillIs = generateNo();
        LeafCodeEntity entity = new LeafCodeEntity();
        entity.setMerchantNo(merchantNo);
        entity.setGoodsSkuCode(1);
        LeafCodeEntity leafCode = leafCodeDao.edit(entity);
        if (leafCode == null) {
            throw new CustomException("商品编号生成错误");
        }
        return DocketType.GOODS_SKU_CODE.getPrefix() + diffMillIs + leafCode.getGoodsSkuCode();
    }

    /**
     * 第一部分 单据类型 （max 2位）
     * <p>
     * 第二部分 单据日期（6未）
     * <p>
     * 第三部分 距离当天0点到当前日期的秒数（5位）
     * <p>
     * 最后一部分 数据库自增编号（每日重置）额定一秒内不会超过一万个单据（4位）
     * <p>
     * 示例 S 240125 03234 0001
     *
     * @return 单据号
     */
    public String generateDocketNo(DocketType type) {
        long nextDocketGen = getNextDocketGen();
        return type.getPrefix() + nextDocketGen;
    }

    private void validLeafCode(long merchantNo) {
        boolean exists = leafCodeDao.exists(merchantNo);
        if (!exists) {
            leafCodeDao.save(merchantNo);
        }
    }

    private long getNextDocketGen() {
        long lastNo = 0;
        String lastNoStr = redisService.get(LAST_DOCKET_NO);
        if (StringUtils.isNotBlank(lastNoStr)) {
            lastNo = Long.parseLong(lastNoStr);
        }
        long gen = getDocketGen();
        while (gen <= lastNo) {
            gen = getDocketGen();
        }
        redisService.set(LAST_DOCKET_NO, String.valueOf(gen));
        sequence = 0;
        return gen;
    }

    private long getDocketGen() {
        sequence++;
        if (sequence > sequenceMask) {
            sequence = 1;
        }
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
        long second = Duration.between(now.toLocalDate().atStartOfDay(), now).getSeconds();
        String secondStr = StringUtils.leftPad(String.valueOf(second), 5, "0");
        String sequenceStr = StringUtils.leftPad(String.valueOf(sequence), 4, "0");
        return Long.parseLong(dateStr + secondStr + sequenceStr);
    }

    private Long generateNo() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate nowDate = now.toLocalDate();
        // 距离startNow多少天
        long diffDays = Duration.between(BEGIN_DATE, now).toDays();
        // 距离0时的毫秒数
        long diffMillis = Duration.between(nowDate.atStartOfDay(), now).toMillis();
        return diffDays + diffMillis;
    }
}
