package com.mammon.goods.dao;

import com.mammon.enums.CommonDeleted;
import com.mammon.goods.domain.entity.CategoryEntity;
import com.mammon.goods.domain.query.CategoryListQuery;
import com.mammon.goods.domain.query.CategoryQuery;
import org.apache.commons.lang3.StringUtils;
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
public class CategoryDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CategoryEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_goods_attr_category (")
                .append(" id, merchant_no, name, pid, level, status, create_time, update_time, sort ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :name, :pid, :level, :status, :createTime, :updateTime, :sort ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(CategoryEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE m_goods_attr_category ")
                .append(" SET ")
                .append(" name = :name, ")
                .append(" pid = :pid, ")
                .append(" level = :level, ")
                .append(" status = :status, ")
                .append(" sort = :sort, ")
                .append(" update_time = :updateTime ")
                .append(" WHERE id = :id and merchant_no = :merchantNo ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(long merchantNo, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM m_goods_attr_category ")
                .append(" WHERE id = :id and merchant_no = :merchantNo ");

        String sql = sb.toString();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public CategoryEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_category ")
                .append(" WHERE id = :id and merchant_no = :merchantNo ");
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<CategoryEntity> rowMapper = new BeanPropertyRowMapper<>(CategoryEntity.class);
        List<CategoryEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int countPage(long merchantNo, CategoryQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_goods_attr_category ");
        sb.append(" WHERE pid IS NULL ");
        sb.append(" and ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        sb.append(" and deleted = 0 ");
        params.addValue("merchantNo", merchantNo);
        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" AND name like :name ");
            params.addValue("name", "%" + query.getSearchKey() + "%");
        }

        if (query.getLevel() != null) {
            sb.append(" AND level = :level ");
            params.addValue("level", query.getLevel());
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<CategoryEntity> findPage(long merchantNo, CategoryQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_category ");
        sb.append(" WHERE ( pid = '' OR pid IS NULL ) ");
        sb.append(" and ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        sb.append(" and deleted = 0 ");
        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" AND name like :name ");
            params.addValue("name", "%" + query.getSearchKey() + "%");
        }
        if (query.getLevel() != null) {
            sb.append(" AND level = :level ");
            params.addValue("level", query.getLevel());
        }

        sb.append(" ORDER BY sort DESC, create_time DESC ");
        sb.append(" limit :limit offset :offset ");

        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();
        RowMapper<CategoryEntity> mapper = new BeanPropertyRowMapper<>(CategoryEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, mapper);
    }

    public List<CategoryEntity> findAll(long merchantNo, CategoryListQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_category ");
        sb.append(" WHERE 1 = 1 ");
        sb.append(" and ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(query.getPid())) {
            sb.append(" and pid = :pid ");
            params.addValue("pid", query.getPid());
        }

        if (StringUtils.isNotBlank(query.getName())) {
            sb.append(" AND name like :name ");
            params.addValue("name", "%" + query.getName() + "%");
        }

        if (query.getLevel() != null) {
            sb.append(" and level = :level ");
            params.addValue("level", query.getLevel());
        }

        if (query.getDeleted() != null) {
            sb.append(" and deleted = :deleted ");
            params.addValue("deleted", query.getDeleted());
        }

        sb.append(" ORDER BY sort DESC, create_time DESC ");

        String sql = sb.toString();
        RowMapper<CategoryEntity> mapper = new BeanPropertyRowMapper<>(CategoryEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, mapper);
    }

    public CategoryEntity findByName(long merchantNo, Integer level, String name, String pid) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_category ");
        sb.append(" WHERE deleted = 0 ");
        sb.append(" and ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(pid)) {
            sb.append(" and pid = :pid ");
            params.addValue("pid", pid);
        }

        if (StringUtils.isNotBlank(name)) {
            sb.append(" AND name = :name ");
            params.addValue("name", name);
        }

        if (level != null) {
            sb.append(" and level = :level ");
            params.addValue("level", level);
        }

        String sql = sb.toString();
        RowMapper<CategoryEntity> mapper = new BeanPropertyRowMapper<>(CategoryEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, mapper).stream().findFirst().orElse(null);
    }

    public List<CategoryEntity> findSubAllByPid(long merchantNo, String pid) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_category ");
        sb.append(" WHERE pid = :pid ");
        sb.append(" and ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("pid", pid);
        params.addValue("merchantNo", merchantNo);

        sb.append(" ORDER BY sort DESC, create_time DESC ");
        String sql = sb.toString();

        RowMapper<CategoryEntity> mapper = new BeanPropertyRowMapper<>(CategoryEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, mapper);
    }
}
