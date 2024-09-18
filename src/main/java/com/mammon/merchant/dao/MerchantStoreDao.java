package com.mammon.merchant.dao;

import com.mammon.merchant.domain.query.MerchantStoreQuery;
import com.mammon.merchant.domain.entity.MerchantStoreEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MerchantStoreDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(MerchantStoreEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_merchant_store (")
                .append(" id, store_no, merchant_no, store_name, store_phone, account_id, main, province, city, area, address, ")
                .append(" industry_one, industry_two, industry_three, status, create_time, update_time, end_date ")
                .append(" ) VALUES ( ")
                .append(" :id, :storeNo, :merchantNo, :storeName, :storePhone, :accountId, :main, :province, :city, :area, :address, ")
                .append(" :industryOne, :industryTwo, :industryThree, :status, :createTime, :updateTime, :endDate ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(MerchantStoreEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_merchant_store ")
                .append(" set ")
                .append(" store_name     = :storeName, ")
                .append(" store_phone    = :storePhone, ")
                .append(" account_id     = :accountId, ")
                .append(" province       = :province, ")
                .append(" city           = :city, ")
                .append(" area           = :area, ")
                .append(" address        = :address, ")
                .append(" industry_one   = :industryOne, ")
                .append(" industry_two   = :industryTwo, ")
                .append(" industry_three = :industryThree, ")
                .append(" update_time    = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatusById(String id, int status) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_merchant_store ")
                .append(" set ")
                .append(" status         = :status ")
                .append(" where id = :id ");

        String sql = sb.toString();
        params.addValue("status", status);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editEndDateById(String id, LocalDate endDate) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_merchant_store ")
                .append(" set ")
                .append(" end_date      = :endDate, ")
                .append(" update_time   = :updateTime ")
                .append(" where id = :id ");

        params.addValue("endDate", endDate);
        params.addValue("updateTime", LocalDate.now());
        params.addValue("id", id);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE  m_merchant_store")
                .append(" SET deleted = 1 ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public MerchantStoreEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_store ")
                .append(" WHERE id = :id ");
        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<MerchantStoreEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantStoreEntity.class);
        List<MerchantStoreEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public MerchantStoreEntity findMain(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_store ")
                .append(" WHERE merchant_no = :merchantNo AND main = true ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        RowMapper<MerchantStoreEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantStoreEntity.class);
        List<MerchantStoreEntity> list = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return list.stream().findFirst().orElse(null);
    }

    public MerchantStoreEntity findByStoreNo(long merchantNo, long storeNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_store ")
                .append(" WHERE merchant_no = :merchantNo AND store_no = :storeNo ");
        params.addValue("merchantNo", merchantNo);
        params.addValue("storeNo", storeNo);

        String sql = sb.toString();

        RowMapper<MerchantStoreEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantStoreEntity.class);
        List<MerchantStoreEntity> store = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        if (store.iterator().hasNext()) {
            return store.get(0);
        }
        return null;
    }

    public List<MerchantStoreEntity> findAllByStoreNos(long merchantNo, List<Long> storeNos) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_store ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        if (!CollectionUtils.isEmpty(storeNos)) {
            sb.append(" AND store_no in ( :storeNos ) ");
            params.addValue("storeNos", storeNos);
        }

        String sql = sb.toString();

        RowMapper<MerchantStoreEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantStoreEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<MerchantStoreEntity> findAllByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_store ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);
        String sql = sb.toString();

        RowMapper<MerchantStoreEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantStoreEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public int countByMerchantNo(long merchantNo) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_merchant_store ")
                .append(" WHERE merchant_no = :merchantNo ");
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public int countPage(long merchantNo, MerchantStoreQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_merchant_store ")
                .append(" WHERE merchant_no = :merchantNo  AND deleted = 0 ");
        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" and ( store_name like :searchKey or store_phone = :searchKey ) ");
            params.addValue("searchKey", "%" + query.getSearchKey() + "%");
        }

        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<MerchantStoreEntity> findPage(long merchantNo, MerchantStoreQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_store ")
                .append(" WHERE merchant_no = :merchantNo AND deleted = 0 ");
        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" and ( store_name like :searchKey or store_phone = :searchKey ) ");
            params.addValue("searchKey", "%" + query.getSearchKey() + "%");
        }

        sb.append(" ORDER BY create_time ASC ");
        sb.append(" limit :limit offset :offset ");
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<MerchantStoreEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantStoreEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<MerchantStoreEntity> findAll() {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_merchant_store ");
        String sql = sb.toString();

        RowMapper<MerchantStoreEntity> rowMapper = new BeanPropertyRowMapper<>(MerchantStoreEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
