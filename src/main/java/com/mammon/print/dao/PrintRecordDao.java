package com.mammon.print.dao;

import com.mammon.print.domain.entity.PrintRecordEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class PrintRecordDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(PrintRecordEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_print_record (")
                .append(" id, merchant_no, store_no, template_id, terminal_id, print_type, content, order_no, status, remark, ")
                .append(" create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :templateId, :terminalId, :printType, :content, :orderNo, :status, :remark, ")
                .append(" :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(PrintRecordEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_print_record ")
                .append(" set ")
                .append(" template_id = :templateId, ")
                .append(" terminal_id = :terminalId, ")
                .append(" remark = :remark ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(String id, int fromStatus, int toStatus, String remark) {
        StringBuilder sb = new StringBuilder();
        sb.append("update m_print_record ")
                .append(" set ")
                .append(" status = :toStatus, ")
                .append(" remark = :remark ")
                .append(" where id = :id ")
                .append(" AND status = :fromStatus ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("toStatus", toStatus);
        params.addValue("remark", remark);
        params.addValue("id", id);
        params.addValue("fromStatus", fromStatus);

        return namedParameterJdbcTemplate.update(sql, params);
    }
}
