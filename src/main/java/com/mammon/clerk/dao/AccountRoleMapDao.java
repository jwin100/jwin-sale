package com.mammon.clerk.dao;

import com.mammon.clerk.domain.entity.AccountRoleMapEntity;
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
public class AccountRoleMapDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(AccountRoleMapEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_clerk_account_role_map (")
                .append(" id, account_id, role_id, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :accountId, :roleId, :createTime ")
                .append(" ) ");

        String sql = sb.toString();

        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteByAccountId(String accountId) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM m_clerk_account_role_map ")
                .append(" WHERE account_id = :accountId ");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("accountId", accountId);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public AccountRoleMapEntity findByUserId(String accountId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_account_role_map ")
                .append(" WHERE account_id = :accountId ");

        params.addValue("accountId", accountId);

        String sql = sb.toString();

        RowMapper<AccountRoleMapEntity> rowMapper = new BeanPropertyRowMapper<>(AccountRoleMapEntity.class);
        List<AccountRoleMapEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<AccountRoleMapEntity> findAllByRoleId(String roleId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_account_role_map ")
                .append(" WHERE role_id = :roleId ");

        params.addValue("roleId", roleId);

        String sql = sb.toString();

        RowMapper<AccountRoleMapEntity> rowMapper = new BeanPropertyRowMapper<>(AccountRoleMapEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
