package com.mammon.goods.dao;

import com.mammon.goods.domain.entity.SkuEntity;
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
public class SkuDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SkuEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_goods_sku (")
                .append(" id, spu_id, sku_code, sku_no, sku_name, purchase_amount, reference_amount, sku_weight, status, ")
                .append(" create_time, update_time, join_spec ")
                .append(" ) VALUES ( ")
                .append(" :id, :spuId, :skuCode, :skuNo, :skuName, :purchaseAmount, :referenceAmount, :skuWeight, :status, ")
                .append(" :createTime, :updateTime, :joinSpec ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(SkuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_goods_sku ")
                .append(" set ")
                .append(" sku_code = :skuCode, ")
                .append(" sku_no = :skuNo, ")
                .append(" sku_name = :skuName, ")
                .append(" purchase_amount = :purchaseAmount, ")
                .append(" reference_amount = :referenceAmount, ")
                .append(" sku_weight = :skuWeight, ")
                .append(" join_spec = :joinSpec, ")
                .append(" status = :status, ")
                .append(" update_time = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_goods_sku set deleted = 1 ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public SkuEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_sku ")
                .append(" WHERE  deleted = 0 AND id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<SkuEntity> rowMapper = new BeanPropertyRowMapper<>(SkuEntity.class);
        List<SkuEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public SkuEntity findBySkuNo(long merchantNo, String skuNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" distinct sku.* ")
                .append(" FROM m_goods_sku sku ")
                .append(" LEFT JOIN m_goods_spu spu on spu.id = sku.spu_id ")
                .append(" WHERE spu.deleted = 0 AND sku.deleted = 0 AND spu.merchant_no = :merchantNo ")
                .append(" AND sku.sku_no = :skuNo");

        params.addValue("merchantNo", merchantNo);
        params.addValue("skuNo", skuNo);

        String sql = sb.toString();

        RowMapper<SkuEntity> rowMapper = new BeanPropertyRowMapper<>(SkuEntity.class);
        List<SkuEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<SkuEntity> findAllByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_sku ")
                .append(" WHERE deleted = 0 AND id in ( :ids ) ");
        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<SkuEntity> rowMapper = new BeanPropertyRowMapper<>(SkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SkuEntity> findAllBySpuId(String spuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_sku ")
                .append(" WHERE deleted = 0 AND spu_id = :spuId ");
        params.addValue("spuId", spuId);

        String sql = sb.toString();

        RowMapper<SkuEntity> rowMapper = new BeanPropertyRowMapper<>(SkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SkuEntity> findAll() {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_sku ");

        String sql = sb.toString();

        RowMapper<SkuEntity> rowMapper = new BeanPropertyRowMapper<>(SkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
