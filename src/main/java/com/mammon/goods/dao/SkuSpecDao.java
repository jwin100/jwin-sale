package com.mammon.goods.dao;

import com.mammon.goods.domain.entity.SkuEntity;
import com.mammon.goods.domain.entity.SkuSpecEntity;
import com.mammon.goods.domain.entity.SpecEntity;
import com.mammon.goods.domain.vo.SkuSpecVo;
import org.apache.commons.lang3.StringUtils;
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

/**
 * @author dcl
 * @date 2023-03-22 18:16:47
 */
@Repository
public class SkuSpecDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SkuSpecEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_goods_sku_spec (")
                .append(" id, spu_id, sku_id, spec_id, spec_name, spec_value_id, spec_value_name, status, ")
                .append(" create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :spuId, :skuId, :specId, :specName, :specValueId, :specValueName, :status, ")
                .append(" :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editByStatus(int status, String spuId, String skuId) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        sb.append("update m_goods_sku_spec ")
                .append(" set ")
                .append(" status = :status, ")
                .append(" update_time = :updateTime ")
                .append(" where spu_id = :spuId ");
        if (StringUtils.isNotBlank(skuId)) {
            sb.append(" and sku_id = skuId ");
            params.addValue("skuId", skuId);
        }


        params.addValue("status", status);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("spuId", spuId);

        String sql = sb.toString();


        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteBySpuId(String spuId) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from m_goods_sku_spec ")
                .append(" where spu_id = :spuId ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("spuId", spuId);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteBySkuId(String skuId) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from m_goods_sku_spec ")
                .append(" where sku_id = :skuId ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("skuId", skuId);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<SkuSpecEntity> findAllBySpuId(String spuId, String skuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_sku_spec ")
                .append(" where spu_id = :spuId ");
        params.addValue("spuId", spuId);

        if (StringUtils.isNotBlank(skuId)) {
            sb.append(" and sku_id = :skuId ");
            params.addValue("skuId", skuId);
        }

        String sql = sb.toString();

        RowMapper<SkuSpecEntity> rowMapper = new BeanPropertyRowMapper<>(SkuSpecEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SkuSpecEntity> findAllBySkuId(String skuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_sku_spec ")
                .append(" where sku_id = :skuId ");
        params.addValue("skuId", skuId);

        String sql = sb.toString();

        RowMapper<SkuSpecEntity> rowMapper = new BeanPropertyRowMapper<>(SkuSpecEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SkuSpecEntity> findAllBySkuIds(List<String> skuIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_sku_spec ")
                .append(" where sku_id in ( :skuIds ) ");
        params.addValue("skuIds", skuIds);

        String sql = sb.toString();

        RowMapper<SkuSpecEntity> rowMapper = new BeanPropertyRowMapper<>(SkuSpecEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SkuSpecEntity> findAllBySpuIds(List<String> spuIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_sku_spec ")
                .append(" where spu_id in ( :spuIds ) ");
        params.addValue("spuIds", spuIds);

        String sql = sb.toString();

        RowMapper<SkuSpecEntity> rowMapper = new BeanPropertyRowMapper<>(SkuSpecEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SkuSpecEntity> findAllBySpecId(String specId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_sku_spec ")
                .append(" where ( spec_id = :specId or spec_value_id = :specId ) ");
        params.addValue("specId", specId);

        String sql = sb.toString();

        RowMapper<SkuSpecEntity> rowMapper = new BeanPropertyRowMapper<>(SkuSpecEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
