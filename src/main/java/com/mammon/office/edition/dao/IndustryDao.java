package com.mammon.office.edition.dao;

import com.mammon.office.edition.domain.entity.IndustryEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-02 11:37:57
 */
@Repository
public class IndustryDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<IndustryEntity> findAllByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_industry ")
                .append(" WHERE id in ( :ids ) ");
        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<IndustryEntity> rowMapper = new BeanPropertyRowMapper<>(IndustryEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public IndustryEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_industry ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<IndustryEntity> rowMapper = new BeanPropertyRowMapper<>(IndustryEntity.class);
        List<IndustryEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
