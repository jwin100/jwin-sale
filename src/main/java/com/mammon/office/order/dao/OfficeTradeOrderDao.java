package com.mammon.office.order.dao;

import com.mammon.office.order.domain.entity.OfficeTradeOrderEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2023-03-20 17:16:18
 */
@Repository
public class OfficeTradeOrderDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(OfficeTradeOrderEntity entity) {
        return 1;
    }

    public int editStatus(String id, int originalStatus, int status) {
        return 1;
    }

    public OfficeTradeOrderEntity findByTradeNo(String outTradeNo, String tradeNo) {
        return null;
    }
}
