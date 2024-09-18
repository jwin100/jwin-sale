package com.mammon.cashier.dao;

import com.mammon.cashier.domain.query.CashierHangQuery;
import com.mammon.enums.CommonStatus;
import com.mammon.cashier.domain.entity.CashierHangEntity;
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

@Repository
public class CashierHangDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CashierHangEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_cashier_hang (")
                .append(" id, merchant_no, store_no, name, hang_no, detail, status, remark, operation_id,  create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :name, :hangNo, (:detail)::jsonb, :status, :remark, :operationId, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int updateHangStatus(long merchantNo, long storeNo, String accountId, String hangNo,
                                String name, String detail, int status, String remark) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_cashier_hang ")
                .append(" set ")
                .append(" status = :status, ");

        if (StringUtils.isNotBlank(name)) {
            sb.append(" name = :name, ");
            params.addValue("name", name);
        }
        if (StringUtils.isNotBlank(detail)) {
            sb.append(" detail = (:detail)::jsonb, ");
            params.addValue("detail", detail);
        }
        if (StringUtils.isNotBlank(remark)) {
            sb.append(" remark = :remark, ");
            params.addValue("remark", remark);
        }

        sb.append(" update_time = :updateTime ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ")
                .append(" AND operation_id = :accountId ")
                .append(" AND hang_no = :hangNo ");

        String sql = sb.toString();

        params.addValue("status", status);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("accountId", accountId);
        params.addValue("hangNo", hangNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ")
                .append(" FROM  m_cashier_hang ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);
        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public CashierHangEntity findById(long merchantNo, long storeNo, String accountId, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_hang ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ")
                .append(" AND operation_id = :accountId ")
                .append(" AND id = :id ");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("accountId", accountId);
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<CashierHangEntity> rowMapper = new BeanPropertyRowMapper<>(CashierHangEntity.class);
        List<CashierHangEntity> hangs = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (hangs.isEmpty()) {
            return null;
        }
        return hangs.get(0);
    }

    public List<CashierHangEntity> findAllByAccount(long merchantNo, long storeNo, String accountId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_hang ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ")
                .append(" AND operation_id = :accountId ")
                .append(" AND status = :status ")
                .append(" ORDER BY update_time DESC ");

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("status", CommonStatus.ENABLED.getCode());
        params.addValue("accountId", accountId);

        String sql = sb.toString();

        RowMapper<CashierHangEntity> rowMapper = new BeanPropertyRowMapper<>(CashierHangEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int count(long merchantNo, long storeNo, String accountId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_cashier_hang ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ")
                .append(" AND status = :status ");

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (StringUtils.isNotBlank(accountId)) {
            sb.append(" AND operation_id = :accountId ");
            params.addValue("accountId", accountId);
        }

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("status", CommonStatus.ENABLED.getCode());

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<CashierHangEntity> page(long merchantNo, long storeNo, String accountId, CashierHangQuery query) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_cashier_hang ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ")
                .append(" AND status = :status ")
                .append(" ORDER BY update_time DESC ");

        MapSqlParameterSource params = new MapSqlParameterSource();
        if (StringUtils.isNotBlank(accountId)) {
            sb.append(" AND operation_id = :accountId ");
            params.addValue("accountId", accountId);
        }

        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("status", CommonStatus.ENABLED.getCode());
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<CashierHangEntity> rowMapper = new BeanPropertyRowMapper<>(CashierHangEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
