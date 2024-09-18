package com.mammon.leaf.dao;

import com.mammon.leaf.domain.entity.LeafCodeEntity;
import com.mammon.leaf.domain.entity.LeafConfigEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/9/8 20:04
 */
@Repository
public class LeafConfigDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public LeafConfigEntity getInfo() {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_leaf_config ");

        String sql = sb.toString();

        RowMapper<LeafConfigEntity> rowMapper = new BeanPropertyRowMapper<>(LeafConfigEntity.class);
        List<LeafConfigEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
