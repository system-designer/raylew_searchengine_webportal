package com.searchengine.webportal.service;

import com.searchengine.webportal.dto.UserInfo;
import com.searchengine.webportal.service.impl.WebApiResult;

/**
 * 用户账号相关功能
 */
public interface UserService {

    /**
     * 新用户注册
     * @param userInfo
     * @return 返回注册信息，如果出现错误，则返回详细的错误信息
     */
    public WebApiResult<Long> register(UserInfo userInfo);

    /**
     * 登录
     * @param loginName
     * @param password
     * @return 返回成功或失败的标识
     */
    public WebApiResult<UserInfo> login(String loginName, String password);

    /***
     * 根据passportId查询用户信息
     * @param userInfo
     * @return
     */
    public WebApiResult<UserInfo> findByPassportId(UserInfo userInfo) throws Exception ;

}
