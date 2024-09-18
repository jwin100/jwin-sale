package com.mammon.cashier.dao;

import com.mammon.cashier.domain.query.CashierRefundQuery;
import com.mammon.cashier.domain.entity.CashierRefundEntity;
import com.mammon.cashier.domain.query.CashierRefundSummaryQuery;
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
public class CashierRefundDao {


    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int save(CashierRefundEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_cashier_refund (")
                .append(" id, merchant_no, store_no, refund_no, order_id, order_no, type, category, subject, original_amount, adjust_amount, ")
                .append(" payable_amount, reality_amount, counted_total, integral, refund_mode, pay_type, status, message, send_sms, ")
                .append(" member_id, operation_id, remark, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :refundNo, :orderId, :orderNo, :type, :category, :subject, :originalAmount, :adjustAmount, ")
                .append(" :payableAmount, :realityAmount, :countedTotal, :integral, :refundMode, (:payType)::jsonb, :status, :message, :sendSms, ")
                .append(" :memberId, :operationId, :remark, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateRefundPayType(String id, int refundMode, String payType) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_refund ")
                .append(" set ")
                .append(" refund_mode = :refundMode, ")
                .append(" pay_type = ( :payType )::jsonb ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("refundMode", refundMode);
        params.addValue("payType", payType);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateRefundStatus(String id, int status, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_refund ")
                .append(" set ")
                .append(" status = :status, ")
                .append(" message = :message ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("message", message);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateRefundAmount(long merchantNo, String id, long realityAmount) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_refund ")
                .append(" set ")
                .append(" reality_amount = :realityAmount ")
                .append(" where merchant_no = :merchantNo ")
                .append(" AND id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("realityAmount", realityAmount);
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<CashierRefundEntity> findAllByOrderId(long merchantNo, long storeNo, String orderId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_refund ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND order_id = :orderId ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("orderId", orderId);

        String sql = sb.toString();

        RowMapper<CashierRefundEntity> rowMapper = new BeanPropertyRowMapper<>(CashierRefundEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public CashierRefundEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_refund ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<CashierRefundEntity> rowMapper = new BeanPropertyRowMapper<>(CashierRefundEntity.class);
        List<CashierRefundEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int countPage(long merchantNo, Long storeNo, String accountId, CashierRefundQuery dto) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_cashier_refund ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(orderPageWhere(storeNo, accountId, dto, params));

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<CashierRefundEntity> findPage(long merchantNo, Long storeNo, String accountId, CashierRefundQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_refund ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(orderPageWhere(storeNo, accountId, query, params))
                .append(" ORDER BY create_time DESC ");

        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<CashierRefundEntity> rowMapper = new BeanPropertyRowMapper<>(CashierRefundEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<CashierRefundEntity> summary(CashierRefundSummaryQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_refund ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", query.getMerchantNo());

        if (query.getStoreNo() != null) {
            sb.append(" AND store_no = :storeNo ");
            params.addValue("storeNo", query.getStoreNo());
        }

        if (query.getCategory() != null) {
            sb.append(" AND category = :category ");
            params.addValue("category", query.getCategory());
        }

        if (query.getStartDate() != null && query.getEndDate() != null) {
            sb.append(" AND create_time >= :startDate AND create_time < :endDate ");
            params.addValue("startDate", query.getStartDate());
            params.addValue("endDate", query.getEndDate());
        }

        if (StringUtils.isNotBlank(query.getMemberId())) {
            sb.append(" AND member_id = :memberId ");
            params.addValue("memberId", query.getMemberId());
        }

        if (query.getStatus() != null) {
            sb.append(" AND status = :status ");
            params.addValue("status", query.getStatus());
        }
        String sql = sb.toString();
        RowMapper<CashierRefundEntity> rowMapper = new BeanPropertyRowMapper<>(CashierRefundEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String orderPageWhere(Long storeNo, String accountId, CashierRefundQuery dto, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();

        if (storeNo != null) {
            sb.append(" AND store_no = :storeNo ");
            params.addValue("storeNo", storeNo);
        }

        if (StringUtils.isNotBlank(dto.getOrderNo())) {
            sb.append(" AND order_no LIKE :orderNo ");
            params.addValue("orderNo", "%" + dto.getOrderNo() + "%");
        }

        if (StringUtils.isNotBlank(dto.getRefundNo())) {
            sb.append(" AND refund_no LIKE :refundNo ");
            params.addValue("refundNo", "%" + dto.getRefundNo() + "%");
        }

        if (StringUtils.isNotBlank(dto.getSearchKey())) {
            sb.append(" AND subject like :searchKey ");
            params.addValue("searchKey", "%" + dto.getSearchKey() + "%");
        }

        if (dto.getPayType() != null) {
            sb.append(" AND id IN ( ")
                    .append(" SELECT id from ")
                    .append("   ( ")
                    .append("       select id, jsonb_array_elements(pay_type) as payTypes from m_cashier_refund as cashierRefundPayType ")
                    .append("       where cashierRefundPayType.merchant_no = merchant_no ")
                    .append("    ) as tb ")
                    .append(" WHERE payTypes ->> 'payCode' = :payType ")
                    .append(" ) ");
            params.addValue("payType", String.valueOf(dto.getPayType()));
        }

        if (StringUtils.isNotBlank(dto.getMemberId())) {
            sb.append(" AND member_id = :memberId ");
            params.addValue("memberId", dto.getMemberId());
        }

        if (dto.getOrderType() != null) {
            sb.append(" AND member_id = :orderType ");
            params.addValue("orderType", dto.getOrderType());
        }

        if (dto.getOrderStatus() != null) {
            sb.append(" AND status = :status ");
            params.addValue("status", dto.getOrderStatus());
        }

        if (dto.getRefundMode() != null) {
            sb.append(" AND refund_mode = :refundMode ");
            params.addValue("refundMode", dto.getRefundMode());
        }

        if (StringUtils.isNotBlank(dto.getOperationId())) {
            sb.append(" AND operation_id = :operationId ");
            params.addValue("operationId", dto.getOperationId());
        }

        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            sb.append(" AND create_time >= :startDate AND create_time < :endDate ");
            params.addValue("startDate", dto.getStartDate());
            params.addValue("endDate", dto.getEndDate().plusDays(1));
        }
        return sb.toString();
    }
}
