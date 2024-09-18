package com.mammon.sms.dao;

import com.mammon.sms.domain.entity.SmsSignChannelRelEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class SmsSignChannelRelDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SmsSignChannelRelEntity findBySignIdAndChannelId(String signId, String channelId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_sign_channel_rel ")
                .append(" WHERE sign_id = :signId AND channel_id = :channelId ");

        params.addValue("signId", signId);
        params.addValue("channelId", channelId);

        String sql = sb.toString();

        RowMapper<SmsSignChannelRelEntity> rowMapper = new BeanPropertyRowMapper<>(SmsSignChannelRelEntity.class);
        List<SmsSignChannelRelEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
