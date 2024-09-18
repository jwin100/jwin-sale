package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockInventoryEntity;
import com.mammon.stock.domain.query.StockInventoryQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dcl
 * @since 2024/4/1 16:28
 */
@Service
public class StockInventoryDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockInventoryEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_inventory (")
                .append(" id, merchant_no, store_no, inventory_no, range, categories, inventory_start_time, ")
                .append(" inventory_end_time, product_num, error_product_num, operation_id, remark, status, ")
                .append(" create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :inventoryNo, :range, (:categories)::jsonb, :inventoryStartTime, ")
                .append(" :inventoryEndTime, :productNum, :errorProductNum, :operationId, :remark, :status, ")
                .append(" :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(StockInventoryEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_inventory ")
                .append(" set ")
                .append(" inventory_end_time    = :inventoryEndTime, ")
                .append(" product_num           = :productNum, ")
                .append(" error_product_num     = :errorProductNum, ")
                .append(" status                = :status, ")
                .append(" update_time           = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(String id, int status) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_inventory ")
                .append(" set ")
                .append(" status                = :status, ")
                .append(" update_time           = :updateTime ")
                .append(" where id = :id ");

        params.addValue("status", status);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("id", id);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public StockInventoryEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_inventory ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<StockInventoryEntity> rowMapper = new BeanPropertyRowMapper<>(StockInventoryEntity.class);
        List<StockInventoryEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int countPage(long merchantNo, StockInventoryQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_stock_inventory ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params));

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<StockInventoryEntity> findPage(long merchantNo, StockInventoryQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_inventory ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params))
                .append(" ORDER BY create_time DESC ");

        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<StockInventoryEntity> rowMapper = new BeanPropertyRowMapper<>(StockInventoryEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String pageWhere(StockInventoryQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();

        if (query.getStoreNo() != null) {
            sb.append(" AND store_no = :storeNo ");
            params.addValue("storeNo", query.getStoreNo());
        }

        if (StringUtils.isNotBlank(query.getInventoryNo())) {
            sb.append(" AND inventory_no LIKE :inventoryNo ");
            params.addValue("inventoryNo", "%" + query.getInventoryNo() + "%");
        }

        return sb.toString();
    }
}
