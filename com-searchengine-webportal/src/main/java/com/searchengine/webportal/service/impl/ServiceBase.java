package com.searchengine.webportal.service.impl;

import com.searchengine.webportal.context.LoginContext;
import org.apache.log4j.Logger;

/**
 * Created by liold on 2014/11/14.
 */
abstract class ServiceBase {
    protected final Logger logger = Logger.getLogger(this.getClass());

    /**
     * 获取当前用户的id
     */
    protected long getCurrentUserId()
    {
        return LoginContext.getLoginContext().getUserId();
    }
}
