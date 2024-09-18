package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockPurchaseOrderSkuEntity;
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
public class StockPurchaseOrderSkuDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockPurchaseOrderSkuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_purchase_order_sku (")
                .append(" id, purchase_id, spu_id, sku_id, purchase_quantity, purchase_amount, replenish_quantity, ")
                .append(" refund_quantity, refund_amount, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :purchaseId, :spuId, :skuId, :purchaseQuantity, :purchaseAmount, :replenishQuantity, ")
                .append(" :refundQuantity, :refundAmount, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editReplenish(StockPurchaseOrderSkuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_purchase_order_sku ")
                .append(" set ")
                .append(" replenish_quantity = replenish_quantity + :replenishQuantity, ")
                .append(" update_time = :updateTime ")
                .append(" where id = :id ")
                .append(" AND purchase_id = :purchaseId ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("replenishQuantity", entity.getReplenishQuantity());
        params.addValue("updateTime", entity.getUpdateTime());
        params.addValue("id", entity.getId());
        params.addValue("purchaseId", entity.getPurchaseId());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editRefund(StockPurchaseOrderSkuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_purchase_order_sku ")
                .append(" set ")
                .append(" refund_quantity = refund_quantity + :refundQuantity, ")
                .append(" update_time = :updateTime ")
                .append(" where id = :id ")
                .append(" AND purchase_id = :purchaseId ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("refundQuantity", entity.getRefundQuantity());
        params.addValue("updateTime", entity.getUpdateTime());
        params.addValue("id", entity.getId());
        params.addValue("purchaseId", entity.getPurchaseId());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteAllByPurchaseNo(String purchaseId) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from m_stock_purchase_order_sku ")
                .append(" WHERE purchase_id = :purchaseId ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("purchaseId", purchaseId);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<StockPurchaseOrderSkuEntity> findAllByPurchaseId(String purchaseId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_purchase_order_sku ")
                .append(" WHERE purchase_id = :purchaseId ");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("purchaseId", purchaseId);

        String sql = sb.toString();

        RowMapper<StockPurchaseOrderSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockPurchaseOrderSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
