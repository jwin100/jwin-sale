package com.mammon.merchant.dao;

import com.mammon.merchant.domain.entity.MerchantIndustryLogEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2023-02-02 11:41:05
 */
@Repository
public class MerchantIndustryLogDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MerchantIndustryLogEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_merchant_industry_log (")
                .append(" id, merchant_no, industry_id, add_month, create_time, type, order_no ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :industryId, :addMonth, :createTime, :type, :orderNo ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }
}
