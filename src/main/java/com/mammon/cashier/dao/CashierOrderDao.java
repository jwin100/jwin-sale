package com.mammon.cashier.dao;

import com.mammon.cashier.domain.query.CashierOrderPageQuery;
import com.mammon.cashier.domain.entity.CashierOrderEntity;
import com.mammon.cashier.domain.query.CashierOrderSummaryQuery;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
public class CashierOrderDao {


    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CashierOrderEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_cashier_order (")
                .append(" id, merchant_no, store_no, order_no, customer_no, category, type, subject, original_amount, ignore_type, ")
                .append(" ignore_amount, discount, discount_amount, preferential_amount, collect_amount, ")
                .append(" adjust_amount, payable_amount, reality_amount, counted_total, integral, pay_type, status, message, refund_mark, ")
                .append(" refund_amount, refund_integral,  member_id, send_sms, operation_id, service_account_ids, source, remark, ")
                .append(" cashier_time, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :orderNo, :customerNo, :category, :type, :subject, :originalAmount, :ignoreType, ")
                .append(" :ignoreAmount, :discount, :discountAmount, :preferentialAmount, :collectAmount, ")
                .append(" :adjustAmount, :payableAmount, :realityAmount, :countedTotal, :integral, (:payType)::jsonb, :status, :message, :refundMark, ")
                .append(" :refundAmount, :refundIntegral, :memberId, :sendSms, :operationId,(:serviceAccountIds)::jsonb, :source, :remark, ")
                .append(" :cashierTime, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateOrderPayType(String id, String payType) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_order ")
                .append(" set ")
                .append(" pay_type = (:payType)::jsonb ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("payType", payType);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateOrderStatus(String id, int status, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_order ")
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

    public int updateOrderAmount(String id, long realityAmount) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_order ")
                .append(" set ")
                .append(" reality_amount = :realityAmount ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("realityAmount", realityAmount);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateOrderRefund(long merchantNo, String id, int refundMark, long refundAmount, long refundIntegral) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_order ")
                .append(" set ")
                .append(" refund_mark = :refundMark, ")
                .append(" refund_amount = :refundAmount, ")
                .append(" refund_integral = :refundIntegral ")
                .append(" where merchant_no = :merchantNo ")
                .append(" AND id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("refundMark", refundMark);
        params.addValue("refundAmount", refundAmount);
        params.addValue("refundIntegral", refundIntegral);
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(long merchantNo, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_cashier_order ")
                .append(" set ")
                .append(" deleted = 1 ")
                .append(" where merchant_no = :merchantNo ")
                .append(" AND id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("merchantNo", merchantNo);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public CashierOrderEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_order ")
                .append(" WHERE id = :id AND deleted = 0 ");
        params.addValue("id", id);
        String sql = sb.toString();

        RowMapper<CashierOrderEntity> rowMapper = new BeanPropertyRowMapper<>(CashierOrderEntity.class);
        List<CashierOrderEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<CashierOrderEntity> findByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_order ")
                .append(" WHERE id in ( :ids ) AND deleted = 0 ");
        params.addValue("ids", ids);
        String sql = sb.toString();

        RowMapper<CashierOrderEntity> rowMapper = new BeanPropertyRowMapper<>(CashierOrderEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int countPage(long merchantNo, Long storeNo, String accountId, CashierOrderPageQuery dto) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_cashier_order ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(orderPageWhere(storeNo, dto, params));

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<CashierOrderEntity> findPage(long merchantNo, Long storeNo, String accountId, CashierOrderPageQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_order ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(orderPageWhere(storeNo, query, params))
                .append(" ORDER BY create_time DESC ");

        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<CashierOrderEntity> rowMapper = new BeanPropertyRowMapper<>(CashierOrderEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String orderPageWhere(Long storeNo, CashierOrderPageQuery dto, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        sb.append(" AND deleted = 0 ");

        if (storeNo != null) {
            sb.append(" AND store_no = :storeNo ");
            params.addValue("storeNo", storeNo);
        }

        if (StringUtils.isNotBlank(dto.getOrderNo())) {
            sb.append(" AND order_no LIKE :orderNo ");
            params.addValue("orderNo", "%" + dto.getOrderNo() + "%");
        }

        if (StringUtils.isNotBlank(dto.getSearchKey())) {
            sb.append(" AND subject like :spuSearchLike ");
            params.addValue("spuSearchLike", "%" + dto.getSearchKey() + "%");
        }

        if (dto.getCategory() != null) {
            sb.append(" AND category = :category ");
            params.addValue("category", dto.getCategory());
        }

        if (dto.getPayType() != null) {
            sb.append(" AND id IN  (")
                    .append(" SELECT id from ")
                    .append("   ( ")
                    .append("       select id, jsonb_array_elements(pay_type) as payTypes from m_cashier_order as cashierOrderPayType ")
                    .append("       where cashierOrderPayType.merchant_no = merchant_no ")
                    .append("    ) as tb ")
                    .append(" where payTypes ->> 'payCode' = :payType ")
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

        if (dto.getStatus() != null) {
            sb.append(" AND status = :status ");
            params.addValue("status", dto.getStatus());
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

    public List<CashierOrderEntity> summary(CashierOrderSummaryQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_order ")
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
            sb.append(" AND cashier_time >= :startDate AND cashier_time < :endDate ");
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
        if (StringUtils.isNotBlank(query.getServiceAccountId())) {
            List<String> serviceAccountIds = Collections.singletonList(query.getServiceAccountId());
            String paramsValue = "'" + JsonUtil.toJSONString(serviceAccountIds) + "'::jsonb";
            sb.append(" AND service_account_ids::jsonb @> ").append(paramsValue);
            // jsonb查询参数不能写到params.addValue()里边，估计是jdbc解析@>这种符号的问题
        }
        String sql = sb.toString();
        RowMapper<CashierOrderEntity> rowMapper = new BeanPropertyRowMapper<>(CashierOrderEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    /**
     * 获取会员最新下单记录
     *
     * @param memberId
     * @return
     */
    public CashierOrderEntity findLastByMemberId(long merchantNo, String memberId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_order ")
                .append(" WHERE merchant_no = :merchantNo AND member_id = :memberId ")
                .append(" order by cashier_time desc ")
                .append(" limit 1 ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("memberId", memberId);

        String sql = sb.toString();
        RowMapper<CashierOrderEntity> rowMapper = new BeanPropertyRowMapper<>(CashierOrderEntity.class);
        List<CashierOrderEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
