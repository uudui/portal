package com.nx.core.web.controller;

import com.nx.core.exceptions.CaptchaException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Neal on 12/21 021.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping
    public ModelAndView showLoginForm(HttpServletRequest req) {
        String exceptionClassName = (String) req.getAttribute("shiroLoginFailure");
        String error = null;
        if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
            error = "UnknownAccount";
        } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
            error = "Use name / password error";
        } else if (CaptchaException.class.getName().equals(exceptionClassName)) {
            error = "captcha error";
        } else if (exceptionClassName != null) {
            error = "other error ：" + exceptionClassName;
        }
        Map<String, String> model = new HashMap<>();
        model.put("error", error);
        model.put("username", SecurityUtils.getSubject().getPrincipal().toString());
        return new ModelAndView("login", model);
    }
}
