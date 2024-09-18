package com.mammon.sms.dao;

import com.mammon.common.PageQuery;
import com.mammon.sms.domain.dto.SmsQuery;
import com.mammon.sms.domain.entity.SmsSendEntity;
import org.springframework.data.domain.PageRequest;
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
public class SmsSendDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SmsSendEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_sms_send (")
                .append(" id, merchant_no, store_no, sign_id, temp_id, temp_group, sms_type, temp_type, send_message, message_params, ")
                .append(" message_length, message_cnt, message_channel_cnt, free, consume_cnt, return_cnt, status, ")
                .append(" error_desc, send_time, sms_channel_id, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :signId, :tempId, :tempGroup, :smsType, :tempType, :sendMessage, (:messageParams)::jsonb, ")
                .append(" :messageLength, :messageCnt, :messageChannelCnt, :free, :consumeCnt, :returnCnt, :status, ")
                .append(" :errorDesc, :sendTime, :smsChannelId, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateStatus(String id, int beforeStatus, int afterStatus, String errorDesc) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_sms_send ")
                .append(" set ")
                .append(" status         = :afterStatus, ")
                .append(" error_desc     = :errorDesc, ")
                .append(" update_time    = :updateTime ")
                .append(" where id = :id and status = :beforeStatus ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("afterStatus", afterStatus);
        params.addValue("errorDesc", errorDesc);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("id", id);
        params.addValue("beforeStatus", beforeStatus);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public SmsSendEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_send ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<SmsSendEntity> rowMapper = new BeanPropertyRowMapper<>(SmsSendEntity.class);
        List<SmsSendEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (store.iterator().hasNext()) {
            return store.get(0);
        }
        return null;
    }

    public List<SmsSendEntity> findAllByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_send ")
                .append(" WHERE id in ( :ids ) ");

        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<SmsSendEntity> rowMapper = new BeanPropertyRowMapper<>(SmsSendEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int countPage(long merchantNo, Long storeNo, String accountId, SmsQuery dto) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_sms_send ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<SmsSendEntity> findPage(long merchantNo, Long storeNo, String accountId, SmsQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_send ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" ORDER BY send_time DESC ");

        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<SmsSendEntity> rowMapper = new BeanPropertyRowMapper<>(SmsSendEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
