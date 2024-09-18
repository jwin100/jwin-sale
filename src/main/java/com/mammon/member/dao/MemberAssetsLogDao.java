package com.mammon.member.dao;

import com.mammon.common.PageQuery;
import com.mammon.member.domain.entity.MemberAssetsEntity;
import com.mammon.member.domain.entity.MemberAssetsLogEntity;
import com.mammon.member.domain.entity.MemberEntity;
import com.mammon.member.domain.query.MemberAssetsLogQuery;
import com.mammon.member.domain.query.MemberQuery;
import org.springframework.data.domain.PageRequest;
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
 * @date 2023-04-05 14:40:38
 */
@Repository
public class MemberAssetsLogDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MemberAssetsLogEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_member_assets_log (")
                .append(" id, member_id, order_no, type, category, before_assets, change_assets, after_assets, remark, ")
                .append(" create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :memberId, :orderNo, :type, :category, :beforeAssets, :changeAssets, :afterAssets, :remark, ")
                .append(" :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<MemberAssetsLogEntity> findAllByOrderNo(String memberId, String orderNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_assets_log ")
                .append(" WHERE member_id = :memberId AND order_no = :orderNo ");
        params.addValue("memberId", memberId);
        params.addValue("orderNo", orderNo);

        String sql = sb.toString();

        RowMapper<MemberAssetsLogEntity> rowMapper = new BeanPropertyRowMapper<>(MemberAssetsLogEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int countPage(MemberAssetsLogQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(id) ")
                .append(" FROM m_member_assets_log ")
                .append(" WHERE member_id = :memberId ")
                .append(" and type = :type ");
        params.addValue("memberId", query.getMemberId());
        params.addValue("type", query.getType());

        if (query.getStartDate() != null) {
            sb.append(" AND create_time >= :createTime ");
            params.addValue("createTime", query.getStartDate());
        }
        if (query.getEndDate() != null) {
            sb.append(" AND create_time < :createTime ");
            params.addValue("createTime", query.getStartDate().plusDays(1));
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<MemberAssetsLogEntity> findPage(MemberAssetsLogQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_assets_log ")
                .append(" WHERE member_id = :memberId ")
                .append(" and type = :type ");
        params.addValue("memberId", query.getMemberId());
        params.addValue("type", query.getType());

        if (query.getStartDate() != null) {
            sb.append(" AND create_time >= :createTime ");
            params.addValue("createTime", query.getStartDate());
        }
        if (query.getEndDate() != null) {
            sb.append(" AND create_time < :createTime ");
            params.addValue("createTime", query.getStartDate().plusDays(1));
        }

        sb.append(" order by create_time desc ");
        sb.append(" limit :limit offset :offset ");
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<MemberAssetsLogEntity> rowMapper = new BeanPropertyRowMapper<>(MemberAssetsLogEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
