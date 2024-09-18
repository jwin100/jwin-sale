package com.mammon.print.dao;

import com.mammon.print.domain.entity.PrintChannelEntity;
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
public class PrintChannelDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int edit(PrintChannelEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_print_channel ")
                .append(" set ")
                .append(" config_str = (:configStr)::jsonb ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public PrintChannelEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_print_channel ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<PrintChannelEntity> rowMapper = new BeanPropertyRowMapper<>(PrintChannelEntity.class);
        List<PrintChannelEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public PrintChannelEntity findByChannelCode(String channelCode) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_print_channel ")
                .append(" WHERE channel_code = :channelCode ");

        params.addValue("channelCode", channelCode);

        String sql = sb.toString();

        RowMapper<PrintChannelEntity> rowMapper = new BeanPropertyRowMapper<>(PrintChannelEntity.class);
        List<PrintChannelEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<PrintChannelEntity> findListByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_print_channel ")
                .append(" WHERE id in ( :ids ) ");

        params.addValue("ids", ids);
        String sql = sb.toString();

        RowMapper<PrintChannelEntity> rowMapper = new BeanPropertyRowMapper<>(PrintChannelEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
