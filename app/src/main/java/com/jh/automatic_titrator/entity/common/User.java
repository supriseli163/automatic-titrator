package com.jh.automatic_titrator.entity.common;

import java.util.Set;

/**
 * Created by apple on 16/9/15.
 */
public class User {

    private String userName;

    private String password;

    private Role role;

    private String createDate;

    private String desc;

    private int headId;

    private String auth;

    private String creator;

    private boolean autoLogin;

    private Set<String> authList;

    private String settingConfig;

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public Set<String> getAuthList() {
        return authList;
    }

    public void setAuthList(Set<String> authList) {
        this.authList = authList;
    }

    public String getSettingConfig() {
        return settingConfig;
    }

    public void setSettingConfig(String settingConfig) {
        this.settingConfig = settingConfig;
    }
}
