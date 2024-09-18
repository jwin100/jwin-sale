package com.mammon.member.dao;

import com.mammon.enums.CommonDeleted;
import com.mammon.member.domain.entity.MemberTimeCardEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MemberTimeCardDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MemberTimeCardEntity save(MemberTimeCardEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_member_time_card (")
                .append(" id, member_id, time_card_id, name, expire_type, expire_time, now_time_card, ")
                .append(" total_time_card, create_time, update_time, spu_ids ")
                .append(" ) VALUES ( ")
                .append(" :id, :memberId, :timeCardId, :name, :expireType, :expireTime, :nowTimeCard, ")
                .append(" :totalTimeCard, :createTime, :updateTime, (:spuIds)::jsonb ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        int result = namedParameterJdbcTemplate.update(sql, params);
        if (result > 0) {
            return entity;
        }
        return null;
    }

    public MemberTimeCardEntity edit(String id, String memberId, int addTimeCard, int expireType, LocalDateTime expireTime) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_member_time_card ")
                .append(" set ")
                .append(" now_time_card   = now_time_card + :timeCard, ");
        if (addTimeCard > 0) {
            sb.append(" expire_type = :expireType, ");
            sb.append(" expire_time = :expireTime, ");
            sb.append(" total_time_card   = total_time_card + :timeCard, ");
        }
        sb.append(" update_time    = :updateTime ")
                .append(" where id = ( ");

        sb.append("SELECT ")
                .append(" id ")
                .append(" FROM m_member_time_card ")
                .append(" WHERE id = :id and member_id = :memberId ")
                .append(" FOR UPDATE ");
        sb.append(" ) RETURNING * ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("timeCard", addTimeCard);
        params.addValue("expireType", expireType);
        params.addValue("expireTime", expireTime);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("id", id);
        params.addValue("memberId", memberId);

        RowMapper<MemberTimeCardEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTimeCardEntity.class);
        List<MemberTimeCardEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public MemberTimeCardEntity edit(String id, String memberId, long timeCard) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_member_time_card ")
                .append(" set ")
                .append(" now_time_card   = now_time_card + :timeCard, ");
        if (timeCard > 0) {
            sb.append(" total_time_card   = total_time_card + :timeCard, ");
        }
        sb.append(" update_time    = :updateTime ")
                .append(" where id = ( ");

        sb.append("SELECT ")
                .append(" id ")
                .append(" FROM m_member_time_card ")
                .append(" WHERE id = :id and member_id = :memberId ");

        if (timeCard > 0) {
            sb.append(" AND now_time_card >= :originalTimeCard ");
            params.addValue("originalTimeCard", timeCard);
        }
        sb.append(" FOR UPDATE ");
        sb.append(" ) RETURNING * ");

        String sql = sb.toString();

        params.addValue("timeCard", timeCard);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("id", id);
        params.addValue("memberId", memberId);

        RowMapper<MemberTimeCardEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTimeCardEntity.class);
        List<MemberTimeCardEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int deleteById(String id) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_member_time_card ")
                .append(" set ")
                .append(" deleted  = :deleted ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("deleted", CommonDeleted.DELETED.getCode());
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public MemberTimeCardEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_time_card ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<MemberTimeCardEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTimeCardEntity.class);
        List<MemberTimeCardEntity> timeCards = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (timeCards.isEmpty()) {
            return null;
        }
        return timeCards.get(0);
    }

    public MemberTimeCardEntity findByMemberIdAndRuleId(String memberId, String timeCardRuleId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_time_card ")
                .append(" WHERE member_id = :memberId and time_card_id = :timeCardRuleId and deleted = :deleted ");
        params.addValue("memberId", memberId);
        params.addValue("timeCardRuleId", timeCardRuleId);
        params.addValue("deleted", CommonDeleted.NOT_DELETED.getCode());

        String sql = sb.toString();

        RowMapper<MemberTimeCardEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTimeCardEntity.class);
        List<MemberTimeCardEntity> timeCards = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (timeCards.isEmpty()) {
            return null;
        }
        return timeCards.get(0);
    }

    public List<MemberTimeCardEntity> findByMemberId(String memberId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_time_card ")
                .append(" WHERE member_id = :memberId and deleted = :deleted ");
        params.addValue("memberId", memberId);
        params.addValue("deleted", CommonDeleted.NOT_DELETED.getCode());

        String sql = sb.toString();

        RowMapper<MemberTimeCardEntity> rowMapper = new BeanPropertyRowMapper<>(MemberTimeCardEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
