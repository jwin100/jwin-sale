package com.mammon.cashier.dao;

import com.mammon.cashier.domain.entity.CashierRefundPayEntity;
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
public class CashierRefundPayDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CashierRefundPayEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_cashier_refund_pay (")
                .append(" id, refund_id, pay_code, payable_amount, reality_amount, counted_id, counted_total, trade_no, ")
                .append(" trade_time, status, message, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :refundId, :payCode, :payableAmount, :realityAmount, :countedId, :countedTotal, :tradeNo, ")
                .append(" :tradeTime, :status, :message, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updatePayStatus(String id, String tradeNo, int status, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_refund_pay ")
                .append(" set ")
                .append(" trade_no = :tradeNo, ")
                .append(" trade_time = :tradeTime, ")
                .append(" status = :status, ")
                .append(" message = :message ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tradeNo", tradeNo);
        params.addValue("tradeTime", LocalDateTime.now());
        params.addValue("status", status);
        params.addValue("message", message);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<CashierRefundPayEntity> findAllByRefundIds(List<String> refundIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_refund_pay ")
                .append(" WHERE refund_id in ( :refundIds ) ");

        params.addValue("refundIds", refundIds);

        String sql = sb.toString();

        RowMapper<CashierRefundPayEntity> rowMapper = new BeanPropertyRowMapper<>(CashierRefundPayEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<CashierRefundPayEntity> findAllByRefundId(String refundId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_refund_pay ")
                .append(" WHERE refund_id = :refundId ");

        params.addValue("refundId", refundId);

        String sql = sb.toString();

        RowMapper<CashierRefundPayEntity> rowMapper = new BeanPropertyRowMapper<>(CashierRefundPayEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
