package com.mammon.stock.dao;

import com.mammon.stock.domain.query.StockRecordReasonQuery;
import com.mammon.stock.domain.entity.StockRecordReasonEntity;
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
public class StockRecordReasonDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockRecordReasonEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_record_reason (")
                .append(" id, merchant_no, reason_name, io_type, remark, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :reasonName, :ioType, :remark, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(long merchantNo, String reasonId, String reasonName, int ioType, String remark) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_record_reason ")
                .append(" set ")
                .append(" reason_name = :reasonName, ")
                .append(" io_type = :ioType, ")
                .append(" remark = :remark, ")
                .append(" update_time = :updateTime ")
                .append(" where merchant_no = :merchantNo ")
                .append(" AND id = :reasonId ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("reasonName", reasonName);
        params.addValue("ioType", ioType);
        params.addValue("remark", remark);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("merchantNo", merchantNo);
        params.addValue("reasonId", reasonId);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(long merchantNo, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from m_stock_record_reason ")
                .append(" where merchant_no = :merchantNo ")
                .append(" AND id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public StockRecordReasonEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_record_reason ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND id = :id ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<StockRecordReasonEntity> rowMapper = new BeanPropertyRowMapper<>(StockRecordReasonEntity.class);
        List<StockRecordReasonEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<StockRecordReasonEntity> findAll(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_record_reason ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" ORDER BY create_time DESC");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<StockRecordReasonEntity> rowMapper = new BeanPropertyRowMapper<>(StockRecordReasonEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int count(long merchantNo, long storeNo, String accountId, StockRecordReasonQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_stock_record_reason ")
                .append(" WHERE merchant_no = :merchantNo ");

        if (StringUtils.isNotBlank(query.getReasonName())) {
            sb.append(" AND reason_name like :reasonName ");
            params.addValue("reasonName", "%" + query.getReasonName() + "%");
        }

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<StockRecordReasonEntity> page(long merchantNo, long storeNo, String accountId, StockRecordReasonQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_record_reason ")
                .append(" WHERE merchant_no = :merchantNo ");
        if (StringUtils.isNotBlank(query.getReasonName())) {
            sb.append(" AND reason_name like :reasonName ");
            params.addValue("reasonName", "%" + query.getReasonName() + "%");
        }
        sb.append(" ORDER BY create_time DESC ");
        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<StockRecordReasonEntity> rowMapper = new BeanPropertyRowMapper<>(StockRecordReasonEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
