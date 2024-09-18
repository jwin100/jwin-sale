package com.mammon.member.dao;

import com.mammon.member.domain.query.MemberQuery;
import com.mammon.member.domain.entity.MemberEntity;
import com.mammon.member.domain.query.MemberSummaryQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MemberDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MemberEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_member (")
                .append(" id, merchant_no, store_no, phone, name, sex, birth_day, ")
                .append(" status, remark, avatar, source, channel, reference_id, ")
                .append(" account_id, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :phone, :name, :sex, :birthDay, ")
                .append(" :status, :remark, :avatar, :source, :channel, :referenceId, ")
                .append(" :accountId, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(MemberEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_member ")
                .append(" set ")
                .append(" name           = :name, ")
                .append(" sex            = :sex, ")
                .append(" birth_day      = :birthDay, ")
                .append(" avatar         = :avatar, ")
                .append(" source         = :source, ")
                .append(" channel        = :channel, ")
                .append(" reference_id   = :referenceId, ")
                .append(" remark         = :remark, ")
                .append(" update_time    = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editPhone(String id, String phone) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();

        sb.append("update m_member ")
                .append(" set ")
                .append(" phone    = :phone ")
                .append(" where id = :id ");

        String sql = sb.toString();
        params.addValue("phone", phone);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(long merchantNo, String id, int status) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE m_member")
                .append(" SET ")
                .append(" status = :status ")
                .append(" WHERE merchant_no = :merchantNo AND id = :id ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);
        params.addValue("status", status);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editLevel(long merchantNo, String id, String levelId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE m_member")
                .append(" SET ")
                .append(" level_id = :levelId ")
                .append(" WHERE merchant_no = :merchantNo AND id = :id ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);
        params.addValue("levelId", levelId);

        String sql = sb.toString();
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE m_member")
                .append(" SET ")
                .append(" deleted = 1 ")
                .append(" WHERE merchant_no = :merchantNo AND id = :id ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public MemberEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<MemberEntity> rowMapper = new BeanPropertyRowMapper<>(MemberEntity.class);
        List<MemberEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public MemberEntity findByPhone(long merchantNo, String phone) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member ")
                .append(" WHERE merchant_no = :merchantNo AND phone = :phone ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("phone", phone);

        String sql = sb.toString();

        RowMapper<MemberEntity> rowMapper = new BeanPropertyRowMapper<>(MemberEntity.class);
        List<MemberEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<MemberEntity> findAllByIds(long merchantNo, List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member ")
                .append(" WHERE id in ( :ids ) and merchant_no = :merchantNo AND deleted = 0 ");
        params.addValue("ids", ids);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<MemberEntity> rowMapper = new BeanPropertyRowMapper<>(MemberEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<MemberEntity> findAll(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member ")
                .append(" WHERE merchant_no = :merchantNo AND deleted = 0 ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<MemberEntity> rowMapper = new BeanPropertyRowMapper<>(MemberEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public boolean existById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" id ")
                .append(" FROM  m_member ")
                .append(" WHERE merchant_no = :merchantNo AND id = :id  AND deleted = 0 ")
                .append(" LIMIT 1 ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();
        RowMapper<MemberEntity> rowMapper = new BeanPropertyRowMapper<>(MemberEntity.class);

        List<MemberEntity> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (CollectionUtils.isEmpty(result)) {
            return false;
        }
        return true;
    }

    public boolean existByPhone(long merchantNo, String phone) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" id ")
                .append(" FROM  m_member ")
                .append(" WHERE merchant_no = :merchantNo AND phone = :phone AND deleted = 0 ")
                .append(" LIMIT 1 ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("phone", phone);

        String sql = sb.toString();
        RowMapper<MemberEntity> rowMapper = new BeanPropertyRowMapper<>(MemberEntity.class);

        List<MemberEntity> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (result.isEmpty()) {
            return false;
        }
        return true;
    }

    public int countPage(long merchantNo, MemberQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(id) ")
                .append(" FROM m_member ")
                .append(" WHERE merchant_no = :merchantNo AND deleted = 0 ")
                .append(memberPageWhere(query, params));
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<MemberEntity> findPage(long merchantNo, MemberQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member ")
                .append(" WHERE merchant_no = :merchantNo AND deleted = 0 ")
                .append(memberPageWhere(query, params))
                .append("ORDER BY create_time DESC")
                .append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<MemberEntity> rowMapper = new BeanPropertyRowMapper<>(MemberEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String memberPageWhere(MemberQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();

        if (StringUtils.isNotBlank(query.getName())) {
            sb.append(" AND name LIKE :name ");
            params.addValue("name", "%" + query.getName() + "%");
        }

        if (StringUtils.isNotBlank(query.getPhone())) {
            sb.append(" AND phone LIKE :phone ");
            params.addValue("phone", "%" + query.getPhone() + "%");
        }

        if (query.getStoreNo() != null) {
            sb.append(" AND store_no = :storeNo ");
            params.addValue("storeNo", query.getStoreNo());
        }
        if (!CollectionUtils.isEmpty(query.getTagIds())) {
            sb.append(" AND id in (select member_id from m_member_tag_map where tag_id in ( :tagIds ))");
            params.addValue("tagIds", query.getTagIds());
        }
        return sb.toString();
    }

    public List<MemberEntity> findAllBySearchKey(long merchantNo, String searchKey, Integer status) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member ")
                .append(" WHERE merchant_no = :merchantNo AND deleted = 0 ");
        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(searchKey)) {
            sb.append(" AND ( name like :searchKey or phone like :searchKey )");
            params.addValue("searchKey", "%" + searchKey + "%");
        }

        String sql = sb.toString();

        RowMapper<MemberEntity> rowMapper = new BeanPropertyRowMapper<>(MemberEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<MemberEntity> summaryList(MemberSummaryQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member ")
                .append(" WHERE merchant_no = :merchantNo AND deleted = 0 ");
        params.addValue("merchantNo", query.getMerchantNo());

        if (query.getStoreNo() != null) {
            sb.append(" AND store_no = :storeNo ");
            params.addValue("storeNo", query.getStoreNo());
        }
        if (query.getStartDate() != null && query.getEndDate() != null) {
            sb.append(" AND create_time >= :startDate AND create_time < :endDate ");
            params.addValue("startDate", query.getStartDate());
            params.addValue("endDate", query.getEndDate());
        }

        String sql = sb.toString();

        RowMapper<MemberEntity> rowMapper = new BeanPropertyRowMapper<>(MemberEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
