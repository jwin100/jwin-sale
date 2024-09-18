package com.mammon.trade.dao;

import com.mammon.trade.model.entity.TradeEntity;
import com.mammon.trade.model.entity.TradeRefundEntity;
import com.mammon.trade.model.vo.TradeRefundVo;
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
 * @since 2024/3/7 16:49
 */
@Repository
public class TradeRefundDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(TradeRefundEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_trade_refund (")
                .append(" id, merchant_no, store_no, trade_no, refund_trade_no, refund_subject, refund_amount, ")
                .append(" channel_id, channel_trade_no, bank_order_no, bank_trade_no, pay_way, status, ")
                .append(" refund_success_time, describe, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :tradeNo, :refundTradeNo, :refundSubject, :refundAmount, ")
                .append(" :channelId, :channelTradeNo, :bankOrderNo, :bankTradeNo, :payWay, :status, ")
                .append(" :refundSuccessTime, :describe, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(String id, int status, String describe) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE m_trade_refund")
                .append(" SET ")
                .append(" status = :status, ")
                .append(" describe = :describe ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);
        params.addValue("status", status);
        params.addValue("describe", describe);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editSuccess(String id, LocalDateTime refundSuccessTime) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE m_trade_refund")
                .append(" SET ")
                .append(" refund_success_time = :refundSuccessTime ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);
        params.addValue("refundSuccessTime", refundSuccessTime);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public TradeRefundEntity findByRefundTradeNo(String refundTradeNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM m_trade_refund ")
                .append(" WHERE refund_trade_no = :refundTradeNo ");
        params.addValue("refundTradeNo", refundTradeNo);

        String sql = sb.toString();

        RowMapper<TradeRefundEntity> rowMapper = new BeanPropertyRowMapper<>(TradeRefundEntity.class);
        List<TradeRefundEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
