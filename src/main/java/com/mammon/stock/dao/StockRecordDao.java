package com.mammon.stock.dao;

import com.mammon.stock.domain.query.StockRecordQuery;
import com.mammon.stock.domain.entity.StockRecordEntity;
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
public class StockRecordDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockRecordEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_record (")
                .append(" id, merchant_no, store_no, record_no, operation_time, type, reason_id, status, operation_id, ")
                .append(" create_time, update_time, join_no, io_type, reason_record ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :recordNo, :operationTime, :type, :reasonId, :status, :operationId, ")
                .append(" :createTime, :updateTime, :joinNo, :ioType, (:reasonRecord)::jsonb ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public StockRecordEntity findByRecordNo(long merchantNo, long storeNo, String accountId, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_record ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND id = :id ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<StockRecordEntity> rowMapper = new BeanPropertyRowMapper<>(StockRecordEntity.class);
        List<StockRecordEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int count(long merchantNo, StockRecordQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_stock_record ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params));

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<StockRecordEntity> page(long merchantNo, StockRecordQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_record ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params));

        sb.append(" ORDER BY create_time DESC ");
        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<StockRecordEntity> rowMapper = new BeanPropertyRowMapper<>(StockRecordEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String pageWhere(StockRecordQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        if (query.getStoreNo() != null) {
            sb.append(" AND store_no = :storeNo ");
            params.addValue("storeNo", query.getStoreNo());
        }
        if (query.getIoType() != null) {
            sb.append(" AND io_type = :ioType ");
            params.addValue("ioType", query.getIoType());
        }
        return sb.toString();
    }
}
