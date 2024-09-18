package com.mammon.office.order.dao;

import com.mammon.office.order.domain.entity.OfficeOrderEntity;
import com.mammon.office.order.domain.query.OfficeOrderQuery;
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
 * @date 2023-02-02 13:23:45
 */
@Repository
public class OfficeOrderDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(OfficeOrderEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_office_order (")
                .append(" id, merchant_no, store_no, account_id, order_no, subject, payable_amount, ")
                .append(" config_id, pay_type, status, pay_message, source, remark, ")
                .append(" pay_time,  trade_no, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :accountId, :orderNo, :subject, :payableAmount, ")
                .append(" :configId, :payType, :status, :payMessage, :source, :remark, ")
                .append(" :payTime,  :tradeNo, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editPayTypeById(String id, String configId, int payType) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_office_order ")
                .append(" set ")
                .append(" pay_type = :payType, ")
                .append(" config_id = :configId ")
                .append(" where id = :id ");

        String sql = sb.toString();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("payType", payType);
        params.addValue("configId", configId);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatusById(String id, int originalStatus, int status) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_office_order ")
                .append(" set ")
                .append(" status            = :status ")
                .append(" where id          = :id ")
                .append(" and status        = :originalStatus ");

        String sql = sb.toString();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("id", id);
        params.addValue("originalStatus", originalStatus);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editPayStatusById(String id, int originalStatus, String tradeNo,
                                 int status, String payMessage, LocalDateTime payTime) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_office_order ")
                .append(" set ")
                .append(" status            = :status, ")
                .append(" pay_message       = :payMessage, ")
                .append(" pay_time          = :payTime, ")
                .append(" trade_no          = :tradeNo ")
                .append(" where id          = :id ")
                .append(" and status        = :originalStatus ");

        String sql = sb.toString();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("payMessage", payMessage);
        params.addValue("payTime", payTime);
        params.addValue("tradeNo", tradeNo);
        params.addValue("id", id);
        params.addValue("originalStatus", originalStatus);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public OfficeOrderEntity findById(long merchantNo, String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_order ")
                .append(" WHERE id = :id ");
        if (merchantNo > 0) {
            sb.append(" AND merchant_no = :merchantNo ");
            params.addValue("merchantNo", merchantNo);
        }
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<OfficeOrderEntity> rowMapper = new BeanPropertyRowMapper<>(OfficeOrderEntity.class);
        List<OfficeOrderEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public OfficeOrderEntity findByOrderNo(long merchantNo, String orderNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_order ")
                .append(" WHERE order_no = :orderNo ");
        if (merchantNo > 0) {
            sb.append(" AND merchant_no = :merchantNo ");
            params.addValue("merchantNo", merchantNo);
        }
        params.addValue("orderNo", orderNo);

        String sql = sb.toString();

        RowMapper<OfficeOrderEntity> rowMapper = new BeanPropertyRowMapper<>(OfficeOrderEntity.class);
        List<OfficeOrderEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int countPage(long merchantNo, OfficeOrderQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_office_order ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);
        if (StringUtils.isNotBlank(query.getOrderNo())) {
            sb.append(" AND  order_no = :orderNo ");
            params.addValue("orderNo", query.getOrderNo());
        }

        if (query.getStatus() != null) {
            sb.append(" AND  status = :status ");
            params.addValue("status", query.getStatus());
        }

        if (query.getPayType() != null) {
            sb.append(" AND  pay_type = :payType ");
            params.addValue("payType", query.getPayType());
        }

        if (query.getStartCreateTime() != null && query.getEndCreateTime() != null) {
            sb.append(" AND create_time >= :startCreateTime AND create_time < :endCreateTime ");
            params.addValue("startCreateTime", query.getStartCreateTime());
            params.addValue("endCreateTime", query.getEndCreateTime().plusDays(1));
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<OfficeOrderEntity> findPage(long merchantNo, OfficeOrderQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_order ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(query.getOrderNo())) {
            sb.append(" AND  order_no = :orderNo ");
            params.addValue("orderNo", query.getOrderNo());
        }

        if (query.getStatus() != null) {
            sb.append(" AND  status = :status ");
            params.addValue("status", query.getStatus());
        }

        if (query.getPayType() != null) {
            sb.append(" AND  pay_type = :payType ");
            params.addValue("payType", query.getPayType());
        }

        if (query.getStartCreateTime() != null && query.getEndCreateTime() != null) {
            sb.append(" AND create_time >= :startCreateTime AND create_time < :endCreateTime ");
            params.addValue("startCreateTime", query.getStartCreateTime());
            params.addValue("endCreateTime", query.getEndCreateTime().plusDays(1));
        }

        sb
                .append(" ORDER BY create_time DESC ")
                .append(" limit :limit offset :offset ");
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<OfficeOrderEntity> rowMapper = new BeanPropertyRowMapper<>(OfficeOrderEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
