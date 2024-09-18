package com.mammon.office.order.dao;

import com.mammon.office.order.domain.entity.OfficeTradeLogEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2023-03-02 15:58:47
 */
@Repository
public class OfficeTradeLogDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(OfficeTradeLogEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_office_trade_log (")
                .append(" id, trade_no, pay_type, trade_type, pay_params, pay_result, remark, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :tradeNo, :payType, :tradeType, :payParams, :payResult, :remark, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }
}
