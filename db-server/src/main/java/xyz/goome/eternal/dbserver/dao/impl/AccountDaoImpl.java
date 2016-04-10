package xyz.goome.eternal.dbserver.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
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
        return null;
    }

    @Override
    public Account createAccount(String account, String password) {
        return null;
    }
}
