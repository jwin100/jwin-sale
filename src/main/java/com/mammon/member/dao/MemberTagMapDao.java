package com.mammon.member.dao;

import com.mammon.member.domain.entity.MemberTagMapEntity;
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
public class MemberTagMapDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MemberTagMapEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_member_tag_map (")
                .append(" id, member_id, tag_id, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :memberId, :tagId, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteByMemberId(String memberId) {
        StringBuilder sb = new StringBuilder();

        sb.append("delete from m_member_tag_map ")
                .append(" where member_id = :memberId ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("memberId", memberId);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteByTagId(String memberId, String tagId) {
        StringBuilder sb = new StringBuilder();

        sb.append("delete from m_member_tag_map ")
                .append(" where member_id = :memberId and tag_id = :tagId ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("memberId", memberId);
        params.addValue("tagId", tagId);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<MemberTagMapEntity> findAllByMemberId(String memberId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_tag_map ")
                .append(" WHERE member_id = :memberId ");
        params.addValue("memberId", memberId);

        String sql = sb.toString();
        RowMapper<MemberTagMapEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTagMapEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
