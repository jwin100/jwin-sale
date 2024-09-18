package com.mammon.office.edition.dao;

import com.mammon.office.edition.domain.entity.IndustryAttrEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-02 11:40:38
 */
@Repository
public class IndustryAttrDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<IndustryAttrEntity> findAll() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_industry_attr ");

        String sql = sb.toString();

        RowMapper<IndustryAttrEntity> rowMapper = new BeanPropertyRowMapper<>(IndustryAttrEntity.class);
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }

    public List<IndustryAttrEntity> findAllByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_industry_attr ")
                .append(" WHERE id in ( :ids ) ");
        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<IndustryAttrEntity> rowMapper = new BeanPropertyRowMapper<>(IndustryAttrEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
