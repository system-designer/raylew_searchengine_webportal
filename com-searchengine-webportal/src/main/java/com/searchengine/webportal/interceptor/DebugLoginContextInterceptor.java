package com.searchengine.webportal.interceptor;

import com.searchengine.webportal.context.LoginContext;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by RayLew on 2014/11/15.
 * 测试用
 */
@Component
public class DebugLoginContextInterceptor extends HandlerInterceptorAdapter {

    private boolean isEnabled;
    private long userId;
    private String loginName;
    private int roleType;

    public DebugLoginContextInterceptor() {

    }

    public DebugLoginContextInterceptor(boolean isEnabled, long userId, String loginName, int roleType) {
        this.isEnabled = isEnabled;
        if (isEnabled) {
            this.userId = userId;
            this.loginName = loginName;
            this.roleType = roleType;
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if (isEnabled) {
            // 登录
            String value = String.format("userId=%1$s,loginName=%2$s", userId, loginName);
            LoginContext.setLoginContext(LoginContext.parse(value));
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        super.postHandle(request, response, handler, modelAndView);
        LoginContext context = LoginContext.getLoginContext();
        if (context != null && modelAndView != null) {
            ModelMap map = modelAndView.getModelMap();
            if (map != null) {
                map.put("context", context);
            }
        }
    }
}
