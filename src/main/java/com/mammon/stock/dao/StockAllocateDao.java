package com.mammon.stock.dao;

import com.mammon.stock.domain.query.StockAllocateQuery;
import com.mammon.stock.domain.entity.StockAllocateEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class StockAllocateDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockAllocateEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_allocate (")
                .append(" id, merchant_no, store_no, allocate_no, in_store_no, out_store_no, status, remark, ")
                .append(" operation_id, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :allocateNo, :inStoreNo, :outStoreNo, :status, :remark, ")
                .append(" :operationId, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(StockAllocateEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_allocate ")
                .append(" set ")
                .append(" in_store_no = :inStoreNo, ")
                .append(" out_store_no = :outStoreNo, ")
                .append(" remark = :remark, ")
                .append(" update_time = :updateTime ")
                .append(" where merchant_no = :merchantNo ")
                .append(" AND allocate_no = :allocateNo ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateStatus(long merchantNo, String id, int status, String errDesc) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_allocate ")
                .append(" set ")
                .append(" status = :status, ")
                .append(" err_desc = :errDesc, ")
                .append(" update_time = :updateTime ")
                .append(" where merchant_no = :merchantNo ")
                .append(" AND id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("errDesc", errDesc);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public StockAllocateEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_allocate ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND id = :id ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<StockAllocateEntity> rowMapper = new BeanPropertyRowMapper<>(StockAllocateEntity.class);
        List<StockAllocateEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public StockAllocateEntity findByAllocateNo(long merchantNo, String allocateNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_allocate ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND allocate_no = :allocateNo ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("allocateNo", allocateNo);

        String sql = sb.toString();

        RowMapper<StockAllocateEntity> rowMapper = new BeanPropertyRowMapper<>(StockAllocateEntity.class);
        List<StockAllocateEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public int count(long merchantNo, StockAllocateQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_stock_allocate ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params));

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<StockAllocateEntity> page(long merchantNo, StockAllocateQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_allocate ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params))
                .append(" ORDER BY create_time DESC ");

        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<StockAllocateEntity> rowMapper = new BeanPropertyRowMapper<>(StockAllocateEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String pageWhere(StockAllocateQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        if (query.getStoreNo() != null) {
            sb.append(" AND ( in_store_no = :storeNo OR out_store_no = :storeNo ) ");
            params.addValue("storeNo", query.getStoreNo());
        }
        if (StringUtils.isNotBlank(query.getAllocateNo())) {
            sb.append(" AND allocate_no like :allocateNo ");
            params.addValue("allocateNo", "%" + query.getAllocateNo() + "%");
        }
        if (query.getStatus() != null) {
            sb.append(" AND status = :status ");
            params.addValue("status", query.getStatus());
        }
        return sb.toString();
    }
}
