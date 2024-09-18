package com.mammon.merchant.dao;

import com.mammon.merchant.domain.query.MerchantQuery;
import com.mammon.merchant.domain.query.MerchantStoreQuery;
import com.mammon.merchant.domain.entity.MerchantEntity;
import com.mammon.merchant.domain.entity.MerchantStoreEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class MerchantDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MerchantEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_merchant (")
                .append(" merchant_no, merchant_name, pc_logo, mobile_logo, account_id, province, city, area, ")
                .append(" address, agent_id, status, code, source, create_time, update_time")
                .append(" ) VALUES ( ")
                .append(" :merchantNo, :merchantName, :pcLogo, :mobileLogo, :accountId, :province, :city, :area, ")
                .append(" :address, :agentId, :status, :code, :source, :createTime, :updateTime")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(MerchantEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_merchant ")
                .append(" set ")
                .append(" merchant_name     = :merchantName, ")
                .append(" pc_logo           = :pcLogo, ")
                .append(" mobile_logo       = :mobileLogo, ")
                .append(" update_time       = :updateTime ")
                .append(" where merchant_no = :merchantNo ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public MerchantEntity findByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<MerchantEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantEntity.class);
        List<MerchantEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public int countPage(MerchantQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_merchant ");
        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<MerchantEntity> findPage(MerchantQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant ");

        sb.append(" ORDER BY create_time ASC ");
        sb.append(" limit :limit offset :offset ");
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<MerchantEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
