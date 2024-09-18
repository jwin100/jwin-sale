package com.mammon.cashier.dao;

import com.mammon.cashier.domain.entity.CashierRefundProductEntity;
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
public class CashierRefundProductDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CashierRefundProductEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_cashier_refund_product (")
                .append(" id, refund_id, order_product_id, spu_id, sku_id, sku_name, picture, refund_quantity, ")
                .append(" reference_amount, adjust_amount, payable_amount, ")
                .append(" status, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :refundId, :orderProductId, :spuId, :skuId, :skuName, :picture, :refundQuantity, ")
                .append(" :referenceAmount, :adjustAmount, :payableAmount, ")
                .append(" :status, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<CashierRefundProductEntity> findAllByRefundIds(List<String> refundIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_refund_product ")
                .append(" WHERE refund_id in ( :refundIds ) ");
        params.addValue("refundIds", refundIds);

        String sql = sb.toString();

        RowMapper<CashierRefundProductEntity> rowMapper = new BeanPropertyRowMapper<>(CashierRefundProductEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<CashierRefundProductEntity> findAllByRefundId(String refundId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_refund_product ")
                .append(" WHERE refund_id = :refundId ");
        params.addValue("refundId", refundId);

        String sql = sb.toString();

        RowMapper<CashierRefundProductEntity> rowMapper = new BeanPropertyRowMapper<>(CashierRefundProductEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<CashierRefundProductEntity> findAllByOrderProductId(String orderProductId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_refund_product ")
                .append(" WHERE order_product_id = :orderProductId ");
        params.addValue("orderProductId", orderProductId);

        String sql = sb.toString();

        RowMapper<CashierRefundProductEntity> rowMapper = new BeanPropertyRowMapper<>(CashierRefundProductEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
