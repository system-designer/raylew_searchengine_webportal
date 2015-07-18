package com.searchengine.webportal.dto;

import java.util.Date;

/**
 * Created by RayLew on 2014-11-18.
 */
public class UserInfo {

    private long id;                // Id
    private String loginName;       // 登录名
    private String password;        // 密码
    private Date createdTime;       // 创建时间
    private Date lastUpdatedTime;   // 最后更新时间
    private long passportId;
    private String avatar;          //头像
    private String email;           //
    private int status;             // 状态
    private String deviceId;        // 设备号
    private int roleType;

    public UserInfo() {
    }

    public UserInfo(String loginName, String password, String avatar, String email) {
        this.loginName = loginName;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public long getPassportId() {
        return passportId;
    }

    public void setPassportId(long passportId) {
        this.passportId = passportId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }
}
