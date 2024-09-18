package com.mammon.cashier.dao;

import com.mammon.cashier.domain.entity.CashierOrderProductEntity;
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
public class CashierOrderProductDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CashierOrderProductEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_cashier_order_product (")
                .append(" id, order_id, spu_id, sku_id, sku_name, picture, sale_quantity, reference_amount, ")
                .append(" adjust_amount, payable_amount, divide_amount, refund_quantity, refund_amount, ")
                .append(" status, integral, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :orderId, :spuId, :skuId, :skuName, :picture, :saleQuantity, :referenceAmount, ")
                .append(" :adjustAmount, :payableAmount, :divideAmount, :refundQuantity, :refundQuantity, ")
                .append(" :status, :integral, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateRefund(String id, long refundQuantity, long refundAmount) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_order_product ")
                .append(" set ")
                .append(" refund_quantity = :refundQuantity, ")
                .append(" refund_amount = :refundAmount ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("refundQuantity", refundQuantity);
        params.addValue("refundAmount", refundAmount);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<CashierOrderProductEntity> findAllByOrderId(String orderId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_order_product ")
                .append(" WHERE order_id = :orderId ");

        params.addValue("orderId", orderId);

        String sql = sb.toString();

        RowMapper<CashierOrderProductEntity> rowMapper = new BeanPropertyRowMapper<>(CashierOrderProductEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<CashierOrderProductEntity> findAllByOrderIds(List<String> orderIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_order_product ")
                .append(" WHERE order_id in ( :orderIds ) ");

        params.addValue("orderIds", orderIds);

        String sql = sb.toString();

        RowMapper<CashierOrderProductEntity> rowMapper = new BeanPropertyRowMapper<>(CashierOrderProductEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
