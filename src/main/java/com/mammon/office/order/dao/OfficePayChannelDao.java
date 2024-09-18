package com.mammon.office.order.dao;

import com.mammon.office.order.domain.entity.OfficeOrderEntity;
import com.mammon.office.order.domain.entity.OfficePayChannelEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-02 16:02:52
 */
@Repository
public class OfficePayChannelDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OfficePayChannelEntity findByCode(String code) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_pay_channel ")
                .append(" WHERE code = :code ");
        params.addValue("code", code);

        String sql = sb.toString();

        RowMapper<OfficePayChannelEntity> rowMapper = new BeanPropertyRowMapper<>(OfficePayChannelEntity.class);
        List<OfficePayChannelEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public OfficePayChannelEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_pay_channel ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<OfficePayChannelEntity> rowMapper = new BeanPropertyRowMapper<>(OfficePayChannelEntity.class);
        List<OfficePayChannelEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
