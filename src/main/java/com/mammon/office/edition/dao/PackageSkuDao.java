package com.mammon.office.edition.dao;

import com.mammon.office.edition.domain.entity.PackageSkuEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-02 11:41:42
 */
@Repository
public class PackageSkuDao {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PackageSkuEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_package_sku ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<PackageSkuEntity> rowMapper = new BeanPropertyRowMapper<>(PackageSkuEntity.class);
        List<PackageSkuEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public PackageSkuEntity findAllBySpecs(String specs) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_package_sku ")
                .append(" WHERE specs = :specs ");
        params.addValue("specs", specs);

        String sql = sb.toString();

        RowMapper<PackageSkuEntity> rowMapper = new BeanPropertyRowMapper<>(PackageSkuEntity.class);
        List<PackageSkuEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<PackageSkuEntity> findAllByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_package_sku ")
                .append(" WHERE id in ( :ids ) ");
        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<PackageSkuEntity> rowMapper = new BeanPropertyRowMapper<>(PackageSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
