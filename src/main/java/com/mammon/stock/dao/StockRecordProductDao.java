package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockRecordSkuEntity;
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
public class StockRecordProductDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockRecordSkuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_record_sku (")
                .append(" id, record_no, spu_id, sku_id, sku_name, record_quantity, ")
                .append(" record_amount, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :recordNo, :spuId, :skuId, :skuName, :recordQuantity, ")
                .append(" :recordAmount, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<StockRecordSkuEntity> findAllByRecordNo(String recordNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_record_sku ")
                .append(" WHERE record_no = :recordNo ");
        params.addValue("recordNo", recordNo);

        String sql = sb.toString();

        RowMapper<StockRecordSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockRecordSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
