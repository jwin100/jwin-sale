package com.mammon.goods.dao;

import com.mammon.goods.domain.entity.SpecEntity;
import com.mammon.goods.domain.query.SpecKeyQuery;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Optional;

@Repository
public class SpecDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SpecEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_goods_attr_spec (")
                .append(" id, merchant_no, name, pid, status, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :name, :pid, :status, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(SpecEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_goods_attr_spec ")
                .append(" set ")
                .append(" name = :name, ")
                .append(" pid = :pid, ")
                .append(" status = :status, ")
                .append(" update_time = :updateTime ")
                .append(" where id = :id and merchant_no = :merchantNo ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(long merchantNo, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM  m_goods_attr_spec ")
                .append(" where id = :id and merchant_no = :merchantNo ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public SpecEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_spec ")
                .append(" WHERE id = :id ");
        sb.append(" and ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SpecEntity> rowMapper = new BeanPropertyRowMapper<>(SpecEntity.class);
        List<SpecEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (store.iterator().hasNext()) {
            return store.get(0);
        }
        return null;
    }

    public SpecEntity findByMerchantNoAndName(long merchantNo, String name, String pid) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_spec ")
                .append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) ")
                .append(" AND name = :name ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("name", name);

        if (StringUtils.isBlank(pid)) {
            sb.append(" AND pid is null ");
        } else {
            sb.append(" AND pid  = :pid ");
            params.addValue("pid", pid);
        }

        String sql = sb.toString();

        RowMapper<SpecEntity> rowMapper = new BeanPropertyRowMapper<>(SpecEntity.class);
        List<SpecEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<SpecEntity> findAllByIds(long merchantNo, List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_spec ")
                .append(" WHERE id in ( :ids ) and ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("ids", ids);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SpecEntity> rowMapper = new BeanPropertyRowMapper<>(SpecEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int count(long merchantNo, SpecKeyQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_goods_attr_spec ");
        sb.append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) ")
                .append(" AND ( pid IS NULL OR pid = '' ) ");
        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" and name like :name ");
            params.addValue("name", "%" + query.getSearchKey() + "%");
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<SpecEntity> findPage(long merchantNo, SpecKeyQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_spec ");
        sb.append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) ")
                .append(" AND ( pid IS NULL OR pid = '' ) ");
        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" and name like :name ");
            params.addValue("name", "%" + query.getSearchKey() + "%");
        }

        sb.append(" ORDER BY create_time DESC ");
        sb.append(" limit :limit offset :offset ");
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<SpecEntity> rowMapper = new BeanPropertyRowMapper<>(SpecEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SpecEntity> findAll(long merchantNo, SpecKeyQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_spec ");
        sb.append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) ");

        if (StringUtils.isNotBlank(query.getPid())) {
            sb.append(" AND pid = :pid ");
            params.addValue("pid", query.getPid());
        } else {
            sb.append(" AND ( pid = '' OR pid IS NULL ) ");
        }

        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" and name like :name ");
            params.addValue("name", "%" + query.getSearchKey() + "%");
        }

        sb.append(" ORDER BY update_time ASC ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SpecEntity> rowMapper = new BeanPropertyRowMapper<>(SpecEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SpecEntity> findAllByPid(long merchantNo, String pid) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_spec ");
        sb.append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(pid)) {
            sb.append(" AND pid = :pid ");
            params.addValue("pid", pid);
        } else {
            sb.append(" AND ( pid = '' OR pid IS NULL ) ");
        }

        String sql = sb.toString();

        RowMapper<SpecEntity> rowMapper = new BeanPropertyRowMapper<>(SpecEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
