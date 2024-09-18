package com.mammon.feedback.dao;

import com.mammon.feedback.domain.entity.FeedbackEntity;
import com.mammon.feedback.domain.query.FeedbackPageQuery;
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
 * @date 2022-11-02 14:36:30
 */
@Repository
public class FeedbackDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(FeedbackEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_feedback (")
                .append(" id, merchant_no, store_no, account_id, type, title, content, images, contact_type, contact_no, ")
                .append(" status, ip, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :accountId, :type, :title, :content, :images, :contactType, :contactNo, ")
                .append(" :status, :ip, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(FeedbackEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_feedback ")
                .append(" set ")
                .append(" type    = :type, ")
                .append(" title    = :title, ")
                .append(" content    = :content, ")
                .append(" images    = :images, ")
                .append(" contact_type    = :contactType, ")
                .append(" contact_no    = :contactNo, ")
                .append(" status    = :status, ")
                .append(" update_time    = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public FeedbackEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_feedback ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<FeedbackEntity> rowMapper = new BeanPropertyRowMapper<>(FeedbackEntity.class);
        List<FeedbackEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int countPage(long merchantNo, Long storeNo, String accountId, FeedbackPageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_feedback ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo");
        if (query.isJustMe()) {
            sb.append(" AND account_id = :accountId ");
            params.addValue("accountId", accountId);
        }

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<FeedbackEntity> findPage(long merchantNo, Long storeNo, String accountId, FeedbackPageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_feedback ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo");

        if (query.isJustMe()) {
            sb.append(" AND account_id = :accountId ");
            params.addValue("accountId", accountId);
        }
        sb.append(" ORDER BY create_time DESC ");
        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<FeedbackEntity> rowMapper = new BeanPropertyRowMapper<>(FeedbackEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
