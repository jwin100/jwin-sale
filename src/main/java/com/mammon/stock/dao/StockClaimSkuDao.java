package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockClaimSkuEntity;
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
 * @since 2024/3/12 15:05
 */
@Repository
public class StockClaimSkuDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockClaimSkuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_claim_sku (")
                .append(" id, split_id, large_spu_id, large_sku_id, large_sku_no, large_sku_code, large_sku_name, ")
                .append(" small_spu_id, small_sku_id, small_sku_no, small_sku_code, small_sku_name ")
                .append(" ) VALUES ( ")
                .append(" :id, :splitId, :largeSpuId, :largeSkuId, :largeSkuNo, :largeSkuCode, :largeSkuName, ")
                .append(" :smallSpuId, :smallSkuId, :smallSkuNo, :smallSkuCode, :smallSkuName ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteBySplitId(String splitId) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from m_stock_claim_sku ")
                .append(" where split_id  = :splitId ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("splitId", splitId);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<StockClaimSkuEntity> findAllBySplitId(String splitId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_claim_sku ")
                .append(" WHERE split_id  = :splitId ");
        params.addValue("splitId", splitId);

        String sql = sb.toString();

        RowMapper<StockClaimSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockClaimSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
