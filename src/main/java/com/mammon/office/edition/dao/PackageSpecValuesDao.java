package com.mammon.office.edition.dao;

import com.mammon.office.edition.domain.entity.PackageSpecValuesEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2023/8/28 10:43
 */
@Repository
public class PackageSpecValuesDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<PackageSpecValuesEntity> findAllBySpecIds(List<String> specIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_package_spec_values ")
                .append(" WHERE spec_id in ( :specIds ) ");
        params.addValue("specIds", specIds);

        String sql = sb.toString();

        RowMapper<PackageSpecValuesEntity> rowMapper = new BeanPropertyRowMapper<>(PackageSpecValuesEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
