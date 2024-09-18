package com.mammon.clerk.dao;

import cn.hutool.core.collection.CollUtil;
import com.mammon.clerk.domain.entity.CommissionEntity;
import com.mammon.clerk.domain.query.CommissionListQuery;
import com.mammon.clerk.domain.query.CommissionQuery;
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
 * @since 2024/4/7 16:45
 */
@Repository
public class CommissionDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(CommissionEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_clerk_commission (")
                .append(" id, merchant_no, store_no, commission_time, account_id, commission_rule_id, ")
                .append(" total_amount, cashier_amount, recharge_amount, counted_amount, service_amount, ")
                .append(" create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :commissionTime, :accountId, :commissionRuleId, ")
                .append(" :totalAmount, :cashierAmount, :rechargeAmount, :countedAmount, :serviceAmount, ")
                .append(" :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(CommissionEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_commission ")
                .append(" set ")
                .append(" commission_rule_id    = :commissionRuleId, ")
                .append(" total_amount          = :totalAmount, ")
                .append(" cashier_amount        = :cashierAmount, ")
                .append(" recharge_amount       = :rechargeAmount, ")
                .append(" counted_amount        = :countedAmount, ")
                .append(" service_amount        = :serviceAmount, ")
                .append(" update_time           = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public CommissionEntity findByCommissionDate(long merchantNo, long storeNo, String accountId, LocalDate commissionDate) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_commission ")
                .append(" WHERE merchant_no = :merchantNo AND store_no = :storeNo ")
                .append(" AND account_id = :accountId ")
                .append(" AND commission_time  = :commissionDate ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("accountId", accountId);
        params.addValue("commissionDate", commissionDate);

        String sql = sb.toString();

        RowMapper<CommissionEntity> rowMapper = new BeanPropertyRowMapper<>(CommissionEntity.class);
        List<CommissionEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<CommissionEntity> findList(long merchantNo, CommissionListQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_commission ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        if (CollUtil.isNotEmpty(query.getAccountIds())) {
            sb.append(" AND account_id in ( :accountIds )");
            params.addValue("accountIds", query.getAccountIds());
        }
        if (query.getCommissionStartTime() != null && query.getCommissionEndTime() != null) {
            sb.append(" AND commission_time >= :startTime AND commission_time <= :endTime ");
            params.addValue("startTime", query.getCommissionStartTime());
            params.addValue("endTime", query.getCommissionEndTime());
        }

        String sql = sb.toString();

        RowMapper<CommissionEntity> rowMapper = new BeanPropertyRowMapper<>(CommissionEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
