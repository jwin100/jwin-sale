package com.mammon.office.edition.dao;

import com.mammon.office.edition.domain.entity.PackageSpecEntity;
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
public class PackageSpecDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<PackageSpecEntity> findAllBySpuId(String spuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_package_spec ")
                .append(" WHERE spu_id = :spuId ");
        params.addValue("spuId", spuId);

        String sql = sb.toString();

        RowMapper<PackageSpecEntity> rowMapper = new BeanPropertyRowMapper<>(PackageSpecEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
