package com.mammon.merchant.dao;

import com.mammon.merchant.domain.entity.MerchantIndustryEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dcl
 * @date 2023-02-02 11:40:56
 */
@Repository
public class MerchantIndustryDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MerchantIndustryEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_merchant_industry (")
                .append(" id, merchant_no, industry_id, expire_date, create_time, update_time, type ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :industryId, :expireDate, :createTime, :updateTime, :type ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editByExpireDate(String id, String industryId, int industryType, LocalDate expireDate) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_merchant_industry ")
                .append(" set ")
                .append(" industry_id       = :industryId, ")
                .append(" expire_date       = :expireDate, ")
                .append(" type              = :industryType, ")
                .append(" update_time       = :updateTime ")
                .append(" where id          = :id ");

        String sql = sb.toString();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("industryId", industryId);
        params.addValue("expireDate", expireDate);
        params.addValue("industryType", industryType);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public MerchantIndustryEntity industryId(String industryId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_industry ")
                .append(" WHERE industry_id = :industryId ");
        params.addValue("industryId", industryId);

        String sql = sb.toString();

        RowMapper<MerchantIndustryEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantIndustryEntity.class);
        List<MerchantIndustryEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public MerchantIndustryEntity findByMerchantNo(long merchantNo, int industryType) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_industry ")
                .append(" WHERE merchant_no = :merchantNo and type = :industryType ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("industryType", industryType);

        String sql = sb.toString();

        RowMapper<MerchantIndustryEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantIndustryEntity.class);
        List<MerchantIndustryEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public MerchantIndustryEntity findByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_industry ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<MerchantIndustryEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantIndustryEntity.class);
        List<MerchantIndustryEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
