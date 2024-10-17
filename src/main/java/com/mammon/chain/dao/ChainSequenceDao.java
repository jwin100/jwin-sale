package com.mammon.chain.dao;

import com.mammon.config.ChainDataSourceConfig;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/6/14 17:20
 */
@Repository
public class ChainSequenceDao {

    private static final String SEQ_NAME = "chain_links_gen_num";
    private static final long DEFAULT_START_SEQ_NO = 111;

    @Resource(name = ChainDataSourceConfig.ChainDbJdbcTemplateName)
    private JdbcTemplate jdbcTemplate;

    public long reset(long startSeqNo) {
        if (startSeqNo <= 0) {
            startSeqNo = DEFAULT_START_SEQ_NO;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT setval('%s', %s)");

        String sql = String.format(sb.toString(), SEQ_NAME, startSeqNo);
        List<Number> result = jdbcTemplate.queryForList(sql, Number.class);
        return result.stream().map(Number::longValue).findFirst().orElse(0L);
    }

    public long next() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT nextval('%s')");
        String sql = String.format(sb.toString(), SEQ_NAME);

        List<Number> result = jdbcTemplate.queryForList(sql, Number.class);
        return result.stream().map(Number::longValue).findFirst().orElse(0L);
    }
}
