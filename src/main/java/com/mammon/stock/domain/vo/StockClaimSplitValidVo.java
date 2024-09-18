package com.mammon.stock.domain.vo;

import com.mammon.stock.domain.entity.StockClaimSkuEntity;
import lombok.Data;

/**
 * @author dcl
 * @since 2024/3/19 14:44
 */
@Data
public class StockClaimSplitValidVo {

    private boolean result;

    private String msg;

    private StockClaimSkuEntity claimSku;
}
