package com.mammon.trade.dao;

import com.mammon.trade.model.entity.TradeEntity;
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
 * @since 2024/3/5 14:20
 */
@Repository
public class TradeDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(TradeEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_trade (")
                .append(" id, merchant_no, store_no, trade_no, channel_id, pay_mode, pay_way, order_no, order_subject, ")
                .append(" order_amount, channel_trade_no, bank_order_no, bank_trade_no, member_id, auth_code, success_time, ")
                .append(" buyer_pay_amount, status, describe, refund, refund_amount, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :tradeNo, :channelId, :payMode, :payWay, :orderNo, :orderSubject, ")
                .append(" :orderAmount, :channelTradeNo, :bankOrderNo, :bankTradeNo, :memberId, :authCode, :successTime, ")
                .append(" :buyerPayAmount, :status, :describe, :refund, :refundAmount, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(TradeEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_trade ")
                .append(" set ")
                .append(" channel_trade_no          = :channelTradeNo, ")
                .append(" bank_order_no             = :bankOrderNo, ")
                .append(" bank_trade_no             = :bankTradeNo, ")
                .append(" status                    = :status, ")
                .append(" describe                  = :describe ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    /**
     * 交易状态修改
     *
     * @param id
     * @param beforeStatus
     * @param afterStatus
     * @param describe
     * @return
     */
    public int editStatus(String id, int beforeStatus, int afterStatus, String describe) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE m_trade")
                .append(" SET ")
                .append(" status = :afterStatus, ")
                .append(" describe = :describe ")
                .append(" WHERE id = :id AND status = :beforeStatus");
        params.addValue("id", id);
        params.addValue("beforeStatus", beforeStatus);
        params.addValue("afterStatus", afterStatus);
        params.addValue("describe", describe);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    /**
     * @param id
     * @param buyerPayAmount 买家实际付款金额
     * @param successTime    交易成功时间
     * @return
     */
    public int editSuccess(String id, long buyerPayAmount, LocalDateTime successTime) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE m_trade")
                .append(" SET ")
                .append(" buyer_pay_amount  = :buyerPayAmount, ")
                .append(" success_time      = :successTime ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);
        params.addValue("buyerPayAmount", buyerPayAmount);
        params.addValue("successTime", successTime);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    /**
     * 退款信息修改
     *
     * @param id
     * @param refund       是否有退款
     * @param refundAmount 成功退款金额
     * @return
     */
    public int editRefund(String id, int refund, long refundAmount) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE m_trade")
                .append(" SET ")
                .append(" refund            = :refund, ")
                .append(" refund_amount     = :refundAmount ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);
        params.addValue("refund", refund);
        params.addValue("refundAmount", refundAmount);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public TradeEntity findByOrderNo(String orderNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM m_trade ")
                .append(" WHERE order_no = :orderNo ");
        params.addValue("orderNo", orderNo);

        String sql = sb.toString();

        RowMapper<TradeEntity> rowMapper = new BeanPropertyRowMapper<>(TradeEntity.class);
        List<TradeEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public TradeEntity findByTradeNo(String tradeNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM m_trade ")
                .append(" WHERE trade_no = :tradeNo ");
        params.addValue("tradeNo", tradeNo);

        String sql = sb.toString();

        RowMapper<TradeEntity> rowMapper = new BeanPropertyRowMapper<>(TradeEntity.class);
        List<TradeEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
