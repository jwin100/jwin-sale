package com.mammon.print.dao;

import com.mammon.print.domain.dto.PrintTerminalQuery;
import com.mammon.print.domain.entity.PrintTerminalEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PrintTerminalDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(PrintTerminalEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_print_terminal (")
                .append(" id, merchant_no, store_no, classify, channel_id, name, terminal_code, form_config, ")
                .append(" width, bind_types, status, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :classify, :channelId, :name, :terminalCode, (:formConfig)::jsonb, ")
                .append(" :width, (:bindTypes)::jsonb, :status, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(PrintTerminalEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_print_terminal ")
                .append(" set ")
                .append(" name              = :name, ")
                .append(" form_config       = (:formConfig)::jsonb, ")
                .append(" terminal_code     = :terminalCode, ")
                .append(" width             = :width, ")
                .append(" bind_types        = (:bindTypes)::jsonb, ")
                .append(" status            = :status, ")
                .append(" update_time       = :updateTime ")
                .append(" where id          = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(String id, int status) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_print_terminal ")
                .append(" set ")
                .append(" status            = :status, ")
                .append(" update_time       = :updateTime ")
                .append(" where id          = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }
    
    public int delete(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM m_print_terminal ")
                .append(" where id = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public PrintTerminalEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_print_terminal ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<PrintTerminalEntity> rowMapper = new BeanPropertyRowMapper<>(PrintTerminalEntity.class);
        List<PrintTerminalEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public PrintTerminalEntity findByTerminalCode(String terminalCode) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_print_terminal ")
                .append(" WHERE terminal_code = :terminalCode ");
        params.addValue("terminalCode", terminalCode);

        String sql = sb.toString();

        RowMapper<PrintTerminalEntity> rowMapper = new BeanPropertyRowMapper<>(PrintTerminalEntity.class);
        List<PrintTerminalEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public List<PrintTerminalEntity> findAllByIds(List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_print_terminal ")
                .append(" WHERE id in (:ids) ");

        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<PrintTerminalEntity> rowMapper = new BeanPropertyRowMapper<>(PrintTerminalEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<PrintTerminalEntity> findAllByClassify(long merchantNo, long storeNo, int classify) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_print_terminal ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ")
                .append(" AND classify = :classify ")
                .append(" AND status = 1 ")
                .append(" ORDER BY create_time DESC ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("classify", classify);

        String sql = sb.toString();

        RowMapper<PrintTerminalEntity> rowMapper = new BeanPropertyRowMapper<>(PrintTerminalEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int count(long merchantNo, long storeNo, PrintTerminalQuery dto) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" COUNT(*) ")
                .append(" FROM  m_print_terminal ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<PrintTerminalEntity> page(long merchantNo, long storeNo, PrintTerminalQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_print_terminal ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND store_no = :storeNo ")
                .append(" ORDER BY create_time DESC ");

        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<PrintTerminalEntity> rowMapper = new BeanPropertyRowMapper<>(PrintTerminalEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
