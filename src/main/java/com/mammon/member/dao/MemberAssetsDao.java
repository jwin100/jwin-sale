package com.mammon.member.dao;

import com.mammon.member.domain.entity.MemberAssetsEntity;
import com.mammon.member.domain.entity.MemberAssetsLogEntity;
import com.mammon.member.domain.entity.MemberEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author dcl
 * @date 2023-04-05 14:40:16
 */
@Repository
public class MemberAssetsDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MemberAssetsEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_member_assets (")
                .append(" id, now_integral, accrual_integral, now_recharge, accrual_recharge, ")
                .append(" create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :nowIntegral, :accrualIntegral, :nowRecharge, :accrualRecharge, ")
                .append(" :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateRecharge(MemberAssetsEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_member_assets ")
                .append(" set ")
                .append(" now_recharge      = :nowRecharge, ")
                .append(" accrual_recharge  = :accrualRecharge, ")
                .append(" update_time       = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateIntegral(MemberAssetsEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_member_assets ")
                .append(" set ")
                .append(" now_integral      = :nowIntegral, ")
                .append(" accrual_integral  = :accrualIntegral, ")
                .append(" update_time       = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public MemberAssetsEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_assets ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<MemberAssetsEntity> rowMapper = new BeanPropertyRowMapper<>(MemberAssetsEntity.class);
        List<MemberAssetsEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<MemberAssetsEntity> findListByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_assets ")
                .append(" WHERE id in ( :ids ) ");
        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<MemberAssetsEntity> rowMapper = new BeanPropertyRowMapper<>(MemberAssetsEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
