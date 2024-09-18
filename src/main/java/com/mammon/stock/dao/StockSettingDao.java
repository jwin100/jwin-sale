package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockSettingEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2023/12/27 10:19
 */
@Repository
public class StockSettingDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockSettingEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_setting (")
                .append(" merchant_no, purchase_order_examine, purchase_refund_examine, allocate_examine, claim ")
                .append(" ) VALUES ( ")
                .append(" :merchantNo, :purchaseOrderExamine, :purchaseRefundExamine, :allocateExamine, :claim ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(StockSettingEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_setting ")
                .append(" set ")
                .append(" purchase_order_examine = :purchaseOrderExamine, ")
                .append(" purchase_refund_examine = :purchaseRefundExamine, ")
                .append(" allocate_examine = :allocateExamine, ")
                .append(" claim = :claim ")
                .append(" where merchant_no = :merchantNo ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public StockSettingEntity findByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_setting ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<StockSettingEntity> rowMapper = new BeanPropertyRowMapper<>(StockSettingEntity.class);
        List<StockSettingEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
