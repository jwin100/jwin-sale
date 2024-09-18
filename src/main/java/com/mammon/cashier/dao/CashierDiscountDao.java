package com.mammon.cashier.dao;

import com.mammon.cashier.domain.entity.CashierDiscountEntity;
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
public class CashierDiscountDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CashierDiscountEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_cashier_discount (")
                .append(" id, merchant_no, discount, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :discount, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(CashierDiscountEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_discount ")
                .append(" set ")
                .append(" discount      = :discount, ")
                .append(" update_time   = :updateTime ")
                .append(" where id      = :id ");

        String sql = sb.toString();

        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ")
                .append(" FROM  m_cashier_discount ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);
        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<CashierDiscountEntity> findAllByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_discount ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" ORDER BY discount ASC ");

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<CashierDiscountEntity> rowMapper = new BeanPropertyRowMapper<>(CashierDiscountEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
