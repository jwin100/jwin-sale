package com.mammon.summary.dao;

import com.mammon.summary.domain.entity.SummaryAccountEntity;
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
 * @since 2024/4/22 14:54
 */
@Repository
public class SummaryAccountDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SummaryAccountEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_summary_account (")
                .append(" id, merchant_no, store_no, summary_date, account_id, total_amount, cashier_amount, ")
                .append(" recharge_amount, counted_amount, service_amount, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :summaryDate, :accountId, :totalAmount, :cashierAmount, ")
                .append(" :rechargeAmount, :countedAmount, :serviceAmount, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(SummaryAccountEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_summary_account ")
                .append(" set ")
                .append(" total_amount = :totalAmount, ")
                .append(" cashier_amount = :cashierAmount, ")
                .append(" recharge_amount = :rechargeAmount, ")
                .append(" counted_amount = :countedAmount, ")
                .append(" service_amount = :serviceAmount, ")
                .append(" update_time = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("totalAmount", entity.getTotalAmount());
        params.addValue("cashierAmount", entity.getCashierAmount());
        params.addValue("rechargeAmount", entity.getRechargeAmount());
        params.addValue("countedAmount", entity.getCountedAmount());
        params.addValue("serviceAmount", entity.getServiceAmount());
        params.addValue("updateTime", entity.getUpdateTime());
        params.addValue("id", entity.getId());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public SummaryAccountEntity findByDate(long merchantNo, String accountId, LocalDate summaryDate) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_summary_account ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND account_id = :accountId ")
                .append(" AND summary_date = :summaryDate ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("accountId", accountId);
        params.addValue("summaryDate", summaryDate);

        String sql = sb.toString();

        RowMapper<SummaryAccountEntity> rowMapper = new BeanPropertyRowMapper<>(SummaryAccountEntity.class);
        List<SummaryAccountEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<SummaryAccountEntity> findListByDate(List<String> accountIds,
                                                     LocalDate summaryStartDate, LocalDate summaryEndDate) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_summary_account ")
                .append(" WHERE account_id in ( :accountIds )")
                .append(" AND summary_date >= :summaryStartDate AND summary_date < :summaryEndDate ");

        params.addValue("accountIds", accountIds);
        params.addValue("summaryStartDate", summaryStartDate);
        params.addValue("summaryEndDate", summaryEndDate);

        String sql = sb.toString();

        RowMapper<SummaryAccountEntity> rowMapper = new BeanPropertyRowMapper<>(SummaryAccountEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
