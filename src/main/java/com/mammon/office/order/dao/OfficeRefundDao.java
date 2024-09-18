package com.mammon.office.order.dao;

import com.mammon.office.order.domain.entity.OfficeRefundEntity;
import com.mammon.office.order.domain.query.OfficeOrderRefundQuery;
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

/**
 * @author dcl
 * @date 2023-03-06 14:15:34
 */
@Repository
public class OfficeRefundDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(OfficeRefundEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_office_refund (")
                .append(" id, merchant_no, store_no, account_id, order_id, refund_no, refund_amount, ")
                .append(" refund_type, status, refund_message, refund_time, config_id, trade_no, remark, ")
                .append(" create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :accountId, :orderId, :refundNo, :refundAmount, ")
                .append(" :refundType, :status, :refundMessage, :refundTime, :configId, :tradeNo, :remark, ")
                .append(" :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatusById(String id, int status, String message, LocalDateTime refundTime, String tradeNo) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_office_refund ")
                .append(" set ")
                .append(" status = :status, ")
                .append(" refund_message = :message, ")
                .append(" refund_time = :refundTime, ")
                .append(" trade_no = :tradeNo ")
                .append(" where id = :id ");

        String sql = sb.toString();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("message", message);
        params.addValue("refundTime", refundTime);
        params.addValue("tradeNo", tradeNo);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int countPage(long merchantNo, OfficeOrderRefundQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_office_refund ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(query.getRefundNo())) {
            sb.append(" AND  refund_no = :refundNo ");
            params.addValue("refundNo", query.getRefundNo());
        }

        if (query.getStatus() != null) {
            sb.append(" AND  status = :status ");
            params.addValue("status", query.getStatus());
        }

        if (query.getStartCreateTime() != null && query.getEndCreateTime() != null) {
            sb.append(" AND create_time >= :startCreateTime AND create_time < :endCreateTime ");
            params.addValue("startCreateTime", query.getStartCreateTime());
            params.addValue("endCreateTime", query.getEndCreateTime().plusDays(1));
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<OfficeRefundEntity> findPage(long merchantNo, OfficeOrderRefundQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_refund ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(query.getRefundNo())) {
            sb.append(" AND  refund_no = :refundNo ");
            params.addValue("refundNo", query.getRefundNo());
        }

        if (query.getStatus() != null) {
            sb.append(" AND  status = :status ");
            params.addValue("status", query.getStatus());
        }

        if (query.getStartCreateTime() != null && query.getEndCreateTime() != null) {
            sb.append(" AND create_time >= :startCreateTime AND create_time < :endCreateTime ");
            params.addValue("startCreateTime", query.getStartCreateTime());
            params.addValue("endCreateTime", query.getEndCreateTime().plusDays(1));
        }

        sb
                .append(" ORDER BY create_time DESC ")
                .append(" limit :limit offset :offset ");
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<OfficeRefundEntity> rowMapper = new BeanPropertyRowMapper<>(OfficeRefundEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public OfficeRefundEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_refund ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<OfficeRefundEntity> rowMapper = new BeanPropertyRowMapper<>(OfficeRefundEntity.class);
        List<OfficeRefundEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public OfficeRefundEntity findByRefundNo(String refundNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_refund ")
                .append(" WHERE refund_no = :refundNo ");

        params.addValue("refundNo", refundNo);

        String sql = sb.toString();

        RowMapper<OfficeRefundEntity> rowMapper = new BeanPropertyRowMapper<>(OfficeRefundEntity.class);
        List<OfficeRefundEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
