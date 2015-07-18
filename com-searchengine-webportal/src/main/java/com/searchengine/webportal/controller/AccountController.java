package com.searchengine.webportal.controller;

import com.searchengine.security.DesSecurityComponent;
import com.searchengine.utils.RequestUtils;
import com.searchengine.webportal.context.LoginContext;
import com.searchengine.webportal.cookie.CookieUtils;
import com.searchengine.webportal.dto.ResultCode;
import com.searchengine.webportal.dto.UserInfo;
import com.searchengine.webportal.service.UserService;
import com.searchengine.webportal.service.impl.WebApiResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * 账号相关
 */
@Controller
@RequestMapping("/account")
public class AccountController extends ControllerBase {

    @Autowired
    private UserService userService;

    @Value("${login.cookie.name}")
    private String cookieName;
    @Value("${login.cookie.domain}")
    private String cookieDomain;
    @Value("${login.cookie.roleType}")
    private String RoleTypeCookieName;

    @Autowired
    private CookieUtils cookieUtils;
    @Autowired
    private DesSecurityComponent desSecurityComponent;

    /**
     * 注册新账户
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        ModelAndView view = new ModelAndView("account/register");
        return view;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView regPost(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        String loginName = RequestUtils.getQueryString(request, "uname", null);
        String email = RequestUtils.getQueryString(request, "email", null);
        String pwd = RequestUtils.getQueryString(request, "pwd", null);
        String pwd1 = RequestUtils.getQueryString(request, "pwd1", null);
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(loginName)) {
            sb.append("用户名不能为空！");
        }
        if (StringUtils.isEmpty(email)) {
            sb.append("邮箱不能为空！");
        }
        if (StringUtils.isEmpty(pwd)) {
            sb.append("密码不能为空！");
        }
        if (StringUtils.isEmpty(pwd1)) {
            sb.append("重复密码不能为空！");
        }
        if (!StringUtils.isEmpty(pwd) && !StringUtils.isEmpty(pwd1) && !pwd.equals(pwd1)) {
            sb.append("两次密码输入不一致！");
        }
        if (sb.length() > 0) {
            modelMap.put("msg", sb.toString());
            return new ModelAndView("account/register", modelMap);
        }

        UserInfo userInfo = new UserInfo(loginName, pwd, null, email);
        WebApiResult<Long> result = userService.register(userInfo);
        if (result.getCode() == ResultCode.STATUS_OK) {
            return new ModelAndView("account/regSuccess");
        } else {
            modelMap.put("msg", result.getMsg());
            return new ModelAndView("account/register", modelMap);
        }
    }


    /**
     * 登录
     *
     * @param request
     * @param response
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        ModelAndView view = new ModelAndView("account/login");
        return view;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginPost(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        String loginName = RequestUtils.getQueryString(request, "uname", null);
        String pwd = RequestUtils.getQueryString(request, "pwd", null);
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(loginName)) {
            sb.append("用户名不能为空！");
        }
        if (StringUtils.isEmpty(pwd)) {
            sb.append("密码不能为空！");
        }
        if (sb.length() > 0) {
            modelMap.put("msg", sb.toString());
            return new ModelAndView("account/login", modelMap);
        }
        // login remotely
        WebApiResult<UserInfo> result = userService.login(loginName, pwd);
        if (result.getCode() == ResultCode.USER_OK && result.getData() != null) {
            //设置cookie
            if (StringUtils.isNotBlank(loginName) && StringUtils.isNotBlank(pwd)) {
                LoginContext context = new LoginContext(result.getData().getId(), loginName, "", result.getData().getRoleType());
                LoginContext.setLoginContext(context);
                byte[] tokenByte = desSecurityComponent.encryptBytes(context.toCookieValue().getBytes());
                byte[] base64Byte = Base64.encodeBase64(tokenByte);
                String tokenValue = null;
                try {
                    tokenValue = new String(base64Byte, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                cookieUtils.setCookie(response, cookieName, tokenValue);
            }
            // 重定向到主页
            response.setStatus(302);
            response.setHeader("location", "/index");
            return null;
        } else {
            modelMap.put("msg", result.getMsg());
            return new ModelAndView("account/login", modelMap);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        //invalidate session
        //request.getSession().invalidate();

        //invalidate context
        LoginContext.remove();

        // invalidate cookie
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setDomain(cookieDomain);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.setStatus(302);
        response.setHeader("location", "login");

        return null;
    }
}
