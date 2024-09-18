package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockAllocateProductEntity;
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
public class StockAllocateProductDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockAllocateProductEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_allocate_product (")
                .append(" id, allocate_no, spu_id, sku_id, sku_name, unit_id, unit_name, allocate_quantity, ")
                .append(" out_quantity, in_quantity, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :allocateNo, :spuId, :skuId, :skuName, :unitId, :unitName, :allocateQuantity, ")
                .append(" :outQuantity, :inQuantity, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(StockAllocateProductEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_allocate_product ")
                .append(" set ")
                .append(" allocate_quantity = :allocateQuantity ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from m_stock_allocate_product ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int outStock(String id, long outQuantity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_allocate_product ")
                .append(" set ")
                .append(" out_quantity = :outQuantity ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("outQuantity", outQuantity);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int inStock(String id, long inQuantity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_allocate_product ")
                .append(" set ")
                .append(" in_quantity = :inQuantity ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("inQuantity", inQuantity);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<StockAllocateProductEntity> findAllByAllocateNo(String allocateNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_allocate_product ")
                .append(" WHERE allocate_no = :allocateNo ");

        params.addValue("allocateNo", allocateNo);

        String sql = sb.toString();

        RowMapper<StockAllocateProductEntity> rowMapper = new BeanPropertyRowMapper<>(StockAllocateProductEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
