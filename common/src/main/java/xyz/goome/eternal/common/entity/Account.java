package xyz.goome.eternal.common.entity;

import java.io.Serializable;

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
    private String authId;
    /**服务器ID*/
    private Integer serverId;

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

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

}
