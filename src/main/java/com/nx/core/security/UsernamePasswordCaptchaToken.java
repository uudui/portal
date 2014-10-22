package com.nx.core.security;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Created by Neal on 10/19 019.
 */
public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {
    private String captcha;

    public UsernamePasswordCaptchaToken(String username, String password, boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        setCaptcha(captcha);
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
