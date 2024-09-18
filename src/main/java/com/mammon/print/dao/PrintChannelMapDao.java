package com.mammon.print.dao;

import com.mammon.print.domain.entity.PrintChannelEntity;
import com.mammon.print.domain.entity.PrintChannelMapEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/3/27 16:22
 */
@Repository
public class PrintChannelMapDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<PrintChannelMapEntity> findListByClassify(Integer classify) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_print_channel_map ")
                .append(" WHERE classify = :classify ");

        params.addValue("classify", classify);
        String sql = sb.toString();

        RowMapper<PrintChannelMapEntity> rowMapper = new BeanPropertyRowMapper<>(PrintChannelMapEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
