package com.mammon.stock.domain.vo;

import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/3/12 16:06
 */
@Data
public class StockClaimPageVo {

    private String id;

    private long merchantNo;

    /**
     * 大件商品
     */
    private String largeSpuId;

    private String largeSpuNo;

    private String largeSpuCode;

    private String largeSpuName;

    private String largeUnitId;

    private String largeUnitName;

    /**
     * 小件商品
     */
    private String smallSpuId;

    private String smallSpuNo;

    private String smallSpuCode;

    private String smallSpuName;

    private String smallUnitId;

    private String smallUnitName;

    /**
     * 大包装数量 =小包装数量x倍数
     */
    private long multiple;

    /**
     * 状态
     */
    private int status;

    private String statusName;

    private LocalDateTime createTime;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }
}
