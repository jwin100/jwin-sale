package com.mammon.sms.dao;

import com.mammon.sms.domain.entity.SmsEntity;
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
public class SmsDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(SmsEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_sms (")
                .append(" id, merchant_no, recharge, status, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :recharge, :status, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    /**
     * @param merchantNo
     * @param recharge
     * @return
     */
//    public int updateRecharge(long merchantNo, int recharge) {
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("update t_sms ")
//                .append(" set ")
//                .append(" recharge       = recharge + :recharge, ")
//                .append(" update_time    = :updateTime ")
//                .append(" where merchant_no = :merchantNo ");
//
//        String sql = sb.toString();
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("recharge", recharge);
//        params.addValue("updateTime", LocalDateTime.now());
//        params.addValue("merchantNo", merchantNo);
//
//        return namedParameterJdbcTemplate.update(sql, params);
//    }
    public SmsEntity updateRecharge(long merchantNo, long recharge) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();

        sb.append(" UPDATE m_sms SET ")
                .append(" recharge = recharge + :recharge ")
                .append(" WHERE id = ( ");
        sb.append("SELECT ")
                .append(" id ")
                .append(" FROM m_sms ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" FOR UPDATE ");
        sb.append(" ) RETURNING * ");

        params.addValue("recharge", recharge);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SmsEntity> rowMapper = new BeanPropertyRowMapper<>(SmsEntity.class);
        List<SmsEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public SmsEntity findByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_sms ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<SmsEntity> rowMapper = new BeanPropertyRowMapper<>(SmsEntity.class);
        List<SmsEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (store.iterator().hasNext()) {
            return store.get(0);
        }
        return null;
    }
}
