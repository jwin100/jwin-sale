package com.mammon.stock.dao;

import com.mammon.stock.domain.entity.StockPurchaseOrderEntity;
import com.mammon.stock.domain.enums.PurchaseOrderStatus;
import com.mammon.stock.domain.query.StockPurchaseOrderQuery;
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
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockPurchaseOrderDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockPurchaseOrderEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_purchase_order (")
                .append(" id, merchant_no, store_no, purchase_no, purchase_store_no, status, err_desc, ")
                .append(" refund_mark, operation_id, delivery_time, remark, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :purchaseNo, :purchaseStoreNo, :status, :errDesc, ")
                .append(" :refundMark, :operationId, :deliveryTime, :remark, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(long merchantNo, String id, long purchaseStoreNo, LocalDateTime deliveryTime, String remark) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_purchase_order ")
                .append(" set ")
                .append(" purchase_store_no = :purchaseStoreNo, ")
                .append(" delivery_time = :deliveryTime, ")
                .append(" remark = :remark, ")
                .append(" update_time = :updateTime ")
                .append(" where merchant_no = :merchantNo ")
                .append(" AND id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("purchaseStoreNo", purchaseStoreNo);
        params.addValue("deliveryTime", deliveryTime);
        params.addValue("remark", remark);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateOrderStatus(long merchantNo, String id, int status, String errDesc) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_purchase_order ")
                .append(" set ")
                .append(" status = :status, ")
                .append(" err_desc = :errDesc ")
                .append(" where merchant_no = :merchantNo ")
                .append(" AND id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("errDesc", errDesc);
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateOrderRefund(long merchantNo, String id, int refundMark) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_purchase_order ")
                .append(" set ")
                .append(" refund_mark = :refundMark ")
                .append(" where merchant_no = :merchantNo ")
                .append(" AND id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("refundMark", refundMark);
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public StockPurchaseOrderEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_purchase_order ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND id = :id ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<StockPurchaseOrderEntity> rowMapper = new BeanPropertyRowMapper<>(StockPurchaseOrderEntity.class);
        List<StockPurchaseOrderEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<StockPurchaseOrderEntity> list(long merchantNo, long storeNo, String accountId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_purchase_order ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND status in ( :status ) ")
                .append(" ORDER BY create_time DESC ");

        params.addValue("merchantNo", merchantNo);
        List<Integer> statusList = new ArrayList<Integer>() {{
            add(PurchaseOrderStatus.SOME_WARE_HOUSE.getCode());
            add(PurchaseOrderStatus.WARE_HOUSED.getCode());
        }};
        params.addValue("status", statusList);

        String sql = sb.toString();

        RowMapper<StockPurchaseOrderEntity> rowMapper = new BeanPropertyRowMapper<>(StockPurchaseOrderEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int count(long merchantNo, long storeNo, String accountId, StockPurchaseOrderQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_stock_purchase_order ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params));

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<StockPurchaseOrderEntity> page(long merchantNo, long storeNo, String accountId, StockPurchaseOrderQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_purchase_order ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params));

        sb.append(" ORDER BY create_time DESC ");
        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<StockPurchaseOrderEntity> rowMapper = new BeanPropertyRowMapper<>(StockPurchaseOrderEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String pageWhere(StockPurchaseOrderQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        if (query.getStoreNo() != null) {
            sb.append(" AND purchase_store_no = :storeNo");
            params.addValue("storeNo", query.getStoreNo());
        }
        if (StringUtils.isNotBlank(query.getPurchaseNo())) {
            sb.append(" AND purchase_no like :purchaseNo");
            params.addValue("purchaseNo", "%" + query.getPurchaseNo() + "%");
        }
        if (query.getStatus() != null) {
            sb.append(" AND status = :status");
            params.addValue("status", query.getStatus());
        }
        return sb.toString();
    }
}
