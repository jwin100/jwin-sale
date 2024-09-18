package com.mammon.sms.dao;

import com.mammon.sms.domain.entity.SmsRechargeLogEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class SmsRechargeLogDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SmsRechargeLogEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_sms_recharge_log (")
                .append(" id, merchant_no, store_no, change_before, change_in, change_after, change_status, change_type, ")
                .append(" change_type_desc, send_id, order_no, remark, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :changeBefore, :changeIn, :changeAfter, :changeStatus, :changeType, ")
                .append(" :changeTypeDesc, :sendId, :orderNo, :remark, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }
}
