package com.mammon.office.order.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author dcl
 * @date 2023-03-05 20:34:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OfficeOrderQuery extends PageQuery {

    private String orderNo;

    private Integer status;

    private Integer payType;

    private LocalDate startCreateTime;

    private LocalDate endCreateTime;
}
