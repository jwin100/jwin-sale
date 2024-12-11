package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockSpuEntity;
import com.mammon.stock.domain.query.StockSpuPageQuery;
import com.mammon.stock.domain.query.StockSpuQuery;
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
import java.util.List;

/**
 * @author dcl
 * @since 2024/6/27 13:52
 */
@Repository
public class StockSpuDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockSpuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_spu (")
                .append(" id, merchant_no, store_no, spu_id, category_id, spu_code, spu_no, many_code, name, unit_id, ")
                .append(" pictures, counted_type, remark, status, goods_status, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :spuId, :categoryId, :spuCode, :spuNo, :manyCode, :name, :unitId, ")
                .append(" (:pictures)::jsonb, :countedType, :remark, :status, :goodsStatus, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(StockSpuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_spu ")
                .append(" set ")
                .append(" category_id = :categoryId, ")
                .append(" spu_code = :spuCode, ")
                .append(" spu_no = :spuNo, ")
                .append(" name = :name, ")
                .append(" unit_id = :unitId, ")
                .append(" pictures = (:pictures)::jsonb, ")
                .append(" counted_type = :countedType, ")
                .append(" status = :status, ")
                .append(" goods_status = :goodsStatus, ")
                .append(" remark = :remark, ")
                .append(" update_time = :updateTime ")
                .append(" where id = :id AND merchant_no = :merchantNo ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(String id, int status) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_stock_spu ")
                .append(" set ")
                .append(" status = :status ")
                .append(" where id = :id ");

        String sql = sb.toString();
        params.addValue("status", status);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editGoodsStatus(String id, Integer goodsStatus) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_stock_spu ")
                .append(" set ")
                .append(" goods_status = :goodsStatus ");
        sb.append(" where id = :id ");

        String sql = sb.toString();
        params.addValue("goodsStatus", goodsStatus);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteBySpuId(long merchantNo, String spuId) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_spu set deleted = 1 ")
                .append(" where spu_id = :spuId AND merchant_no = :merchantNo ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("spuId", spuId);
        params.addValue("merchantNo", merchantNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public StockSpuEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_spu ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<StockSpuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSpuEntity.class);
        List<StockSpuEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public StockSpuEntity findBySpuId(long merchantNo, long storeNo, String spuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_spu ")
                .append(" WHERE merchant_no = :merchantNo AND store_no = :storeNo ")
                .append(" AND spu_id = :spuId and deleted = 0 ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("spuId", spuId);

        String sql = sb.toString();

        RowMapper<StockSpuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSpuEntity.class);
        List<StockSpuEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<StockSpuEntity> findListBySpuIds(long merchantNo, long storeNo, List<String> spuIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_spu ")
                .append(" WHERE merchant_no = :merchantNo AND store_no = :storeNo ")
                .append(" AND spu_id IN ( :spuIds )  and deleted = 0 ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("spuIds", spuIds);

        String sql = sb.toString();

        RowMapper<StockSpuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSpuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int countPage(long merchantNo, StockSpuPageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(distinct spu.id) ")
                .append(" FROM m_stock_spu spu ")
                .append(" LEFT JOIN m_stock_sku sku on spu.id = sku.stock_spu_id ")
                .append(" WHERE spu.deleted = 0  AND sku.deleted = 0 AND spu.merchant_no = :merchantNo ")
                .append(pageWhere(query, params));

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<StockSpuEntity> findPage(long merchantNo, StockSpuPageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" distinct spu.* ")
                .append(" FROM m_stock_spu spu ")
                .append(" LEFT JOIN m_stock_sku sku on spu.id = sku.stock_spu_id ")
                .append(" WHERE spu.deleted = 0  AND sku.deleted = 0 AND spu.merchant_no = :merchantNo ")
                .append(pageWhere(query, params))
                .append(" ORDER BY spu.create_time desc ");

        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<StockSpuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSpuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<StockSpuEntity> findList(long merchantNo, StockSpuQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" distinct spu.* ")
                .append(" FROM m_stock_spu spu ")
                .append(" LEFT JOIN m_stock_sku sku on spu.id = sku.stock_spu_id ")
                .append(" WHERE spu.deleted = 0 AND sku.deleted = 0 AND spu.merchant_no = :merchantNo ")
                .append(listWhere(query, params))
                .append(" ORDER BY spu.create_time desc ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<StockSpuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSpuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String pageWhere(StockSpuPageQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        if (query.getStoreNo() != null) {
            sb.append(" AND spu.store_no = :storeNo ");
            params.addValue("storeNo", query.getStoreNo());
        }
        if (!CollectionUtils.isEmpty(query.getSpuIds())) {
            sb.append(" AND spu.spu_id in ( :spuIds )");
            params.addValue("spuIds", query.getSpuIds());
        }
        if (StringUtils.isNotBlank(query.getSpuNo())) {
            sb.append(" AND ( spu.spu_no like :spuNo OR sku.sku_no like :spuNo) ");
            params.addValue("spuNo", "%" + query.getSpuNo() + "%");
        }
        if (StringUtils.isNotBlank(query.getSpuCode())) {
            sb.append(" AND ( spu.spu_code like :spuCode OR sku.sku_code like :spuCode)");
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

    private String listWhere(StockSpuQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" AND ( ")
                    .append(" spu.spu_code like :searchKey OR spu.spu_no like :searchKey OR spu.name like :searchKey ")
                    .append(" OR sku.sku_code like :searchKey OR sku.sku_no like :searchKey OR sku.sku_name like :searchKey ")
                    .append(" ) ");
            params.addValue("searchKey", "%" + query.getSearchKey() + "%");
        }
        if (query.getStoreNo() != null) {
            sb.append(" AND spu.store_no = :storeNo ");
            params.addValue("storeNo", query.getStoreNo());
        }
        if (!CollectionUtils.isEmpty(query.getSpuIds())) {
            sb.append(" AND spu.spu_id in ( :spuIds )");
            params.addValue("spuIds", query.getSpuIds());
        }
        if (!CollectionUtils.isEmpty(query.getCategoryIds())) {
            sb.append(" AND spu.category_id in ( :categoryIds )");
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
