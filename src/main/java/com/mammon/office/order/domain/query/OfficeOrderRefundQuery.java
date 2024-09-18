package com.mammon.office.order.domain.query;

import com.mammon.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author dcl
 * @date 2023-03-06 15:05:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OfficeOrderRefundQuery extends PageQuery {

    private String refundNo;

    private Integer status;

    private LocalDate startCreateTime;

    private LocalDate endCreateTime;
}
