package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockSkuEntity;
import com.mammon.stock.domain.query.StockSkuPageQuery;
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
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class StockSkuDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockSkuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_sku (")
                .append(" id, stock_spu_id, merchant_no, store_no, spu_id, sku_id, sku_code, sku_no, sku_name, ")
                .append(" purchase_amount, reference_amount, sku_weight, sell_stock, status, create_time, update_time, join_spec ")
                .append(" ) VALUES ( ")
                .append(" :id, :stockSpuId, :merchantNo, :storeNo, :spuId, :skuId, :skuCode, :skuNo, :skuName, ")
                .append(" :purchaseAmount, :referenceAmount, :skuWeight, :sellStock, :status, :createTime, :updateTime, :joinSpec ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(StockSkuEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_sku ")
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

    public int editSellStockIn(long merchantNo, long storeNo, String skuId, long sellStock) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_sku ")
                .append(" set ")
                .append(" sell_stock = sell_stock + :sellStock, ")
                .append(" update_time = :updateTime ")
                .append(" where sku_id = :skuId ")
                .append(" AND merchant_no = :merchantNo AND store_no = :storeNo ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("sellStock", sellStock);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("skuId", skuId);
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editSellStockOut(long merchantNo, long storeNo, String skuId, long sellStock) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_sku ")
                .append(" set ")
                .append(" sell_stock = sell_stock - :sellStock, ")
                .append(" update_time = :updateTime ")
                .append(" where sku_id = :skuId ")
                .append(" AND merchant_no = :merchantNo AND store_no = :storeNo AND sell_stock >= :sellStock ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("sellStock", sellStock);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("skuId", skuId);
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deletedBySkuId(String skuId) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_sku set deleted = 1 ")
                .append(" where sku_id = :skuId ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("skuId", skuId);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int countPage(long merchantNo, StockSkuPageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(sku.id) ")
                .append(" FROM m_stock_sku sku ")
                .append(" LEFT JOIN m_stock_spu spu on spu.id = sku.stock_spu_id ")
                .append(" WHERE spu.deleted = 0 AND sku.deleted = 0 AND spu.merchant_no = :merchantNo AND spu.store_no = :storeNo ")
                .append(pageWhere(query, params));

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", query.getStoreNo());

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<StockSkuEntity> findPage(long merchantNo, StockSkuPageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" sku.* ")
                .append(" FROM m_stock_sku sku ")
                .append(" LEFT JOIN m_stock_spu spu on spu.id = sku.stock_spu_id ")
                .append(" WHERE spu.deleted = 0 AND sku.deleted = 0 AND spu.merchant_no = :merchantNo AND spu.store_no = :storeNo ")
                .append(pageWhere(query, params))
                .append(" ORDER BY sku.create_time desc ");

        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", query.getStoreNo());
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<StockSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<StockSkuEntity> findListByStockSpuId(String stockSpuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_sku ")
                .append(" WHERE stock_spu_id = :stockSpuId  AND deleted = 0");
        params.addValue("stockSpuId", stockSpuId);

        String sql = sb.toString();

        RowMapper<StockSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<StockSkuEntity> findListByStockSpuIds(List<String> stockSpuIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_sku ")
                .append(" WHERE stock_spu_id in ( :stockSpuIds )  AND deleted = 0 ");
        params.addValue("stockSpuIds", stockSpuIds);

        String sql = sb.toString();

        RowMapper<StockSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public StockSkuEntity findBySkuNo(long merchantNo, long storeNo, String skuNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_sku ")
                .append(" WHERE merchant_no = :merchantNo AND store_no = :storeNo ")
                .append(" AND sku_no = :skuNo ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("skuNo", skuNo);

        String sql = sb.toString();

        RowMapper<StockSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSkuEntity.class);
        List<StockSkuEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public StockSkuEntity findBySkuId(long merchantNo, long storeNo, String skuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_sku ")
                .append(" WHERE merchant_no = :merchantNo AND store_no = :storeNo ")
                .append(" AND sku_id = :skuId  AND deleted = 0 ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("skuId", skuId);

        String sql = sb.toString();

        RowMapper<StockSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSkuEntity.class);
        List<StockSkuEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<StockSkuEntity> findListBySkuId(String skuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_sku ")
                .append(" WHERE sku_id = :skuId AND deleted = 0");
        params.addValue("skuId", skuId);

        String sql = sb.toString();

        RowMapper<StockSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<StockSkuEntity> findListBySkuIds(long merchantNo, long storeNo, List<String> skuIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_sku ")
                .append(" WHERE merchant_no = :merchantNo AND store_no = :storeNo ")
                .append(" AND sku_id in ( :skuIds )   AND deleted = 0");
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("skuIds", skuIds);

        String sql = sb.toString();

        RowMapper<StockSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<StockSkuEntity> findListBySpuId(long merchantNo, Long storeNo, String spuId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_sku ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND spu_id = :spuId   AND deleted = 0");
        if(storeNo!=null){
            sb.append(" AND store_no = :storeNo ");
            params.addValue("storeNo", storeNo);
        }
        params.addValue("merchantNo", merchantNo);
        params.addValue("spuId", spuId);

        String sql = sb.toString();

        RowMapper<StockSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<StockSkuEntity> findListBySpuIds(long merchantNo, long storeNo, List<String> spuIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_sku ")
                .append(" WHERE merchant_no = :merchantNo AND store_no = :storeNo ")
                .append(" AND spu_id in ( :spuIds )   AND deleted = 0");
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("spuIds", spuIds);

        String sql = sb.toString();

        RowMapper<StockSkuEntity> rowMapper = new BeanPropertyRowMapper<>(StockSkuEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String pageWhere(StockSkuPageQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" AND ( ")
                    .append(" spu.spu_code like :searchKey OR spu.spu_no like :searchKey OR spu.name like :searchKey ")
                    .append(" OR sku.sku_code like :searchKey OR sku.sku_no like :searchKey OR sku.sku_name like :searchKey ")
                    .append(" ) ");
            params.addValue("searchKey", "%" + query.getSearchKey() + "%");
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
