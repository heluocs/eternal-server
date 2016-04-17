package xyz.goome.eternal.common.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 账号信息
 * Created by matrix on 16/4/9.
 */
public class Account implements Serializable {

    private Integer id;
    /**账号*/
    private String account;
    /**密码*/
    private String password;
    /**认证ID*/
    private String authid;
    /**当前服务器ID*/
    private Integer currServId;
    /**注册时间*/
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthid() {
        return authid;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }

    public Integer getCurrServId() {
        return currServId;
    }

    public void setCurrServId(Integer currServId) {
        this.currServId = currServId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
