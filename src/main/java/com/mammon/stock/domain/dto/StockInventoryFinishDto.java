package com.mammon.stock.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2024/4/1 17:39
 */
@Data
public class StockInventoryFinishDto {

    private List<StockInventoryProductDto> products = new ArrayList<>();
}
