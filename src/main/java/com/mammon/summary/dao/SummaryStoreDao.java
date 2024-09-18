package com.mammon.summary.dao;

import com.mammon.summary.domain.entity.SummaryStoreEntity;
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
public class SummaryStoreDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SummaryStoreEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_summary_store (")
                .append(" id, merchant_no, store_no, summary_date, total_amount, cashier_amount, recharge_amount, ")
                .append(" counted_amount, service_amount, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :summaryDate, :totalAmount, :cashierAmount, :rechargeAmount, ")
                .append(" :countedAmount, :serviceAmount, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(SummaryStoreEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_summary_store ")
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

    public SummaryStoreEntity findByDate(long merchantNo, long storeNo, LocalDate summaryDate) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_summary_store ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ")
                .append(" AND summary_date = :summaryDate ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("summaryDate", summaryDate);

        String sql = sb.toString();

        RowMapper<SummaryStoreEntity> rowMapper = new BeanPropertyRowMapper<>(SummaryStoreEntity.class);
        List<SummaryStoreEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<SummaryStoreEntity> findListByDate(long merchantNo, long storeNo,
                                                   LocalDate summaryStartDate, LocalDate summaryEndDate) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_summary_store ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ")
                .append(" AND summary_date >= :summaryStartDate AND summary_date < :summaryEndDate ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("summaryStartDate", summaryStartDate);
        params.addValue("summaryEndDate", summaryEndDate);

        String sql = sb.toString();

        RowMapper<SummaryStoreEntity> rowMapper = new BeanPropertyRowMapper<>(SummaryStoreEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
