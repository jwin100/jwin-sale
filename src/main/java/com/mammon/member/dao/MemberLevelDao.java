package com.mammon.member.dao;

import com.mammon.member.domain.entity.MemberEntity;
import com.mammon.member.domain.entity.MemberLevelEntity;
import com.mammon.member.domain.entity.MemberTagEntity;
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
public class MemberLevelDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MemberLevelEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_member_level (")
                .append(" id, merchant_no, name, start_integral, end_integral, discount, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :name, :startIntegral, :endIntegral, :discount, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(MemberLevelEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_member_level ")
                .append(" set ")
                .append(" name              = :name, ")
                .append(" start_integral    = :startIntegral, ")
                .append(" end_integral      = :endIntegral, ")
                .append(" discount          = :discount ")
                .append(" where id          = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ")
                .append(" FROM  m_member_level ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ")
                .append(" FROM  m_member_level ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<MemberLevelEntity> findAllByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_level ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<MemberLevelEntity> rowMapper = new BeanPropertyRowMapper<>(MemberLevelEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
