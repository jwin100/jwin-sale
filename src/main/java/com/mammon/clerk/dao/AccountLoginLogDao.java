package com.mammon.clerk.dao;

import com.mammon.clerk.domain.entity.AccountLoginLogEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class AccountLoginLogDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(AccountLoginLogEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_clerk_account_login_log (")
                .append(" id, account_id, type, login_time, ip, platform, address, user_agent, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :accountId, :type, :loginTime, :ip, :platform, :address, :userAgent, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        return namedParameterJdbcTemplate.update(sql, params);
    }
}
