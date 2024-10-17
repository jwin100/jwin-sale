package com.mammon.chain.dao;

import com.mammon.chain.domain.entity.ChainEntity;
import com.mammon.config.ChainDataSourceConfig;
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
 * @date 2022-10-17 16:06:15
 */
@Repository
public class ChainDao {

    @Resource(name = ChainDataSourceConfig.ChainDbNamedParameterJdbcTemplateName)
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(ChainEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO t_chain (")
                .append(" id, code, url, link, status, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :code, :url, :link, :status, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public ChainEntity findByCode(String code) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  t_chain ")
                .append(" WHERE code = :code ");

        params.addValue("code", code);

        String sql = sb.toString();

        RowMapper<ChainEntity> rowMapper = new BeanPropertyRowMapper<>(ChainEntity.class);
        List<ChainEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
