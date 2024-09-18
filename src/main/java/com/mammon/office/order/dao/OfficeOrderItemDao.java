package com.mammon.office.order.dao;

import com.mammon.office.order.domain.entity.OfficeOrderItemEntity;
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

/**
 * @author dcl
 * @date 2023-02-02 13:23:57
 */
@Repository
public class OfficeOrderItemDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(OfficeOrderItemEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_office_order_item (")
                .append(" id, order_id, spu_id, sku_id, quantity, unit, type, status, active_message, ")
                .append(" remark, active_time, payable_amount, bind_store_no, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :orderId, :spuId, :skuId, :quantity, :unit, :type, :status, :activeMessage, ")
                .append(" :remark, :activeTime, :payableAmount, :bindStoreNo, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatusById(String id, int status, String message, LocalDateTime activeTime) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_office_order_item ")
                .append(" set ")
                .append(" status            = :status, ")
                .append(" active_message    = :message, ")
                .append(" active_time       = :activeTime, ")
                .append(" update_time       = :updateTime ")
                .append(" where id          = :id ");

        String sql = sb.toString();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("message", message);
        params.addValue("activeTime", activeTime);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public List<OfficeOrderItemEntity> findAllByOrderId(String orderId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_office_order_item ")
                .append(" WHERE order_id = :orderId ");

        params.addValue("orderId", orderId);

        String sql = sb.toString();

        RowMapper<OfficeOrderItemEntity> rowMapper = new BeanPropertyRowMapper<>(OfficeOrderItemEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

}
