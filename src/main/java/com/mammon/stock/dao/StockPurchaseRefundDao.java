package com.mammon.stock.dao;

import com.mammon.stock.domain.query.StockPurchaseRefundQuery;
import com.mammon.stock.domain.entity.StockPurchaseRefundEntity;
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
public class StockPurchaseRefundDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(StockPurchaseRefundEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_stock_purchase_refund (")
                .append(" id, merchant_no, store_no, refund_no, purchase_id, status, err_desc, operation_id, remark, reason_id, ")
                .append(" create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :refundNo, :purchaseId, :status, :errDesc, :operationId, :remark, :reasonId, ")
                .append(" :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(StockPurchaseRefundEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_purchase_refund ")
                .append(" set ")
                .append(" reason_id = :reasonId, ")
                .append(" remark = :remark, ")
                .append(" update_time = :updateTime ")
                .append(" where merchant_no = :merchantNo ")
                .append(" AND id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateOrderStatus(long merchantNo, String id, int status, String errDesc) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_stock_purchase_refund ")
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

    public StockPurchaseRefundEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_purchase_refund ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND id = :id ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<StockPurchaseRefundEntity> rowMapper = new BeanPropertyRowMapper<>(StockPurchaseRefundEntity.class);
        List<StockPurchaseRefundEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int count(long merchantNo, long storeNo, String accountId, StockPurchaseRefundQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_stock_purchase_refund ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params));

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<StockPurchaseRefundEntity> page(long merchantNo, long storeNo, String accountId, StockPurchaseRefundQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_stock_purchase_refund ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(pageWhere(query, params));

        sb.append(" ORDER BY create_time DESC ");
        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<StockPurchaseRefundEntity> rowMapper = new BeanPropertyRowMapper<>(StockPurchaseRefundEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String pageWhere(StockPurchaseRefundQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        if (query.getStoreNo() != null) {
            sb.append(" AND store_no = :storeNo");
            params.addValue("storeNo", query.getStoreNo());
        }
        if (StringUtils.isNotBlank(query.getRefundNo())) {
            sb.append(" AND refund_no like :refundNo");
            params.addValue("refundNo", "%" + query.getRefundNo() + "%");
        }
        if (query.getStatus() != null) {
            sb.append(" AND status = :status");
            params.addValue("status", query.getStatus());
        }
        return sb.toString();
    }
}
