package com.mammon.sms.dao;

import com.mammon.sms.domain.entity.SmsSignEntity;
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
public class SmsSignDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SmsSignEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_sms_sign (")
                .append(" id, merchant_no, sign_name, default_status, status, error_desc, operation_id, operation_time, ")
                .append(" create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :signName, :defaultStatus, :status, :errorDesc, :operationId, :operationTime, ")
                .append(" :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(long merchantNo, String id, String signName, int status) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_sms_sign ")
                .append(" set ")
                .append(" sign_name         = :signName, ")
                .append(" status            = :status, ")
                .append(" update_time       = :updateTime ")
                .append(" where merchant_no = :merchantNo ")
                .append(" and id            = :id ");

        params.addValue("signName", signName);
        params.addValue("status", status);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editDefaultStatus(long merchantNo, String id, int defaultStatus) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_sms_sign ")
                .append(" set ")
                .append(" default_status    = :defaultStatus, ")
                .append(" update_time       = :updateTime ")
                .append(" where merchant_no = :merchantNo ");

        params.addValue("defaultStatus", defaultStatus);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(id)) {
            sb.append(" AND id = :id ");
            params.addValue("id", id);
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<SmsSignEntity> findAllByMerchantNo(long merchantNo, Integer defaultStatus) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_sign ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);

        if (defaultStatus != null) {
            sb.append(" AND  default_status = :defaultStatus ");
            params.addValue("defaultStatus", defaultStatus);
        }

        String sql = sb.toString();

        RowMapper<SmsSignEntity> rowMapper = new BeanPropertyRowMapper<>(SmsSignEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public SmsSignEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_sign ")
                .append(" WHERE merchant_no = :merchantNo  AND id = :id ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<SmsSignEntity> rowMapper = new BeanPropertyRowMapper<>(SmsSignEntity.class);
        List<SmsSignEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
