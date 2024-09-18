package com.mammon.market.dao;

import com.mammon.common.PageQuery;
import com.mammon.market.domain.entity.MarketRechargeRuleEntity;
import com.mammon.market.domain.query.MarketRechargeRuleQuery;
import org.springframework.data.domain.PageRequest;
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
public class MarketRechargeRuleDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MarketRechargeRuleEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_market_recharge_rule (")
                .append(" id, merchant_no, store_no, prepaid_amount, name, real_amount, give_amount, give_integral, ")
                .append(" give_coupon_id, give_coupon_total, status, sort, remark, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :prepaidAmount, :name, :realAmount, :giveAmount, :giveIntegral, ")
                .append(" :giveCouponId, :giveCouponTotal, :status, :sort, :remark, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(MarketRechargeRuleEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_market_recharge_rule ")
                .append(" set ")
                .append(" prepaid_amount    = :prepaidAmount, ")
                .append(" name              = :name, ")
                .append(" real_amount       = :realAmount, ")
                .append(" give_amount       = :giveAmount, ")
                .append(" give_integral     = :giveIntegral, ")
                .append(" give_coupon_id    = :giveCouponId, ")
                .append(" give_coupon_total = :giveCouponTotal, ")
                .append(" sort              = :sort, ")
                .append(" remark            = :remark, ")
                .append(" update_time       = :updateTime ")
                .append(" where id          = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(long merchantNo, String id, int status) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_market_recharge_rule ")
                .append(" set ")
                .append(" status        = :status, ")
                .append(" update_time   = :updateTime ")
                .append(" where id      = :id AND merchant_no = :merchantNo ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(String id, long merchantNo) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_market_recharge_rule ")
                .append(" set ")
                .append(" deleted        = 1 ")
                .append(" where id = :id AND merchant_no = :merchantNo ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public MarketRechargeRuleEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_market_recharge_rule ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<MarketRechargeRuleEntity> rowMapper = new BeanPropertyRowMapper<>(MarketRechargeRuleEntity.class);
        List<MarketRechargeRuleEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public int countPage(long merchantNo, MarketRechargeRuleQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(id) ")
                .append(" FROM m_market_recharge_rule ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        if (query.getDeleted() != null) {
            sb.append(" AND deleted = :deleted ");
            params.addValue("deleted", query.getDeleted());
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<MarketRechargeRuleEntity> findPage(long merchantNo,
                                                   MarketRechargeRuleQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_market_recharge_rule ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        if (query.getDeleted() != null) {
            sb.append(" AND deleted = :deleted ");
            params.addValue("deleted", query.getDeleted());
        }
        sb.append(" ORDER BY sort DESC, create_time DESC ");

        sb.append(" limit :limit offset :offset ");
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<MarketRechargeRuleEntity> rowMapper = new BeanPropertyRowMapper<>(MarketRechargeRuleEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<MarketRechargeRuleEntity> findAll(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_market_recharge_rule ")
                .append(" WHERE merchant_no = :merchantNo ");
        sb.append(" ORDER BY sort DESC, create_time DESC ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<MarketRechargeRuleEntity> rowMapper = new BeanPropertyRowMapper<>(MarketRechargeRuleEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
