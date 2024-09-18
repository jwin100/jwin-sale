package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockPurchaseRefundSkuEntity;
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
 * @date 2023-03-27 16:07:25
 */
@Repository
public class StockPurchaseRefundSkuDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockPurchaseRefundSkuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_purchase_refund_sku (")
                .append(" id, refund_id, spu_id, sku_id, refund_quantity, refund_amount, ")
                .append(" create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :refundId, :spuId, :skuId, :refundQuantity, :refundAmount, ")
                .append(" :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteAllByRefundId(String refundId) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from m_stock_purchase_refund_sku ")
                .append(" WHERE refund_id = :refundId ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("refundId", refundId);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<StockPurchaseRefundSkuEntity> findAllByRefundId(String refundId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_purchase_refund_sku ")
                .append(" WHERE refund_id = :refundId ");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("refundId", refundId);

        String sql = sb.toString();

        RowMapper<StockPurchaseRefundSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockPurchaseRefundSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
