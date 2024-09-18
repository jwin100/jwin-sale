package com.mammon.clerk.dao;

import com.mammon.clerk.domain.entity.RoleEntity;
import com.mammon.clerk.domain.query.RolePageQuery;
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
public class RoleDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(RoleEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_clerk_role (")
                .append(" id, name, en_name, remark, merchant_no, default_status, status, create_time, update_time, type ")
                .append(" ) VALUES ( ")
                .append(" :id, :name, :enName, :remark, :merchantNo, :defaultStatus, :status, :createTime, :updateTime, :type ")
                .append(" ) ");

        String sql = sb.toString();

        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(RoleEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_role ")
                .append(" set ")
                .append(" name           = :name, ")
                .append(" en_name        = :enName, ")
                .append(" remark         = :remark, ")
                .append(" type           = :type, ")
                .append(" update_time    = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int batchEdit(long merchantNo, int defaultStatusBefore, int defaultStatus) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_role ")
                .append(" set ")
                .append(" default_status = :defaultStatus ")
                .append(" where merchant_no = :merchantNo and default_status = :defaultStatusBefore ");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("defaultStatus", defaultStatus);
        params.addValue("merchantNo", merchantNo);
        params.addValue("defaultStatusBefore", defaultStatusBefore);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editDefaultStatus(long merchantNo, String id, int defaultStatus) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_role ")
                .append(" set ")
                .append(" default_status = :defaultStatus ")
                .append(" where merchant_no = :merchantNo and id = :id ");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("defaultStatus", defaultStatus);
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(long merchantNo, String id, int status) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_role ")
                .append(" set ")
                .append(" status = :status ")
                .append(" where merchant_no = :merchantNo and id = :id ");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public RoleEntity findDefaultRole(long merchantNo, int defaultStatus) {

        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_role ")
                .append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) ")
                .append(" AND default_status = :defaultStatus ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("defaultStatus", defaultStatus);

        String sql = sb.toString();

        RowMapper<RoleEntity> rowMapper = new BeanPropertyRowMapper<>(RoleEntity.class);
        List<RoleEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int delete(String id) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("delete from m_clerk_role ")
                .append(" where id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public RoleEntity findById(String id, long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_role ")
                .append(" WHERE id = :id ")
                .append(" AND ( merchant_no = 0 OR merchant_no = :merchantNo ) ");

        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<RoleEntity> rowMapper = new BeanPropertyRowMapper<>(RoleEntity.class);
        List<RoleEntity> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);

        if (result.isEmpty())
            return null;

        return result.get(0);
    }

    public int countPage(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_clerk_role ")
                .append(" WHERE ( merchant_no = 0 OR merchant_no = :merchantNo ) ");

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<RoleEntity> findPage(long merchantNo, RolePageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_role ")
                .append(" WHERE ( merchant_no = 0 OR merchant_no = :merchantNo ) ");

        params.addValue("merchantNo", merchantNo);

        sb.append(" ORDER BY create_time ASC ");
        sb.append(" limit :limit offset :offset ");
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<RoleEntity> rowMapper = new BeanPropertyRowMapper<>(RoleEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<RoleEntity> findAll(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_role ")
                .append(" WHERE ( merchant_no = 0 OR merchant_no = :merchantNo ) ");

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<RoleEntity> rowMapper = new BeanPropertyRowMapper<>(RoleEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
