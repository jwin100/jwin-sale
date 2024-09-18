package com.mammon.clerk.dao;

import com.mammon.clerk.domain.entity.HandoverEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2023/9/28 13:58
 */
@Repository
public class HandoverDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(HandoverEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_clerk_handover (")
                .append(" id, merchant_no, store_no, account_id, type, handover_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :accountId, :type, :handoverTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }
}
