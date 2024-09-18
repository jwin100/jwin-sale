package com.mammon.merchant.dao;

import com.mammon.merchant.domain.entity.MerchantStoreEntity;
import com.mammon.merchant.domain.entity.RegionEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class RegionDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<RegionEntity> findAll() {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_region ")
                .append(" WHERE level < 4 ");

        String sql = sb.toString();

        RowMapper<RegionEntity> rowMapper = new BeanPropertyRowMapper<>(RegionEntity.class);
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }

    public List<RegionEntity> findAllByPid(String pid) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_region ")
                .append(" WHERE pid = :pid ");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("pid", pid);

        String sql = sb.toString();

        RowMapper<RegionEntity> rowMapper = new BeanPropertyRowMapper<>(RegionEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public RegionEntity findById(String id) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_region ")
                .append(" WHERE id = :id ");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        String sql = sb.toString();
        RowMapper<RegionEntity> rowMapper = new BeanPropertyRowMapper<>(RegionEntity.class);
        List<RegionEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
