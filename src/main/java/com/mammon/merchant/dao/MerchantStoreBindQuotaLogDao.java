package com.mammon.merchant.dao;

import com.mammon.merchant.domain.entity.MerchantStoreBindQuotaLogEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2023-03-03 11:59:46
 */
@Repository
public class MerchantStoreBindQuotaLogDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MerchantStoreBindQuotaLogEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_merchant_store_bind_quota (")
                .append(" id, merchant_no, store_no, before_end_date, add_days, after_end_date, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :beforeEndDate, :addDays, :afterEndDate, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }
}
