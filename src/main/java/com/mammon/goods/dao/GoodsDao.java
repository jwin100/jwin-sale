package com.mammon.goods.dao;

import com.mammon.goods.domain.entity.GoodsEntity;
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
public class GoodsDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(GoodsEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_goods (")
                .append(" id, barcode, name, class_code, brand_name, specification, price, width, height, depth, code_source, ")
                .append(" firm_name, gross_weight, batch, firm_address, firm_status, firm_description, barcode_status, ")
                .append(" image, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :barcode, :name, :classCode, :brandName, :specification, :price, :width, :height, :depth, :codeSource, ")
                .append(" :firmName, :grossWeight, :batch, :firmAddress, :firmStatus, :firmDescription, :barcodeStatus, ")
                .append(" (:image)::jsonb, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public GoodsEntity findByBarcode(String barcode) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_goods ")
                .append(" WHERE barcode = :barcode ");
        params.addValue("barcode", barcode);

        String sql = sb.toString();

        RowMapper<GoodsEntity> rowMapper = new BeanPropertyRowMapper<>(GoodsEntity.class);
        List<GoodsEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }
}
