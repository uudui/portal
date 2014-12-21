package com.nx.core.filters;

import com.nx.core.utils.CaptchaUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Neal on 10/19 019.
 */
public class CaptchaFilter implements Filter {
    public static final String KEY_CAPTCHA = "SE_KEY_CAPTCHA_CODE";

    private ConfigurableCaptchaService captchaService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.captchaService = CaptchaUtils.generateCaptchaService();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        try {
            Session session = SecurityUtils.getSubject().getSession();

            String token = EncoderHelper.getChallangeAndWriteImage(captchaService, "png", res.getOutputStream());
            session.removeAttribute(KEY_CAPTCHA);
            session.setAttribute(KEY_CAPTCHA, token);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        captchaService = null;
    }
}
