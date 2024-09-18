package com.mammon.member.dao;

import com.mammon.member.domain.entity.MemberEntity;
import com.mammon.member.domain.entity.MemberRechargeOrderEntity;
import com.mammon.member.domain.query.MemberRechargeOrderSummaryQuery;
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
 * 会员储值记录
 *
 * @author dcl
 * @since 2024/2/21 15:45
 */
@Repository
public class MemberRechargeOrderDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 创建、修改、统计
    public int save(MemberRechargeOrderEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_member_recharge_order (")
                .append(" id, merchant_no, store_no, member_id, order_no, rule_id, quantity, recharge_amount, ")
                .append(" give_amount, receives_amount, give_integral, refunded, recharge_time, refund_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :memberId, :orderNo, :ruleId, :quantity, :rechargeAmount, ")
                .append(" :giveAmount, :receivesAmount, :giveIntegral, :refunded, :rechargeTime, :refundTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editRefund(long merchantNo, String orderNo, LocalDateTime refundTime) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_member_recharge_order ")
                .append(" set ")
                .append(" refunded          = :refunded, ")
                .append(" refund_time       = :refundTime ")
                .append(" where merchant_no = :merchantNo AND order_no = :orderNo ");

        String sql = sb.toString();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("refunded", 1);
        params.addValue("refundTime", refundTime);
        params.addValue("merchantNo", merchantNo);
        params.addValue("orderNo", orderNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<MemberRechargeOrderEntity> summaryList(MemberRechargeOrderSummaryQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_recharge_order ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", query.getMerchantNo());

        if (query.getStoreNo() != null) {
            sb.append(" AND store_no = :storeNo ");
            params.addValue("storeNo", query.getStoreNo());
        }
        if (query.getStartDate() != null && query.getEndDate() != null) {
            sb.append(" AND recharge_time >= :startDate AND recharge_time < :endDate ");
            params.addValue("startDate", query.getStartDate());
            params.addValue("endDate", query.getEndDate());
        }

        if (query.getRefunded() != null) {
            sb.append(" AND refunded = :refunded ");
            params.addValue("refunded", query.getRefunded());
        }

        String sql = sb.toString();

        RowMapper<MemberRechargeOrderEntity> rowMapper = new BeanPropertyRowMapper<>(MemberRechargeOrderEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
