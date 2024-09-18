package com.mammon.cashier.dao;

import com.mammon.cashier.domain.entity.CashierFlowEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CashierFlowDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CashierFlowEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_cashier_flow (")
                .append(" id, merchant_no, custom_flow_model, rating_rest_flow, init_flow, ")
                .append(" create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :customFlowModel, :ratingRestFlow, :initFlow, ")
                .append(" :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(CashierFlowEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_flow ")
                .append(" set ")
                .append(" custom_flow_model = :customFlowModel, ")
                .append(" rating_rest_flow  = :ratingRestFlow, ")
                .append(" init_flow         = :initFlow, ")
                .append(" update_time       = :updateTime ")
                .append(" where id          = :id ");

        String sql = sb.toString();

        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public CashierFlowEntity findByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_flow ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<CashierFlowEntity> rowMapper = new BeanPropertyRowMapper<>(CashierFlowEntity.class);
        List<CashierFlowEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
