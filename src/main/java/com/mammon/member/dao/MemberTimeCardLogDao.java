package com.mammon.member.dao;

import com.mammon.common.PageQuery;
import com.mammon.member.domain.dto.MemberTimeCardLogQuery;
import com.mammon.member.domain.entity.MemberTimeCardLogEntity;
import org.springframework.data.domain.PageRequest;
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
public class MemberTimeCardLogDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MemberTimeCardLogEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_member_time_card_log (")
                .append(" id, member_id, change_type, change_before, change_total, change_after, remark, ")
                .append(" order_no, account_id, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :memberId, :changeType, :changeBefore, :changeTotal, :changeAfter, :remark, ")
                .append(" :orderNo, :accountId, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<MemberTimeCardLogEntity> findAllByMemberId(String memberId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_time_card_log ")
                .append(" WHERE member_id = :memberId ")
                .append(" LIMIT 50 ");
        params.addValue("memberId", memberId);

        String sql = sb.toString();

        RowMapper<MemberTimeCardLogEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTimeCardLogEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int countPage(MemberTimeCardLogQuery dto) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(id) ")
                .append(" FROM m_member_time_card_log ")
                .append(" WHERE member_id = :memberId ");
        params.addValue("memberId", dto.getMemberId());

        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            sb.append(" and create_time >= :startDate ");
            sb.append(" and create_time < :endDate ");
            params.addValue("startDate", dto.getStartDate());
            params.addValue("endDate", dto.getEndDate().plusDays(1));
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<MemberTimeCardLogEntity> findPage(MemberTimeCardLogQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_time_card_log ")
                .append(" WHERE member_id = :memberId ");
        params.addValue("memberId", query.getMemberId());

        if (query.getStartDate() != null && query.getEndDate() != null) {
            sb.append(" and create_time >= :startDate ");
            sb.append(" and create_time < :endDate ");
            params.addValue("startDate", query.getStartDate());
            params.addValue("endDate", query.getEndDate().plusDays(1));
        }

        sb.append(" ORDER BY create_time DESC ");
        sb.append(" limit :limit offset :offset ");
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<MemberTimeCardLogEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTimeCardLogEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
