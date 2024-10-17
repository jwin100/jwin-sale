package com.mammon.stock.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2024/10/17 17:12
 */
@Data
public class StockSkuSpecVo {

    private String specId;

    private String specName;

    private List<StockSkuSpecValueVo> specValues = new ArrayList<>();
}
