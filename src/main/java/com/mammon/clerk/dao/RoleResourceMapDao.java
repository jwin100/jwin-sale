package com.mammon.clerk.dao;

import com.mammon.clerk.domain.entity.RoleResourceMapEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class RoleResourceMapDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(RoleResourceMapEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_clerk_role_resource_map (")
                .append(" id, role_id, resource_id, resource_index, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :roleId, :resourceId, :resourceIndex, :createTime ")
                .append(" ) ");

        String sql = sb.toString();

        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteByRoleId(String roleId) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("delete from m_clerk_role_resource_map ")
                .append(" where role_id = :roleId ");
        params.addValue("roleId", roleId);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<RoleResourceMapEntity> findByRoleId(String roleId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_role_resource_map ")
                .append(" WHERE role_id = :roleId ");

        params.addValue("roleId", roleId);

        String sql = sb.toString();

        RowMapper<RoleResourceMapEntity> rowMapper = new BeanPropertyRowMapper<>(RoleResourceMapEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
