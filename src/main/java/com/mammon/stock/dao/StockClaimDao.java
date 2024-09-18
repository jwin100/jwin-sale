package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockClaimEntity;
import com.mammon.stock.domain.query.StockClaimPageQuery;
import org.apache.commons.lang3.StringUtils;
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
public class StockClaimDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockClaimEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_claim (")
                .append(" id, merchant_no, large_spu_id, large_spu_no, large_spu_code, large_spu_name, large_unit_id, ")
                .append(" small_spu_id, small_spu_no, small_spu_code, small_spu_name, small_unit_id, multiple, status, ")
                .append(" create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :largeSpuId, :largeSpuNo, :largeSpuCode, :largeSpuName, :largeUnitId, ")
                .append(" :smallSpuId, :smallSpuNo, :smallSpuCode, :smallSpuName, :smallUnitId, :multiple, :status, ")
                .append(" :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(StockClaimEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_claim ")
                .append(" set ")
                .append(" large_spu_id      = :largeSpuId, ")
                .append(" large_spu_no      = :largeSpuNo, ")
                .append(" large_spu_code    = :largeSpuCode, ")
                .append(" large_spu_name    = :largeSpuName, ")
                .append(" large_unit_id     = :largeUnitId, ")
                .append(" small_spu_id      = :smallSpuId, ")
                .append(" small_spu_no      = :smallSpuNo, ")
                .append(" small_spu_code    = :smallSpuCode, ")
                .append(" small_spu_name    = :smallSpuName, ")
                .append(" small_unit_id     = :smallUnitId, ")
                .append(" multiple          = :multiple, ")
                .append(" update_time       = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(String id, int status) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_claim ")
                .append(" set ")
                .append(" status    = :status ")
                .append(" where id  = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from m_stock_claim ")
                .append(" where id  = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public StockClaimEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_claim ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<StockClaimEntity> rowMapper = new BeanPropertyRowMapper<>(StockClaimEntity.class);
        List<StockClaimEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public StockClaimEntity findByLargeSpuId(long merchantNo, String largeSpuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_claim ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND large_spu_id = :largeSpuId ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("largeSpuId", largeSpuId);

        String sql = sb.toString();

        RowMapper<StockClaimEntity> rowMapper = new BeanPropertyRowMapper<>(StockClaimEntity.class);
        List<StockClaimEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public StockClaimEntity findBySmallSpuId(long merchantNo, String smallSpuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_claim ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND small_spu_id = :smallSpuId ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("smallSpuId", smallSpuId);

        String sql = sb.toString();

        RowMapper<StockClaimEntity> rowMapper = new BeanPropertyRowMapper<>(StockClaimEntity.class);
        List<StockClaimEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int countPage(long merchantNo, StockClaimPageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_stock_claim ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params));

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<StockClaimEntity> findPage(long merchantNo, StockClaimPageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_claim ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params))
                .append(" ORDER BY create_time DESC ");

        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<StockClaimEntity> rowMapper = new BeanPropertyRowMapper<>(StockClaimEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String pageWhere(StockClaimPageQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(query.getLargeKeyword())) {
            sb.append(" AND ( large_spu_no LIKE :largeKeyword OR large_spu_code LIKE :largeKeyword OR large_spu_name LIKE :largeKeyword ) ");
            params.addValue("largeKeyword", "%" + query.getLargeKeyword() + "%");
        }
        if (StringUtils.isNotBlank(query.getSmallKeyword())) {
            sb.append(" AND ( small_spu_no LIKE :smallKeyword OR small_spu_code LIKE :smallKeyword OR small_spu_name LIKE :smallKeyword ) ");
            params.addValue("smallKeyword", "%" + query.getSmallKeyword() + "%");
        }
        if (StringUtils.isNotBlank(query.getLargeUnitId())) {
            sb.append(" AND large_unit_id = :largeUnitId ");
            params.addValue("largeUnitId", query.getLargeUnitId());
        }
        if (StringUtils.isNotBlank(query.getSmallUnitId())) {
            sb.append(" AND small_unit_id = :smallUnitId ");
            params.addValue("smallUnitId", query.getSmallUnitId());
        }
        if (query.getStatus() != null) {
            sb.append(" AND status = :status ");
            params.addValue("status", query.getStatus());
        }
        return sb.toString();
    }
}
