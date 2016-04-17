package xyz.goome.eternal.dbserver.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import xyz.goome.eternal.common.entity.Account;
import xyz.goome.eternal.dbserver.dao.AccountDao;

/**
 * Created by matrix on 16/4/9.
 */
@Repository
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Account getAccount(String account, String password) {
        String sql = "select * from t_account where account = ? and password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{account, password}, BeanPropertyRowMapper.newInstance(Account.class));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int addAccount(Account account) {
        String sql = "insert into t_account(account, password, authid, createTime) values (?,?,?,?)";
        return jdbcTemplate.update(sql, new Object[]{account.getAccount(), account.getPassword(),
            account.getAuthid(), account.getCreateTime()});
    }


}
