package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockInventoryProductEntity;
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
 * @since 2024/4/1 16:27
 */
@Repository
public class StockInventoryProductDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockInventoryProductEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_inventory_product (")
                .append(" id, inventory_id, spu_id, spu_name, category_id, category_name, before_stock, ")
                .append(" reality_stock, phase_stock, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :inventoryId, :spuId, :spuName, :categoryId, :categoryName, :beforeStock, ")
                .append(" :realityStock, :phaseStock, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<StockInventoryProductEntity> findByInventoryId(String inventoryId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_inventory_product ")
                .append(" WHERE inventory_id  = :inventoryId ");
        params.addValue("inventoryId", inventoryId);

        String sql = sb.toString();

        RowMapper<StockInventoryProductEntity> rowMapper = new BeanPropertyRowMapper<>(StockInventoryProductEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
