package com.mammon.sms.dao;

import com.mammon.sms.domain.entity.SmsTemplateChannelRelEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class SmsTemplateChannelRelDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SmsTemplateChannelRelEntity findByTempIdAndChannelId(String tempId, String channelId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_template_channel_rel ")
                .append(" WHERE temp_id = :tempId AND channel_id = :channelId ");

        params.addValue("tempId", tempId);
        params.addValue("channelId", channelId);

        String sql = sb.toString();

        RowMapper<SmsTemplateChannelRelEntity> rowMapper = new BeanPropertyRowMapper<>(SmsTemplateChannelRelEntity.class);
        List<SmsTemplateChannelRelEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
