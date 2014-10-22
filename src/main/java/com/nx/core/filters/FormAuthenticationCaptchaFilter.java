package com.nx.core.filters;

import com.nx.core.security.UsernamePasswordCaptchaToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by Neal on 10/19 019.
 */
public class FormAuthenticationCaptchaFilter extends FormAuthenticationFilter {
    public static final String DEFAULT_CAPTCHA_PARAM = "jCaptcha";
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    public static final String DEFAULT_CAPTCHA_URL = "/captchaImg";

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        //Add captcha
        String captcha = getCaptcha(request);
        return new UsernamePasswordCaptchaToken(username, password, rememberMe, host, captcha);
    }

    private String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }
}
