package com.mammon.print.dao;

import com.mammon.print.domain.entity.PrintTemplateEntity;
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
public class PrintTemplateDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(PrintTemplateEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_print_template (")
                .append(" id, merchant_no, type, classify, ")
                .append(" template, status, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :type, :classify, ")
                .append(" (:template)::jsonb, :status, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(PrintTemplateEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_print_template ")
                .append(" set ")
                .append(" template      = (:template)::jsonb, ")
                .append(" update_time   = :updateTime ")
                .append(" where id      = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public PrintTemplateEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_print_template ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<PrintTemplateEntity> rowMapper = new BeanPropertyRowMapper<>(PrintTemplateEntity.class);
        List<PrintTemplateEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    /**
     * 根据类型获取商户下对应模板
     *
     * @param merchantNo
     * @param storeNo
     * @param type
     * @return
     */
    public PrintTemplateEntity findByType(long merchantNo, int type) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_print_template ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND type = :type");

        params.addValue("merchantNo", merchantNo);
        params.addValue("type", type);

        String sql = sb.toString();

        RowMapper<PrintTemplateEntity> rowMapper = new BeanPropertyRowMapper<>(PrintTemplateEntity.class);
        List<PrintTemplateEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
