package com.searchengine.webportal.controller;

import com.searchengine.webportal.context.LoginContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Controller基类
 */
@Controller
public class ControllerBase {

    protected final Logger logger = Logger.getLogger(this.getClass());

    /**
     * 将请求流转成字符串
     *
     * @param request
     * @return
     * @throws java.io.IOException
     */
    protected String getRequestEntity(HttpServletRequest request) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read = bis.read();
        while (read != -1) {
            baos.write(read);
            read = bis.read();
        }
        bis.close();
        byte data[] = baos.toByteArray();
        return new String(data, "utf-8");
    }

    /**
     * 得到当前登录的用户id
     *
     * @return
     */
    protected Long getCurrentUserId() {
        Long userId = 0L;
        LoginContext loginContext = LoginContext.getLoginContext();
        if (loginContext != null) {
            userId = loginContext.getUserId();
        }
        return userId;
    }

    /**
     * 得到当前登录的用户类型
     *
     * @return
     */
    protected int getCurrentRoleType() {
        int roleType = 0;
        LoginContext loginContext = LoginContext.getLoginContext();
        if (loginContext != null) {
            roleType = loginContext.getRoleType();
        }
        return roleType;
    }
}
