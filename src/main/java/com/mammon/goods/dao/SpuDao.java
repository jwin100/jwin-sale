package com.mammon.goods.dao;

import com.mammon.common.PageQuery;
import com.mammon.config.JsonMapper;
import com.mammon.goods.domain.query.SpuPageQuery;
import com.mammon.goods.domain.query.SpuQuery;
import com.mammon.goods.domain.entity.SpuEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Repository
@Slf4j
public class SpuDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SpuEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_goods_spu (")
                .append(" id, merchant_no, category_id, spu_code, spu_no, name, unit_id, remark, counted_type, status, ")
                .append(" create_time, update_time, pictures ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :categoryId, :spuCode, :spuNo, :name, :unitId, :remark, :countedType, :status, ")
                .append(" :createTime, :updateTime, (:pictures)::jsonb ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(SpuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_goods_spu ")
                .append(" set ")
                .append(" category_id = :categoryId, ")
                .append(" spu_code = :spuCode, ")
                .append(" spu_no = :spuNo, ")
                .append(" name = :name, ")
                .append(" unit_id = :unitId, ")
                .append(" pictures = (:pictures)::jsonb, ")
                .append(" remark = :remark, ")
                .append(" counted_type = :countedType, ")
                .append(" update_time = :updateTime ")
                .append(" where id = :id AND merchant_no = :merchantNo ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateState(long merchantNo, String id, int status) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_goods_spu ")
                .append(" set ")
                .append(" status = :status ")
                .append(" where id = :id AND merchant_no = :merchantNo ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);
        params.addValue("status", status);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(long merchantNo, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_goods_spu set deleted = 1 ")
                .append(" where id = :id AND merchant_no = :merchantNo ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public SpuEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_spu ")
                .append(" WHERE  deleted = 0 AND id = :id AND merchant_no = :merchantNo ");
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SpuEntity> rowMapper = new BeanPropertyRowMapper<>(SpuEntity.class);
        List<SpuEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return store.stream().findFirst().orElse(null);
    }

    public SpuEntity findByName(long merchantNo, String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM m_goods_spu ")
                .append(" WHERE deleted = 0 AND name = :name AND merchant_no = :merchantNo ");
        params.addValue("name", name);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SpuEntity> rowMapper = new BeanPropertyRowMapper<>(SpuEntity.class);
        List<SpuEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return store.stream().findFirst().orElse(null);
    }

    public List<SpuEntity> findAllByIds(long merchantNo, List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_spu ")
                .append(" WHERE deleted = 0 AND id in ( :ids ) AND merchant_no = :merchantNo ");
        params.addValue("ids", ids);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SpuEntity> rowMapper = new BeanPropertyRowMapper<>(SpuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public SpuEntity findBySpuNo(long merchantNo, String spuNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods_spu ")
                .append(" WHERE deleted = 0 AND spu_no = :spuNo AND merchant_no = :merchantNo ");
        params.addValue("spuNo", spuNo);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SpuEntity> rowMapper = new BeanPropertyRowMapper<>(SpuEntity.class);
        List<SpuEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return store.stream().findFirst().orElse(null);
    }

    public int countPage(long merchantNo, SpuPageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(distinct spu.id) ")
                .append(" FROM m_goods_spu spu ")
                .append(" LEFT JOIN m_goods_sku sku on spu.id = sku.spu_id ")
                .append(" WHERE spu.deleted = 0  AND sku.deleted = 0 AND spu.merchant_no = :merchantNo ")
                .append(pageWhere(query, params));

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<SpuEntity> findPage(long merchantNo, SpuPageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" distinct spu.* ")
                .append(" FROM m_goods_spu spu ")
                .append(" LEFT JOIN m_goods_sku sku on spu.id = sku.spu_id ")
                .append(" WHERE spu.deleted = 0 AND sku.deleted = 0 AND spu.merchant_no = :merchantNo ")
                .append(pageWhere(query, params))
                .append(" ORDER BY spu.create_time desc ");

        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<SpuEntity> rowMapper = new BeanPropertyRowMapper<>(SpuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<SpuEntity> findAllByQuery(long merchantNo, SpuQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" distinct spu.* ")
                .append(" FROM m_goods_spu spu ")
                .append(" LEFT JOIN m_goods_sku sku on spu.id = sku.spu_id ")
                .append(" WHERE spu.deleted = 0 AND spu.merchant_no = :merchantNo ")
                .append(listWhere(query, params))
                .append(" ORDER BY spu.create_time desc ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SpuEntity> rowMapper = new BeanPropertyRowMapper<>(SpuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String pageWhere(SpuPageQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(query.getSpuNo())) {
            sb.append(" AND ( spu.spu_no like :spuNo OR sku.sku_no like :spuNo) ");
            params.addValue("spuNo", "%" + query.getSpuNo() + "%");
        }
        if (StringUtils.isNotBlank(query.getSpuCode())) {
            sb.append(" AND ( spu.spu_code like :spuCode OR sku.sku_code like :spuCode)");
            params.addValue("spuCode", "%" + query.getSpuCode() + "%");
        }
        if (StringUtils.isNotBlank(query.getName())) {
            sb.append(" AND spu.name like :name ");
            params.addValue("name", "%" + query.getName() + "%");
        }
        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" AND ( ")
                    .append(" spu.spu_code like :searchKey OR spu.spu_no like :searchKey OR spu.name like :searchKey ")
                    .append(" OR sku.sku_code like :searchKey OR sku.sku_no like :searchKey OR sku.sku_name like :searchKey ")
                    .append(" ) ");
            params.addValue("searchKey", "%" + query.getSearchKey() + "%");
        }
        if (StringUtils.isNotBlank(query.getUnitId())) {
            sb.append(" AND spu.unit_id = :unitId ");
            params.addValue("unitId", query.getUnitId());
        }
        if (!CollectionUtils.isEmpty(query.getCategoryIds())) {
            sb.append(" AND category_id in ( :categoryIds )");
            params.addValue("categoryIds", query.getCategoryIds());
        }
        if (query.getCountedType() != null) {
            sb.append(" AND spu.counted_type = :countedType ");
            params.addValue("countedType", query.getCountedType());
        }
        if (query.getStatus() != null) {
            sb.append(" AND spu.status = :status ");
            params.addValue("status", query.getStatus());
        }
        return sb.toString();
    }

    private String listWhere(SpuQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(query.getSpuNo())) {
            sb.append(" AND spu.spu_no like :spuNo ");
            params.addValue("spuNo", "%" + query.getSpuNo() + "%");
        }
        if (StringUtils.isNotBlank(query.getSpuCode())) {
            sb.append(" AND spu.spu_code like :spuCode ");
            params.addValue("spuCode", "%" + query.getSpuCode() + "%");
        }
        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" AND ( ")
                    .append(" spu.spu_code like :searchKey OR spu.spu_no like :searchKey OR spu.name like :searchKey ")
                    .append(" OR sku.sku_code like :searchKey OR sku.sku_no like :searchKey OR sku.sku_name like :searchKey ")
                    .append(" ) ");
            params.addValue("searchKey", "%" + query.getSearchKey() + "%");
        }
        if (StringUtils.isNotBlank(query.getUnitId())) {
            sb.append(" AND spu.unit_id = :unitId ");
            params.addValue("unitId", query.getUnitId());
        }
        if (!CollectionUtils.isEmpty(query.getCategoryIds())) {
            sb.append(" AND category_id in ( :categoryIds )");
            params.addValue("categoryIds", query.getCategoryIds());
        }
        if (query.getCountedType() != null) {
            sb.append(" AND spu.counted_type = :countedType ");
            params.addValue("countedType", query.getCountedType());
        }
        if (query.getStatus() != null) {
            sb.append(" AND spu.status = :status ");
            params.addValue("status", query.getStatus());
        }
        return sb.toString();
    }
}
