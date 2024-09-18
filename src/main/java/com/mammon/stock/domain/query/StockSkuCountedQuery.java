package com.mammon.stock.domain.query;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2023/12/9 11:03
 */
@Data
public class StockSkuCountedQuery {

    private List<String> spuIds = new ArrayList<>();
}
