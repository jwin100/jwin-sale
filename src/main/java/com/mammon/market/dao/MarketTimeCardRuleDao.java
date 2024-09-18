package com.mammon.market.dao;

import com.mammon.common.PageQuery;
import com.mammon.market.domain.entity.MarketTimeCardRuleEntity;
import com.mammon.market.domain.query.MarketTimeCardRuleQuery;
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
public class MarketTimeCardRuleDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MarketTimeCardRuleEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_market_time_card_rule (")
                .append(" id, merchant_no, store_no, name, expire_type, expire_month, time_total, ")
                .append(" real_amount, give_integral, give_coupon_id, give_coupon_total, status, sort, ")
                .append(" account_id, create_time, update_time, remark, spu_ids ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :name, :expireType, :expireMonth, :timeTotal, ")
                .append(" :realAmount, :giveIntegral, :giveCouponId, :giveCouponTotal, :status, :sort, ")
                .append(" :accountId, :createTime, :updateTime, :remark, (:spuIds)::jsonb ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(MarketTimeCardRuleEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_market_time_card_rule ")
                .append(" set ")
                .append(" name              = :name, ")
                .append(" expire_type       = :expireType, ")
                .append(" expire_month      = :expireMonth, ")
                .append(" time_total        = :timeTotal, ")
                .append(" real_amount       = :realAmount, ")
                .append(" give_integral     = :giveIntegral, ")
                .append(" give_coupon_id    = :giveCouponId, ")
                .append(" give_coupon_total = :giveCouponTotal, ")
                .append(" spu_ids           = (:spuIds)::jsonb, ")
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

        sb.append("update m_market_time_card_rule ")
                .append(" set ")
                .append(" status        = :status, ")
                .append(" update_time   = :updateTime ")
                .append(" where id = :id AND merchant_no = :merchantNo ");

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

        sb.append("update m_market_time_card_rule ")
                .append(" set ")
                .append(" deleted        = 1 ")
                .append(" where id = :id AND merchant_no = :merchantNo ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public MarketTimeCardRuleEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_market_time_card_rule ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<MarketTimeCardRuleEntity> rowMapper = new BeanPropertyRowMapper<>(MarketTimeCardRuleEntity.class);
        List<MarketTimeCardRuleEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<MarketTimeCardRuleEntity> findAllByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_market_time_card_rule ")
                .append(" WHERE id in ( :ids ) ");
        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<MarketTimeCardRuleEntity> rowMapper = new BeanPropertyRowMapper<>(MarketTimeCardRuleEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int countPage(long merchantNo, MarketTimeCardRuleQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(id) ")
                .append(" FROM m_market_time_card_rule ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        if (query.getDeleted() != null) {
            sb.append(" AND deleted = :deleted ");
            params.addValue("deleted", query.getDeleted());
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<MarketTimeCardRuleEntity> findPage(long merchantNo, MarketTimeCardRuleQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_market_time_card_rule ")
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

        RowMapper<MarketTimeCardRuleEntity> rowMapper = new BeanPropertyRowMapper<>(MarketTimeCardRuleEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<MarketTimeCardRuleEntity> findAll(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_market_time_card_rule ")
                .append(" WHERE merchant_no = :merchantNo ");
        sb.append(" ORDER BY sort DESC, create_time DESC ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<MarketTimeCardRuleEntity> rowMapper = new BeanPropertyRowMapper<>(MarketTimeCardRuleEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
