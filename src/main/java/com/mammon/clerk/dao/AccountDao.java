package com.mammon.clerk.dao;

import com.mammon.clerk.domain.entity.AccountEntity;
import com.mammon.clerk.domain.query.AccountQuery;
import org.apache.commons.lang3.StringUtils;
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
public class AccountDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int save(AccountEntity entity) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO m_clerk_account (")
                .append(" id, merchant_no, store_no, username, password, mobile_cash_mode, ")
                .append(" name, phone, email, open_id, ")
                .append(" status, create_time, update_time ")
                .append(" ) VALUES ( ")
                .append(" :id, :merchantNo, :storeNo, :username, :password, :mobileCashMode, ")
                .append(" :name, :phone, :email, :openId, ")
                .append(" :status, :createTime, :updateTime ")
                .append(" ) ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int edit(AccountEntity entity) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_account ")
                .append(" set ")
                .append(" store_no       = :storeNo, ")
                .append(" username       = :username, ")
                .append(" name           = :name, ")
                .append(" phone          = :phone, ")
                .append(" email          = :email, ")
                .append(" open_id        = :openId, ")
                .append(" status         = :status, ")
                .append(" update_time    = :updateTime ")
                .append(" where id = :id ");

        String sql = sb.toString();
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStoreNo(String id, long storeNo) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_account ")
                .append(" set ")
                .append(" store_no       = :storeNo ")
                .append(" where id       = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("storeNo", storeNo);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editPhone(String id, String originalPhone, String newPhone) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_account ")
                .append(" set ")
                .append(" username      = :username, ")
                .append(" phone         = :newPhone, ")
                .append(" update_time   = :updateTime ")
                .append(" where id      = :id AND phone = :originalPhone ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", newPhone);
        params.addValue("newPhone", newPhone);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("id", id);
        params.addValue("originalPhone", originalPhone);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editOpenId(String id, String openId) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_account ")
                .append(" set ")
                .append(" open_id       = :openId ")
                .append(" where id      = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("openId", openId);
        params.addValue("id", id);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editStatus(String id, int status) {
        StringBuilder sb = new StringBuilder();

        sb.append("update m_clerk_account ")
                .append(" set ")
                .append(" status        = :status ")
                .append(" where id      = :id ");

        String sql = sb.toString();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editPassword(String id, String originalPassword, String newPassword) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_clerk_account ")
                .append(" set ")
                .append(" password      = :newPassword, ")
                .append(" update_time   = :updateTime ")
                .append(" where id      = :id ");

        if (StringUtils.isNotBlank(originalPassword)) {
            sb.append(" AND password = :originalPassword ");
            params.addValue("originalPassword", originalPassword);
        }

        String sql = sb.toString();

        params.addValue("newPassword", newPassword);
        params.addValue("updateTime", LocalDateTime.now());
        params.addValue("id", id);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int editMobileCashMode(String id, int cashMode) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_clerk_account ")
                .append(" set ")
                .append(" mobile_cash_mode     = :cashMode ")
                .append(" where id      = :id ");
        String sql = sb.toString();

        params.addValue("cashMode", cashMode);
        params.addValue("id", id);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int delete(long merchantNo, String id) {
        StringBuilder sb = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sb.append("update m_clerk_account set deleted = 1 ")
                .append(" where id = :id and merchant_no = :merchantNo ");
        params.addValue("id", id);
        params.addValue("merchantNo", merchantNo);

        String sql = sb.toString();

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public AccountEntity findByPhone(String phone) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_account ")
                .append(" WHERE phone = :phone and deleted = 0 ");

        params.addValue("phone", phone);

        String sql = sb.toString();

        RowMapper<AccountEntity> rowMapper = new BeanPropertyRowMapper<>(AccountEntity.class);
        List<AccountEntity> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return result.stream().findFirst().orElse(null);
    }

    public AccountEntity findById(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_account ")
                .append(" WHERE id = :id ");

        params.addValue("id", id);

        String sql = sb.toString();

        RowMapper<AccountEntity> rowMapper = new BeanPropertyRowMapper<>(AccountEntity.class);
        List<AccountEntity> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return result.stream().findFirst().orElse(null);
    }

    public AccountEntity findByOpenId(String openId) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_account ")
                .append(" WHERE open_id = :openId ");

        params.addValue("openId", openId);

        String sql = sb.toString();

        RowMapper<AccountEntity> rowMapper = new BeanPropertyRowMapper<>(AccountEntity.class);
        List<AccountEntity> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return result.stream().findFirst().orElse(null);
    }

    public int countPage(long merchantNo, AccountQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" count(*) ")
                .append(" FROM m_clerk_account ")
                .append(" WHERE merchant_no = :merchantNo and deleted = 0 ")
                .append(pageWhere(query, params));

        params.addValue("merchantNo", merchantNo);
        String sql = sb.toString();

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<AccountEntity> findPage(long merchantNo, AccountQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_account ")
                .append(" WHERE merchant_no = :merchantNo and deleted = 0 ")
                .append(pageWhere(query, params));

        sb.append(" ORDER BY create_time ASC ");
        sb.append(" limit :limit offset :offset ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("limit", query.getPageSize());
        params.addValue("offset", query.getOffset());

        String sql = sb.toString();

        RowMapper<AccountEntity> rowMapper = new BeanPropertyRowMapper<>(AccountEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<AccountEntity> findAllByStoreNo(long merchantNo, long storeNo, Integer status, Integer deleted) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_account ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);

        if (storeNo > 0) {
            sb.append(" AND  store_no = :storeNo ");
            params.addValue("storeNo", storeNo);
        }

        if (status != null) {
            sb.append(" AND  status = :status ");
            params.addValue("status", status);
        }

        if (deleted != null) {
            sb.append(" AND  deleted = :deleted ");
            params.addValue("deleted", deleted);
        }

        String sql = sb.toString();

        RowMapper<AccountEntity> rowMapper = new BeanPropertyRowMapper<>(AccountEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<AccountEntity> findAllByIds(long merchantNo, List<String> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_account ")
                .append(" WHERE merchant_no = :merchantNo ")
                .append(" AND id in ( :ids ) ");

        params.addValue("merchantNo", merchantNo);
        params.addValue("ids", ids);

        String sql = sb.toString();

        RowMapper<AccountEntity> rowMapper = new BeanPropertyRowMapper<>(AccountEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<AccountEntity> findList(long merchantNo, AccountQuery query) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
                .append(" * ")
                .append(" FROM  m_clerk_account ")
                .append(" WHERE merchant_no = :merchantNo ");

        params.addValue("merchantNo", merchantNo);

        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" AND ( name like :searchKey or phone like :searchKey or username like :searchKey ) ");
            params.addValue("searchKey", "%" + query.getSearchKey() + "%");
        }

        if (query.getStatus() != null) {
            sb.append(" AND  status = :status ");
            params.addValue("status", query.getStatus());
        }

        String sql = sb.toString();

        RowMapper<AccountEntity> rowMapper = new BeanPropertyRowMapper<>(AccountEntity.class);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String pageWhere(AccountQuery query, MapSqlParameterSource params) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(query.getId())) {
            sb.append(" AND id = :id");
            params.addValue("id", query.getId());
        }
        if (query.getStoreNo() != null) {
            sb.append(" AND store_no = :storeNo");
            params.addValue("storeNo", query.getStoreNo());
        }
        if (StringUtils.isNotBlank(query.getSearchKey())) {
            sb.append(" AND ( name LIKE :searchKey OR phone LIKE :searchKey ) ");
            params.addValue("searchKey", "%" + query.getSearchKey() + "%");
        }
        if (query.getStatus() != null) {
            sb.append(" AND status = :status");
            params.addValue("status", query.getStatus());
        }
        return sb.toString();
    }
}
