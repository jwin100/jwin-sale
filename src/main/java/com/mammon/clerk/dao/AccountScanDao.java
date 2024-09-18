package com.mammon.clerk.dao;

import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.domain.entity.AccountScanEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/5/23 11:31
 */
@Repository
public class AccountScanDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(AccountScanEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_clerk_account_scan (")
                .append(" id, source, status, expire_time, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :source, :status, :expireTime, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(String id, int status) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_account_scan ")
                .append(" set ")
                .append(" status        = :status ")
                .append(" where id      = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editOpenId(String id, int status, String openId) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_account_scan ")
                .append(" set ")
                .append(" status        = :status, ")
                .append(" open_id       = :openId ")
                .append(" where id      = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("openId", openId);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editLoginInfo(String id, int status, String loginInfo) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_account_scan ")
                .append(" set ")
                .append(" status        = :status, ")
                .append(" login_info    = (:loginInfo)::jsonb ")
                .append(" where id      = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("loginInfo", loginInfo);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public AccountScanEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_account_scan ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<AccountScanEntity> rowMapper = new BeanPropertyRowMapper<>(AccountScanEntity.class);
        List<AccountScanEntity> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return result.stream().findFirst().orElse(null);
    }
}
