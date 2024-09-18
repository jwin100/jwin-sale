package com.mammon.stock.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author dcl
 * @date 2023-04-20 17:08:09
 */
@Data
@Builder
public class StockChangeVo {

    /**
     * 1 成功，0失败
     */
    private int code;

    /**
     * 结果描述
     */
    private String message;
}
