package com.mammon.member.dao;

import com.mammon.member.domain.entity.MemberAttrEntity;
import com.mammon.member.domain.entity.MemberEntity;
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
public class MemberAttrDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MemberAttrEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_member_attr (")
                .append(" id, merchant_no, new_reward, reward_model, reward_number, convert_amount, convert_integral ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :newReward, :rewardModel, :rewardNumber, :convertAmount, :convertIntegral ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(MemberAttrEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_member_attr ")
                .append(" set ")
                .append(" new_reward          = :newReward, ")
                .append(" reward_model        = :rewardModel, ")
                .append(" reward_number       = :rewardNumber, ")
                .append(" convert_amount      = :convertAmount, ")
                .append(" convert_integral    = :convertIntegral ")
                .append(" where merchant_no   = :merchantNo ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public MemberAttrEntity findByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_member_attr ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<MemberAttrEntity> rowMapper = new BeanPropertyRowMapper<>(MemberAttrEntity.class);
        List<MemberAttrEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (store.iterator().hasNext()) {
            return store.get(0);
        }
        return null;
    }
}
