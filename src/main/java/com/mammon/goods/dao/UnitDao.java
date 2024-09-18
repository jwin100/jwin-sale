package com.mammon.goods.dao;

import com.mammon.goods.domain.entity.UnitEntity;
import com.mammon.goods.domain.query.UnitQuery;
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

@Repository
public class UnitDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(UnitEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_goods_attr_unit (")
                .append(" id, merchant_no, name, type, status, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :name, :type, :status, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(UnitEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_goods_attr_unit ")
                .append(" set ")
                .append(" name = :name, ")
                .append(" type = :type, ")
                .append(" status = :status, ")
                .append(" update_time = :updateTime ")
                .append(" where id = :id and merchant_no = :merchantNo ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(long merchantNo, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM m_goods_attr_unit ")
                .append(" where id = :id ");
        sb.append(" and merchant_no = :merchantNo ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public UnitEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_unit ")
                .append(" WHERE id = :id ");
        sb.append(" and ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<UnitEntity> rowMapper = new BeanPropertyRowMapper<>(UnitEntity.class);
        List<UnitEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (store.iterator().hasNext()) {
            return store.get(0);
        }
        return null;
    }

    public UnitEntity findByMerchantNoAndName(long merchantNo, String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_unit ")
                .append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) AND name = :name ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("name", name);

        String sql = sb.toString();

        RowMapper<UnitEntity> rowMapper = new BeanPropertyRowMapper<>(UnitEntity.class);
        List<UnitEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (store.iterator().hasNext()) {
            return store.get(0);
        }
        return null;
    }

    public List<UnitEntity> findAllByIds(long merchantNo, List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_unit ")
                .append(" WHERE id in ( :ids ) ");
        params.addValue("ids", ids);

        sb.append(" and ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<UnitEntity> rowMapper = new BeanPropertyRowMapper<>(UnitEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int count(long merchantNo, String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_goods_attr_unit ");
        sb.append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);
        if (StringUtils.isNotBlank(name)) {
            sb.append(" and name like :name ");
            params.addValue("name", "%" + name + "%");
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<UnitEntity> findPage(long merchantNo, String name, UnitQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_unit ");
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

        RowMapper<UnitEntity> rowMapper = new BeanPropertyRowMapper<>(UnitEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<UnitEntity> findAll(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_attr_unit ");
        sb.append(" WHERE ( merchant_no = :merchantNo OR merchant_no = 0 ) ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<UnitEntity> rowMapper = new BeanPropertyRowMapper<>(UnitEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
