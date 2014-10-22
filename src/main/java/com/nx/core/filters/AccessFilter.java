package com.nx.core.filters;

import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by neal.xu on 2014/10/15.
 */
public class AccessFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.info("Access Host {}", WebUtils.getPathWithinApplication((HttpServletRequest) request));
        chain.doFilter(request, response);
    }
}
