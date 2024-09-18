package com.mammon.sms.dao;

import com.mammon.sms.domain.entity.SmsSendItemEntity;
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
public class SmsSendItemDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int batchSave(List<SmsSendItemEntity> items) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_sms_send_item (")
                .append(" id, send_id, phone, user_id, content, status, error_desc, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :sendId, :phone, :userId, :content, :status, :errorDesc, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();

        SqlParameterSource[] sources = new SqlParameterSource[items.size()];
        for (int i = 0; i < items.size(); i++) {
            sources[i] = new BeanPropertySqlParameterSource(items.get(i));
        }

        return namedParameterJdbcTemplate.batchUpdate(sql, sources).length;
    }

    public int updateStatus(String smsSendId, String phone, int beforeStatus, int afterStatus, String errorDesc) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_sms_send_item ")
                .append(" set ")
                .append(" status        = :afterStatus, ")
                .append(" error_desc    = :errorDesc, ")
                .append(" update_time   = :updateTime ")
                .append(" where send_id = :smsSendId and status = :beforeStatus ");

        if (StringUtils.isNotBlank(phone)) {
            sb.append(" and phone = :phone ");
            params.addValue("phone", phone);
        }

        params.addValue("smsSendId", smsSendId);
        params.addValue("beforeStatus", beforeStatus);
        params.addValue("afterStatus", afterStatus);
        params.addValue("errorDesc", errorDesc);
        params.addValue("updateTime", LocalDateTime.now());

        String sql = sb.toString();
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<SmsSendItemEntity> findAllBySmsSendId(String sendId) {
        //where 判断phone==null
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_send_item ")
                .append(" WHERE send_id = :sendId ");

        params.addValue("sendId", sendId);

        String sql = sb.toString();

        RowMapper<SmsSendItemEntity> rowMapper = new BeanPropertyRowMapper<>(SmsSendItemEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

}
