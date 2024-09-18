package com.mammon.sms.dao;

import com.mammon.sms.domain.dto.SmsTempQuery;
import com.mammon.sms.domain.entity.SmsTemplateEntity;
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
public class SmsTemplateDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SmsTemplateEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_sms_template (")
                .append(" id, merchant_no, temp_name, temp_group, sms_type, temp_type, template, default_status, ")
                .append(" status, error_desc, operation_id, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :tempName, :tempGroup, :smsType, :tempType, :template, :defaultStatus, ")
                .append(" :status, :errorDesc, :operationId, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(long merchantNo, String id, String tempName, String template, int status) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_sms_template ")
                .append(" set ")
                .append(" temp_name         = :tempName, ")
                .append(" template          = :template, ")
                .append(" status            = :status, ")
                .append(" update_time       = :updateTime ")
                .append(" where merchant_no = :merchantNo ")
                .append(" and id            = :id ");

        params.addValue("tempName", tempName);
        params.addValue("template", template);
        params.addValue("status", status);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public SmsTemplateEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_template ")
                .append(" WHERE ( merchant_no = 0 OR merchant_no = :merchantNo ) AND id = :id ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<SmsTemplateEntity> rowMapper = new BeanPropertyRowMapper<>(SmsTemplateEntity.class);
        List<SmsTemplateEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<SmsTemplateEntity> findAllByIds(long merchantNo, List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_template ")
                .append(" WHERE ( merchant_no = 0 OR merchant_no = :merchantNo ) ")
                .append(" AND id in ( :ids ) ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<SmsTemplateEntity> rowMapper = new BeanPropertyRowMapper<>(SmsTemplateEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SmsTemplateEntity> findAllByTempType(long merchantNo, int tempType) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_template ")
                .append(" WHERE ( merchant_no = :merchantNo or merchant_no = 0 ) ")
                .append(" AND temp_type = :tempType ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("tempType", tempType);

        String sql = sb.toString();

        RowMapper<SmsTemplateEntity> rowMapper = new BeanPropertyRowMapper<>(SmsTemplateEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SmsTemplateEntity> findAllBySmsType(long merchantNo, int smsType) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_template ")
                .append(" WHERE ( merchant_no = :merchantNo or merchant_no = 0 ) ")
                .append(" AND sms_type = :smsType ")
                .append(" ORDER BY temp_type ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("smsType", smsType);

        String sql = sb.toString();

        RowMapper<SmsTemplateEntity> rowMapper = new BeanPropertyRowMapper<>(SmsTemplateEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SmsTemplateEntity> findAllByTempGroup(long merchantNo, int tempGroup, int smsType) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_template ")
                .append(" WHERE ( merchant_no = :merchantNo or merchant_no = 0 ) ")
                .append(" AND temp_group = :tempGroup ")
                .append(" AND sms_type = :smsType ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("tempGroup", tempGroup);
        params.addValue("sms_type", smsType);

        String sql = sb.toString();

        RowMapper<SmsTemplateEntity> rowMapper = new BeanPropertyRowMapper<>(SmsTemplateEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SmsTemplateEntity> findAll(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_template ")
                .append(" WHERE ( merchant_no = :merchantNo or merchant_no = 0 ) ");

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SmsTemplateEntity> rowMapper = new BeanPropertyRowMapper<>(SmsTemplateEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int countPage(long merchantNo, Long storeNo, String accountId, SmsTempQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_sms_template ")
                .append(" WHERE (merchant_no = 0 OR merchant_no = :merchantNo ) ");

        if (query.getSmsType() != null) {
            sb.append(" AND sms_type = :smsType ");
            params.addValue("smsType", query.getSmsType());
        }

        if (query.getTempType() != null) {
            sb.append(" AND temp_type = :tempType ");
            params.addValue("tempType", query.getTempType());
        }

        if (query.getStatus() != null) {
            sb.append(" AND status = :status ");
            params.addValue("status", query.getStatus());
        }

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<SmsTemplateEntity> findPage(long merchantNo, Long storeNo, String accountId, SmsTempQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms_template ")
                .append(" WHERE (merchant_no = 0 OR merchant_no = :merchantNo ) ");
        
        if (query.getSmsType() != null) {
            sb.append(" AND sms_type = :smsType ");
            params.addValue("smsType", query.getSmsType());
        }

        if (query.getTempType() != null) {
            sb.append(" AND temp_type = :tempType ");
            params.addValue("tempType", query.getTempType());
        }

        if (query.getStatus() != null) {
            sb.append(" AND status = :status ");
            params.addValue("status", query.getStatus());
        }

        sb.append(" ORDER BY create_time DESC ");
        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<SmsTemplateEntity> rowMapper = new BeanPropertyRowMapper<>(SmsTemplateEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
