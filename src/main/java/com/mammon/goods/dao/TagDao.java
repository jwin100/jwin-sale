package com.mammon.goods.dao;

import com.mammon.goods.domain.entity.TagEntity;
import com.mammon.goods.domain.query.TagQuery;
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
public class TagDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(TagEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_goods_attr_tag (")
                .append(" id, merchant_no, name, remark, status, create_time, update_time, sort ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :name, :remark, :status, :createTime, :updateTime, :sort ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(TagEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_goods_attr_tag ")
                .append(" set ")
                .append(" name = :name, ")
                .append(" remark = :remark, ")
                .append(" status = :status, ")
                .append(" update_time = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(long merchantNo, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM m_goods_attr_tag ")
                .append(" where id = :id ");
        sb.append(" and merchant_no = :merchantNo ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public TagEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_tag ")
                .append(" WHERE id = :id ");
        sb.append(" and ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<TagEntity> rowMapper = new BeanPropertyRowMapper<>(TagEntity.class);
        List<TagEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (store.iterator().hasNext()) {
            return store.get(0);
        }
        return null;
    }

    public TagEntity findByMerchantNoAndName(long merchantNo, String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_tag ")
                .append(" WHERE name = :name AND ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("name", name);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<TagEntity> rowMapper = new BeanPropertyRowMapper<>(TagEntity.class);
        List<TagEntity> tags = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (tags.iterator().hasNext()) {
            return tags.get(0);
        }
        return null;
    }

    public List<TagEntity> findAllByIds(long merchantNo, List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_tag ")
                .append(" WHERE id in ( :ids ) ");
        params.addValue("ids", ids);

        sb.append(" and ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<TagEntity> rowMapper = new BeanPropertyRowMapper<>(TagEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int count(long merchantNo, String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_goods_attr_tag ");
        sb.append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);
        if (StringUtils.isNotBlank(name)) {
            sb.append(" and name like :name ");
            params.addValue("name", "%" + name + "%");
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<TagEntity> findPage(long merchantNo, String name, TagQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_tag ");

        sb.append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(name)) {
            sb.append(" and name like :name ");
            params.addValue("name", "%" + name + "%");
        }

        sb.append(" ORDER BY create_time DESC ");
        sb.append(" limit :limit offset :offset ");
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<TagEntity> rowMapper = new BeanPropertyRowMapper<>(TagEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<TagEntity> findAll(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_tag ");
        sb.append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<TagEntity> rowMapper = new BeanPropertyRowMapper<>(TagEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
