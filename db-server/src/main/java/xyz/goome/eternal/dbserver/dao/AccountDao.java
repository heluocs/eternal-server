package xyz.goome.eternal.dbserver.dao;

import xyz.goome.eternal.common.entity.Account;

/**
 * Created by matrix on 16/4/9.
 */
public interface AccountDao {

    /**
     * 查找账号
     * @param account
     * @param password
     * @return
     */
    public Account getAccount(String account, String password);

    /**
     * 创建账号
     * @param account
     * @param password
     * @return
     */
    public Account createAccount(String account, String password);

}
