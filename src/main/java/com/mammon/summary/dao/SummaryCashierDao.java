package com.mammon.summary.dao;

import com.mammon.summary.domain.entity.SummaryCashierEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author dcl
 * @since 2024/2/29 10:07
 */
@Repository
public class SummaryCashierDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SummaryCashierEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_summary_cashier (")
                .append(" id, merchant_no, store_no, summary_date, cashier_total, cashier_amount, refund_amount, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :summaryDate, :cashierTotal, :cashierAmount, :refundAmount, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(SummaryCashierEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_summary_cashier ")
                .append(" set ")
                .append(" cashier_total = :cashierTotal, ")
                .append(" cashier_amount = :cashierAmount, ")
                .append(" refund_amount = :refundAmount ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("cashierTotal", entity.getCashierTotal());
        params.addValue("cashierAmount", entity.getCashierAmount());
        params.addValue("refundAmount", entity.getRefundAmount());
        params.addValue("id", entity.getId());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public SummaryCashierEntity findByDate(long merchantNo, long storeNo, LocalDate summaryDate) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_summary_cashier ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ")
                .append(" AND summary_date = :summaryDate ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("summaryDate", summaryDate);

        String sql = sb.toString();

        RowMapper<SummaryCashierEntity> rowMapper = new BeanPropertyRowMapper<>(SummaryCashierEntity.class);
        List<SummaryCashierEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<SummaryCashierEntity> findListByDate(long merchantNo, long storeNo, LocalDate summaryStartDate, LocalDate summaryEndDate) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_summary_cashier ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ")
                .append(" AND summary_date >= :summaryStartDate AND summary_date < :summaryEndDate ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("summaryStartDate", summaryStartDate);
        params.addValue("summaryEndDate", summaryEndDate);

        String sql = sb.toString();

        RowMapper<SummaryCashierEntity> rowMapper = new BeanPropertyRowMapper<>(SummaryCashierEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
