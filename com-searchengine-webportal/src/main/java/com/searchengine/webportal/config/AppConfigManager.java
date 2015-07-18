package com.searchengine.webportal.config;

import com.searchengine.webportal.common.ApplicationContextHelper;

/**
 * Created by [Ray Lew] on 2014/11/15.
 */
public class AppConfigManager {

    private static final AppConfig appConfig;

    static {
        appConfig = (AppConfig) ApplicationContextHelper.getBean("appConfig");
    }

    /**
     * 获取 DoraWebApiBaseUrl 配置
     * @return
     */
    public static String getDoraWebApiBaseUrl()
    {
        return appConfig.getDoraWebApiBaseUrl();
    }
}
