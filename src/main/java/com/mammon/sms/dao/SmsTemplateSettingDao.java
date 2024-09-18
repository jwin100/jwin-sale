package com.mammon.sms.dao;

import com.mammon.sms.domain.entity.SmsTemplateSettingEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2024/5/9 13:43
 */
@Repository
public class SmsTemplateSettingDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int save(SmsTemplateSettingEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_sms_template_setting (")
                .append(" id, merchant_no, temp_type, temp_id, status ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :tempType, :tempId, :status ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editTempId(SmsTemplateSettingEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_sms_template_setting ")
                .append(" set ")
                .append(" temp_id         = :tempId ")
                .append(" where merchant_no = :merchantNo ")
                .append(" and temp_type            = :tempType ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(SmsTemplateSettingEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_sms_template_setting ")
                .append(" set ")
                .append(" status          = :status ")
                .append(" where merchant_no = :merchantNo ")
                .append(" and temp_type            = :tempType ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteAll(long merchantNo) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        sb.append("DELETE FROM m_sms_template_setting ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);
        String sql = sb.toString();
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public SmsTemplateSettingEntity findByTempType(long merchantNo, int tempType) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_template_setting ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND temp_type = :tempType ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("tempType", tempType);

        String sql = sb.toString();

        RowMapper<SmsTemplateSettingEntity> rowMapper = new BeanPropertyRowMapper<>(SmsTemplateSettingEntity.class);
        List<SmsTemplateSettingEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<SmsTemplateSettingEntity> findAll(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_template_setting ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" ORDER BY temp_type ");

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SmsTemplateSettingEntity> rowMapper = new BeanPropertyRowMapper<>(SmsTemplateSettingEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
