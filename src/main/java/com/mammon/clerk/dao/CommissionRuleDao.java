package com.mammon.clerk.dao;

import com.mammon.enums.CommonDeleted;
import com.mammon.clerk.domain.entity.CommissionRuleEntity;
import com.mammon.clerk.domain.query.CommissionRuleQuery;
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
 * @since 2024/4/7 16:42
 */

@Repository
public class CommissionRuleDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CommissionRuleEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_clerk_commission_rule (")
                .append(" id, merchant_no, type, mode, unit, rate, status, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :type, :mode, :unit, :rate, :status, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(CommissionRuleEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_commission_rule ")
                .append(" set ")
                .append(" mode           = :mode, ")
                .append(" unit           = :unit, ")
                .append(" rate           = :rate, ")
                .append(" update_time    = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(String id, int status) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_clerk_commission_rule ")
                .append(" set ")
                .append(" status = :status ")
                .append(" where id = :id ");

        params.addValue("status", status);
        params.addValue("id", id);

        String sql = sb.toString();
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(String id) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_clerk_commission_rule set deleted = :deleted ")
                .append(" where id = :id ");
        params.addValue("id", id);
        params.addValue("deleted", CommonDeleted.DELETED.getCode());

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public CommissionRuleEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_commission_rule ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);
        String sql = sb.toString();

        RowMapper<CommissionRuleEntity> rowMapper = new BeanPropertyRowMapper<>(CommissionRuleEntity.class);
        List<CommissionRuleEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<CommissionRuleEntity> findList(long merchantNo, CommissionRuleQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_commission_rule ")
                .append(" WHERE merchant_no = :merchantNo and deleted = 0 ");

        params.addValue("merchantNo", merchantNo);

        if (query.getStatus() != null) {
            sb.append(" AND status = :status ");
            params.addValue("status", query.getStatus());
        }

        sb.append(" ORDER BY create_time ASC ");

        String sql = sb.toString();
        RowMapper<CommissionRuleEntity> rowMapper = new BeanPropertyRowMapper<>(CommissionRuleEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int countPage(long merchantNo, CommissionRuleQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_clerk_commission_rule ")
                .append(" WHERE merchant_no = :merchantNo and deleted = 0 ");
        params.addValue("merchantNo", merchantNo);
        String sql = sb.toString();

        Integer result = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
        if (result == null) {
            return 0;
        }
        return result;
    }

    public List<CommissionRuleEntity> findPage(long merchantNo, CommissionRuleQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_commission_rule ")
                .append(" WHERE merchant_no = :merchantNo and deleted = 0 ");

        params.addValue("merchantNo", merchantNo);

        sb.append(" ORDER BY create_time ASC ");
        sb.append(" limit :limit offset :offset ");
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<CommissionRuleEntity> rowMapper = new BeanPropertyRowMapper<>(CommissionRuleEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
