package com.mammon.trade.dao;

import com.mammon.enums.CommonStatus;
import com.mammon.trade.model.entity.TradeChannelEntity;
import com.mammon.trade.model.entity.TradeEntity;
import com.mammon.trade.model.entity.TradeRefundEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/3/5 14:22
 */
@Repository
public class TradeChannelDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TradeChannelEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM m_trade_channel ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<TradeChannelEntity> rowMapper = new BeanPropertyRowMapper<>(TradeChannelEntity.class);
        List<TradeChannelEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public TradeChannelEntity findByStoreNo(long merchantNo, long storeNo) {
        // 商户号，门店号，且状态为启用
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM m_trade_channel ")
                .append(" WHERE merchant_no = :merchantNo AND store_no =:storeNo and status = :status ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("status", CommonStatus.ENABLED.getCode());

        String sql = sb.toString();

        RowMapper<TradeChannelEntity> rowMapper = new BeanPropertyRowMapper<>(TradeChannelEntity.class);
        List<TradeChannelEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
