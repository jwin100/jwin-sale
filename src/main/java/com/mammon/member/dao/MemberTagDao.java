package com.mammon.member.dao;

import com.mammon.member.domain.entity.MemberEntity;
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
public class MemberTagDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MemberTagEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_member_tag (")
                .append(" id, merchant_no, name, color, status, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :name, :color, :status, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(MemberTagEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_member_tag ")
                .append(" set ")
                .append(" name          = :name, ")
                .append(" color         = :color, ")
                .append(" status        = :status, ")
                .append(" update_time   = :updateTime ")
                .append(" where id      = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(long merchantNo, String id) {
        StringBuilder sb = new StringBuilder();

        sb.append("delete from  m_member_tag ")
                .append(" where id = :id and merchant_no = :merchantNo ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public MemberTagEntity findByName(long merchantNo, String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_tag ")
                .append(" WHERE merchant_no = :merchantNo and name = :name ")
                .append(" LIMIT 1");
        params.addValue("merchantNo", merchantNo);
        params.addValue("name", name);

        String sql = sb.toString();
        RowMapper<MemberTagEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTagEntity.class);
        List<MemberTagEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public MemberTagEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_tag ")
                .append(" WHERE merchant_no = :merchantNo and id = :id ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<MemberTagEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTagEntity.class);
        List<MemberTagEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<MemberTagEntity> findAllByIds(long merchantNo, List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_tag ")
                .append(" WHERE merchant_no = :merchantNo and id in ( :ids ) ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<MemberTagEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTagEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<MemberTagEntity> findAll(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_tag ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<MemberTagEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTagEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
