package com.mammon.chain.dao;

import com.mammon.chain.domain.entity.ChainRecordEntity;
import com.mammon.config.ChainDataSourceConfig;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2022-10-17 16:21:37
 */
@Repository
public class ChainRecordDao {

    @Resource(name = ChainDataSourceConfig.ChainDbNamedParameterJdbcTemplateName)
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(ChainRecordEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO t_chain_record (")
                .append(" id, code, url, ip, refer, useragent, domain, create_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :code, :url, :ip, :refer, :useragent, :domain, :createTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }
}
