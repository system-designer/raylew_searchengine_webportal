package com.searchengine.webportal.interceptor;

import com.searchengine.security.DesSecurityComponent;
import com.searchengine.utils.CommonUtils;
import com.searchengine.utils.JsonUtils;
import com.searchengine.webportal.common.RequestPathExcluder;
import com.searchengine.webportal.context.LoginContext;
import com.searchengine.webportal.cookie.CookieUtils;
import com.searchengine.webportal.dto.ResultCode;
import com.searchengine.webportal.dto.UserInfo;
import com.searchengine.webportal.service.UserService;
import com.searchengine.webportal.service.impl.WebApiResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

//import com.searchengine.service.RedisService;

public class LoginContextInterceptor extends HandlerInterceptorAdapter {
    private final static Logger logger = Logger.getLogger(LoginContextInterceptor.class);

    /**
     * 判断session有效时间，单位：秒 1800 为 30 * 60 。30分钟
     */
    protected int sessionTimeout = 24 * 60 * 60;

    protected int rate = 2;

    protected CookieUtils cookieUtils;

    protected RequestPathExcluder requestPathExcluder;

    private DesSecurityComponent desSecurityComponent;

    //private RedisService redisService;

    private String sessionErrorFilter;

    @Value("${login.cookie.name}")
    private String loginCookieKey;

    @Resource
    private UserService userService;

    /**
     * 解析cookieValue转为LoginContext
     *
     * @param cookieValue
     * @return
     */
    protected LoginContext getLoginContext(String cookieValue) {
        return LoginContext.parse(cookieValue);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (requestPathExcluder.isExclude(request)) {
                return true;
            } else {
                return updateLogin(request, response);
            }
        } catch (Exception e) {
            logger.warn("updateLogin error!", e);
        }
        return false;
    }

    /**
     * 更新用户登陆状态
     *
     * @param request
     * @param response
     */
    protected boolean updateLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginContext context = null;
            logger.info("==received request=" + request.getPathInfo());
            String paramCookieValue = cookieUtils.getCookieValue(request, loginCookieKey);
            //如果客户端没有cookie(第一次登录)，直接转到登录操作
            if (paramCookieValue == null || paramCookieValue.length() < 10) {
                return true;
            }
            //有cookie,解析客户端传上来的cookie
            try {
                byte[] base64 = Base64.decodeBase64(paramCookieValue.getBytes());
                byte[] cookieByte = desSecurityComponent.decryptBytes(base64);
                String cookieParam = new String(cookieByte, "UTF-8");
                context = getLoginContext(cookieParam); // 反序列化
            } catch (Exception e) {
                logger.error("cookie param error:", e);
            }
            if (context == null) {
                logger.debug("parse session cookie[" + loginCookieKey + "] return null!");
                WebApiResult<String> WebApiResult = new WebApiResult<String>();
                WebApiResult.setCode(ResultCode.SESSION_ERROR);
                WebApiResult.setMsg("session error.");
                sendMsgOut(response, JsonUtils.toJson(WebApiResult));
                return false;
            }
            logger.info("===received request=" + context.getLoginName());
            if (context.getUserId() == CommonUtils.SYS_NOTIFY_USER_ID) {
                logger.info("sysNotify=" + context.getLoginName());
                return true;
            }

            long current = System.currentTimeMillis();
            long created = context.getCreated();
            long expires = context.getExpires();
            long timeout = (expires == 0) ? (sessionTimeout * 1000) : (expires - created);//
            if (current - created < timeout) {  // slide expiration
                LoginContext.setLoginContext(context);
                if ((current - created) * rate > timeout) {
                    context.setCreated(current);
                    if (expires != 0) {
                        context.setTimeout(timeout);
                    }
                    String cookieValue = context.toCookieValue();
                    byte[] tokenByte = desSecurityComponent.encryptBytes(cookieValue.getBytes());
                    byte[] base64Byte = Base64.encodeBase64(tokenByte);
                    String tokenValue = new String(base64Byte, "UTF-8");
                    cookieUtils.setCookie(response, loginCookieKey, tokenValue);
                    logger.debug("session cookie[" + loginCookieKey + "] rewrite!");
                }
                return true;
            } else {
                logger.debug("session cookie[" + loginCookieKey + "] is valid!");
                cookieUtils.invalidate(request, response);
            }
        } catch (Exception e) {
            logger.error("login intercept error", e);
        }
        WebApiResult<String> WebApiResult = new WebApiResult<String>();
        WebApiResult.setCode(ResultCode.SESSION_ERROR);
        WebApiResult.setMsg("session error.");
        sendMsgOut(response, JsonUtils.toJson(WebApiResult));
        return false;
    }

    @Deprecated
    private LoginContext initContext(String loginName, String pwd) throws Exception {
        LoginContext context = new LoginContext();
        context.setLoginName(loginName);
        UserInfo userInfo = new UserInfo(context.getLoginName(), null, null, null);

        WebApiResult<UserInfo> result = null;
        try {
            result = userService.login(loginName, pwd);
            if (result.getCode() == ResultCode.USER_OK) {
                context.setUserId(result.getData().getId());
                context.setNickName(result.getData().getLoginName());
            }
        } catch (Exception e) {
            context.setUserId(userInfo.getId());
            logger.error(e.getMessage());
        }

        context.setCreated(System.currentTimeMillis());
        context.setExpires(System.currentTimeMillis() + sessionTimeout * 1000);
        return context;
    }

    /**
     * 输出错误信息
     *
     * @param response
     * @param WebApiResult
     */
    private void sendMsgOut(HttpServletResponse response, String WebApiResult) {
        try {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setContentType(CommonUtils.MediaTypeJSON);
            OutputStream ps = response.getOutputStream();
            ps.write(WebApiResult.getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("send message out error.");
        }
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

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        super.afterCompletion(request, response, handler, ex);
    }

    public void setCookieUtils(CookieUtils cookieUtils) {
        this.cookieUtils = cookieUtils;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public RequestPathExcluder getRequestPathExcluder() {
        return requestPathExcluder;
    }

    public void setRequestPathExcluder(RequestPathExcluder requestPathExcluder) {
        this.requestPathExcluder = requestPathExcluder;
    }

    public DesSecurityComponent getDesSecurityComponent() {
        return desSecurityComponent;
    }

    public void setDesSecurityComponent(DesSecurityComponent desSecurityComponent) {
        this.desSecurityComponent = desSecurityComponent;
    }

/*    public RedisService getRedisService() {
        return redisService;
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }*/

    /**
     * @return the sessionErrorFilter
     */
    public String getSessionErrorFilter() {
        return sessionErrorFilter;
    }

    /**
     * @param sessionErrorFilter the sessionErrorFilter to set
     */
    public void setSessionErrorFilter(String sessionErrorFilter) {
        this.sessionErrorFilter = sessionErrorFilter;
    }
}
