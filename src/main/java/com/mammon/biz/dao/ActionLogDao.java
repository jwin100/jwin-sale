package com.mammon.biz.dao;

import com.mammon.biz.domain.entity.ActionLogEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author dcl
 * @since 2024/10/16 19:25
 */
@Repository
public class ActionLogDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int insert(ActionLogEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_biz_action_log (")
                .append(" id, merchant_no, store_no, account_id, position, event, source, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :accountId, :position, :event, :source, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        return namedParameterJdbcTemplate.update(sql, params);
    }
}
