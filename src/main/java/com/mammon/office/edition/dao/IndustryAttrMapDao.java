package com.mammon.office.edition.dao;

import com.mammon.office.edition.domain.entity.IndustryAttrMapEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-02 11:40:47
 */
@Repository
public class IndustryAttrMapDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<IndustryAttrMapEntity> findAllByIndustryId(String industryId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_industry_attr_map ")
                .append(" WHERE industry_id = :industryId ");
        params.addValue("industryId", industryId);

        String sql = sb.toString();

        RowMapper<IndustryAttrMapEntity> rowMapper = new BeanPropertyRowMapper<>(IndustryAttrMapEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
