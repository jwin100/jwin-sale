package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockSkuLockEntity;
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
 * @date 2023-06-08 16:12:58
 */
@Repository
public class StockSkuLockDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockSkuLockEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_sku_lock (")
                .append(" id, spu_id, sku_id, merchant_no, store_no, order_id, lock_stock, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :spuId, :skuId, :merchantNo, :storeNo, :orderId, :lockStock, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ")
                .append(" FROM  m_stock_sku_lock ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);
        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public StockSkuLockEntity findByOrderId(long merchantNo, long storeNo, String orderId, String skuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_sku_lock ")
                .append(" WHERE merchant_no = :merchantNo AND store_no = :storeNo ")
                .append(" AND order_id = :orderId AND sku_id = :skuId ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("orderId", orderId);
        params.addValue("skuId", skuId);

        String sql = sb.toString();

        RowMapper<StockSkuLockEntity> rowMapper = new BeanPropertyRowMapper<>(StockSkuLockEntity.class);
        List<StockSkuLockEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
