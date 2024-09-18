package com.mammon.document.dao;

import cn.hutool.core.util.StrUtil;
import com.mammon.document.domain.entity.DocumentHelpEntity;
import com.mammon.document.domain.entity.DocumentResourceEntity;
import com.mammon.document.domain.query.DocumentHelpQuery;
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
public class DocumentHelpDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DocumentHelpEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_document_help ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<DocumentHelpEntity> rowMapper = new BeanPropertyRowMapper<>(DocumentHelpEntity.class);
        List<DocumentHelpEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int countPage(DocumentHelpQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(id) ")
                .append(" FROM  m_document_help ")
                .append(" WHERE status = 1 ");
        if (StrUtil.isNotBlank(query.getSearchKey())) {
            sb.append(" AND title like  :searchKey ");
            params.addValue("searchKey", "%" + query.getSearchKey() + "%");
        } else if (StrUtil.isNotBlank(query.getPid())) {
            sb.append(" AND resource_id = :pid ");
            params.addValue("pid", query.getPid());
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<DocumentHelpEntity> findPage(DocumentHelpQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" id, resource_id, code, title, status, create_time, update_time ")
                .append(" FROM  m_document_help ")
                .append(" WHERE status = 1 ");

        if (StrUtil.isNotBlank(query.getSearchKey())) {
            sb.append(" AND title like  :searchKey ");
            params.addValue("searchKey", "%" + query.getSearchKey() + "%");
        } else if (StrUtil.isNotBlank(query.getPid())) {
            sb.append(" AND resource_id = :pid ");
            params.addValue("pid", query.getPid());
        }

        sb.append(" ORDER BY create_time DESC ");
        sb.append(" limit :limit offset :offset ");

        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<DocumentHelpEntity> rowMapper = new BeanPropertyRowMapper<>(DocumentHelpEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
