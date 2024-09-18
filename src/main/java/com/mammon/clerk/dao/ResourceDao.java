package com.mammon.clerk.dao;

import com.mammon.enums.CommonStatus;
import com.mammon.clerk.domain.query.ResourcePageQuery;
import com.mammon.clerk.domain.entity.ResourceEntity;
import org.apache.commons.lang3.StringUtils;
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
public class ResourceDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(ResourceEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_clerk_resource (")
                .append(" id, name, uri, status, pid, icon, sort, create_time, update_time, resource_type, api_path, permissions ")
                .append(" ) VALUES ( ")
                .append(" :id, :name, :uri, :status, :pid, :icon, :sort, :createTime, :updateTime, :resourceType, :apiPath, :permissions ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(ResourceEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE m_clerk_resource ")
                .append(" set ")
                .append(" name           = :name, ")
                .append(" uri            = :uri, ")
                .append(" status         = :status, ")
                .append(" pid            = :pid, ")
                .append(" icon           = :icon, ")
                .append(" resource_type  = :resourceType, ")
                .append(" permissions    = :permissions, ")
                .append(" api_path       = :apiPath, ")
                .append(" sort           = :sort, ")
                .append(" update_time    = :updateTime ")
                .append(" WHERE id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(String id) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("DELETE FROM m_clerk_resource ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<ResourceEntity> findAllByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_resource ")
                .append(" WHERE id IN ( :ids ) ");

        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<ResourceEntity> rowMapper = new BeanPropertyRowMapper<>(ResourceEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public ResourceEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_resource ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<ResourceEntity> rowMapper = new BeanPropertyRowMapper<>(ResourceEntity.class);
        List<ResourceEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (store.iterator().hasNext()) {
            return store.get(0);
        }
        return null;
    }

    public int countByPid(String pid) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM m_clerk_resource ")
                .append(" WHERE status = :status ");

        params.addValue("status", CommonStatus.ENABLED.getCode());

        if (StringUtils.isBlank(pid)) {
            sb.append(" AND pid IS NULL ");
        } else {
            sb.append(" AND pid = :pid ");
            params.addValue("pid", pid);
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<ResourceEntity> findAll() {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_resource ")
                .append(" WHERE status = :status ");
        params.addValue("status", CommonStatus.ENABLED.getCode());

        String sql = sb.toString();

        RowMapper<ResourceEntity> rowMapper = new BeanPropertyRowMapper<>(ResourceEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<ResourceEntity> findPage(String pid, ResourcePageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_resource ");

        if (StringUtils.isBlank(pid)) {
            sb.append(" WHERE pid is null ");
            sb.append(" LIMIT :limit OFFSET :offset ");
            params.addValue("limit", query.getPageSize());
            params.addValue("offset", query.getOffset());
        } else {
            sb.append(" WHERE pid = :pid ");
            params.addValue("pid", pid);
        }

        String sql = sb.toString();

        RowMapper<ResourceEntity> rowMapper = new BeanPropertyRowMapper<>(ResourceEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }


    public List<ResourceEntity> findAllByPid(String pid) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_resource ")
                .append(" WHERE status = :status ");

        params.addValue("status", CommonStatus.ENABLED.getCode());

        if (StringUtils.isBlank(pid)) {
            sb.append(" AND pid IS NULL ");
        } else {
            sb.append(" AND pid = :pid ");
            params.addValue("pid", pid);
        }

        String sql = sb.toString();

        RowMapper<ResourceEntity> rowMapper = new BeanPropertyRowMapper<>(ResourceEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
