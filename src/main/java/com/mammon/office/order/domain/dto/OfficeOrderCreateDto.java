package com.mammon.office.order.domain.dto;

import lombok.Data;

/**
 * @author dcl
 * @date 2023-02-02 13:26:32
 */
@Data
public class OfficeOrderCreateDto {

    /**
     * 套餐包id
     */
    private String skuId;

    /**
     * 绑定到门店
     */
    private Long bindStoreNo;

    private int source;

    private String remark;
}
