package com.nx.web.controller;

import com.nx.core.exceptions.CaptchaException;
import com.nx.core.security.CustomSecurityRealm;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Neal on 2014-10-09.
 */
@Controller
public class DefaultController {
    private CustomSecurityRealm customSecurityRealm;

    @RequestMapping("/**")
    public String notFound() {
        return "errors/404";
    }

    @RequestMapping("/login")
    public String showLoginForm(HttpServletRequest req, Model model) {
        String exceptionClassName = (String) req.getAttribute("shiroLoginFailure");
        String error = null;
        if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        } else if (CaptchaException.class.getName().equals(exceptionClassName)) {
            error = "验证码错误";
        } else if (exceptionClassName != null) {
            error = "其他错误：" + exceptionClassName;
        }
        model.addAttribute("error", error);
        return "login";
    }

    @RequestMapping("/manage/cacheAllClear")
    public String clear() {
        customSecurityRealm.clearAllAuthCache();
        return "login";
    }

    @Autowired
    public void setCustomSecurityRealm(CustomSecurityRealm customSecurityRealm) {
        this.customSecurityRealm = customSecurityRealm;
    }
}

