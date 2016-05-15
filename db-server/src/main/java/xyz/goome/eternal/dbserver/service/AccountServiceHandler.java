package xyz.goome.eternal.dbserver.service;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.goome.eternal.common.service.AccountService;
import xyz.goome.eternal.common.entity.Account;
import xyz.goome.eternal.common.entity.Result;
import xyz.goome.eternal.dbserver.dao.AccountDao;

import java.util.Date;
import java.util.UUID;

/**
 * Created by matrix on 16/4/9.
 */
@Component("accountService")
public class AccountServiceHandler implements AccountService.Iface {

    @Autowired
    private AccountDao accountDao;

    @Override
    public String accountLogin(String account, String password) throws TException {
        Account obj = accountDao.getAccount(account, password);
        Result result = new Result();
        if(obj == null) {
            result.setSuccess(false);
        } else {
            result.setSuccess(true);
            result.setData(obj);
        }
        return result.toString();
    }

    @Override
    public String accountRegist(String account, String password) throws TException {
        UUID uuid = UUID.randomUUID();
        String authid = uuid.toString().toLowerCase();

        Account obj = new Account();
        obj.setAccount(account);
        obj.setPassword(password);
        obj.setAuthid(authid);
        obj.setCreateTime(new Date());

        boolean success = accountDao.addAccount(obj);
        Result result = new Result();
        if(success) {
            result.setSuccess(true);
            result.setData(authid);
        } else {
            result.setSuccess(false);
        }
        return result.toString();
    }

}
