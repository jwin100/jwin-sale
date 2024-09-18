package com.mammon.sms.dao;

import com.mammon.sms.domain.entity.SmsChannelEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class SmsChannelDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SmsChannelEntity findBySmsType(int smsType) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_channel ")
                .append(" WHERE sms_type = :smsType AND default_status = 1 ");

        params.addValue("smsType", smsType);

        String sql = sb.toString();

        RowMapper<SmsChannelEntity> rowMapper = new BeanPropertyRowMapper<>(SmsChannelEntity.class);
        List<SmsChannelEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (store.iterator().hasNext()) {
            return store.get(0);
        }
        return null;
    }

    public SmsChannelEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_channel ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<SmsChannelEntity> rowMapper = new BeanPropertyRowMapper<>(SmsChannelEntity.class);
        List<SmsChannelEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (store.iterator().hasNext()) {
            return store.get(0);
        }
        return null;
    }

}
