package com.mammon.leaf.dao;

import com.mammon.leaf.domain.entity.LeafCodeEntity;
import com.mammon.market.domain.entity.MarketRechargeRuleEntity;
import com.mammon.member.domain.entity.MemberTimeCardEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2024/1/25 10:13
 */
@Repository
public class LeafCodeDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(long merchantNo) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_leaf_code (")
                .append(" merchant_no ")
                .append(" ) VALUES ( ")
                .append(" :merchantNo ")
                .append(" ) ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("merchantNo", merchantNo);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public LeafCodeEntity edit(LeafCodeEntity entity) {
        StringBuilder sb = new StringBuilder();

        List<String> list = new ArrayList<>();
        if (entity.getGoodsSpuCode() > 0) {
            list.add(" goods_spu_code = goods_spu_code + :goodsSpuCode ");
        }
        if (entity.getGoodsSkuCode() > 0) {
            list.add(" goods_sku_code = goods_sku_code + :goodsSkuCode ");
        }
        if (entity.getDocketNo() > 0) {
            list.add(" docket_no = docket_no + :docketNo ");
        }
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        sb.append("update m_leaf_code ")
                .append(" set ")
                .append(String.join(",", list))
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" RETURNING * ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        RowMapper<LeafCodeEntity> rowMapper = new BeanPropertyRowMapper<>(LeafCodeEntity.class);
        return namedParameterJdbcTemplate.queryForObject(sql, params, rowMapper);
    }

    public boolean exists(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_leaf_code ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<LeafCodeEntity> rowMapper = new BeanPropertyRowMapper<>(LeafCodeEntity.class);
        List<LeafCodeEntity> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return !CollectionUtils.isEmpty(result);
    }

}
