package com.mammon.document.dao;

import com.mammon.document.domain.entity.DocumentResourceEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/8/2 11:58
 */
@Repository
public class DocumentResourceDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<DocumentResourceEntity> getList() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_document_resource ")
                .append(" WHERE status = 1 ");

        String sql = sb.toString();

        RowMapper<DocumentResourceEntity> rowMapper = new BeanPropertyRowMapper<>(DocumentResourceEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
