package com.mammon.cashier.dao;

import com.mammon.cashier.domain.entity.CashierIgnoreEntity;
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
public class CashierIgnoreDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CashierIgnoreEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_cashier_ignore (")
                .append(" id, merchant_no, type, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :type, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(CashierIgnoreEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_ignore ")
                .append(" set ")
                .append(" type          = :type, ")
                .append(" update_time   = :updateTime ")
                .append(" where id      = :id ");

        String sql = sb.toString();

        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public CashierIgnoreEntity findByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_ignore ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<CashierIgnoreEntity> rowMapper = new BeanPropertyRowMapper<>(CashierIgnoreEntity.class);
        List<CashierIgnoreEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
