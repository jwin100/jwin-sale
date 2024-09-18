package com.mammon.cashier.dao;

import com.mammon.cashier.domain.entity.CashierOrderPayEntity;
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
public class CashierOrderPayDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CashierOrderPayEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_cashier_order_pay (")
                .append(" id, order_id, pay_code, payable_amount, reality_amount, auth_code, trade_no, ")
                .append(" trade_time, counted_id, counted_total, status, message, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :orderId, :payCode, :payableAmount, :realityAmount, :authCode, :tradeNo, ")
                .append(" :tradeTime, :countedId, :countedTotal, :status, :message, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updatePayStatus(String id, String tradeNo, int status, String message, long realityAmount) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_order_pay ")
                .append(" set ")
                .append(" trade_no = :tradeNo, ")
                .append(" trade_time = :tradeTime, ")
                .append(" reality_amount = :realityAmount, ")
                .append(" status = :status, ")
                .append(" message = :message ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tradeNo", tradeNo);
        params.addValue("tradeTime", LocalDateTime.now());
        params.addValue("realityAmount", realityAmount);
        params.addValue("status", status);
        params.addValue("message", message);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<CashierOrderPayEntity> findAllByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_order_pay ")
                .append(" WHERE id in ( :ids ) ");

        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<CashierOrderPayEntity> rowMapper = new BeanPropertyRowMapper<>(CashierOrderPayEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<CashierOrderPayEntity> findAllByOrderIds(List<String> orderIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_order_pay ")
                .append(" WHERE order_id in ( :orderIds ) ");

        params.addValue("orderIds", orderIds);

        String sql = sb.toString();

        RowMapper<CashierOrderPayEntity> rowMapper = new BeanPropertyRowMapper<>(CashierOrderPayEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<CashierOrderPayEntity> findAllByOrderId(String orderId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_order_pay ")
                .append(" WHERE order_id = :orderId ");

        params.addValue("orderId", orderId);

        String sql = sb.toString();

        RowMapper<CashierOrderPayEntity> rowMapper = new BeanPropertyRowMapper<>(CashierOrderPayEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
