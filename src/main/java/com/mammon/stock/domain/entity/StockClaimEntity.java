package com.mammon.stock.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 组装拆包
 *
 * @author dcl
 * @since 2024/3/12 14:10
 */
@Data
public class StockClaimEntity {

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

    /**
     * 小件商品
     */
    private String smallSpuId;

    private String smallSpuNo;

    private String smallSpuCode;

    private String smallSpuName;

    private String smallUnitId;

    /**
     * 大包装数量 =小包装数量x倍数
     */
    private long multiple;

    /**
     * 状态
     */
    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
