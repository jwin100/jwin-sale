package com.mammon.office.edition.dao;

import com.mammon.office.edition.domain.entity.PackageSpuEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-02 11:41:59
 */
@Repository
public class PackageSpuDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<PackageSpuEntity> findAll(int status) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_package_spu ")
                .append(" WHERE status = :status ");
        params.addValue("status", status);

        String sql = sb.toString();

        RowMapper<PackageSpuEntity> rowMapper = new BeanPropertyRowMapper<>(PackageSpuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public PackageSpuEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_package_spu ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<PackageSpuEntity> rowMapper = new BeanPropertyRowMapper<>(PackageSpuEntity.class);
        List<PackageSpuEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<PackageSpuEntity> findAllByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_package_spu ")
                .append(" WHERE id in ( :ids ) ");
        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<PackageSpuEntity> rowMapper = new BeanPropertyRowMapper<>(PackageSpuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
