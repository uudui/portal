package com.nx.core.security;

import com.nx.core.cache.CacheConfigSupport;
import com.nx.core.filters.AccessFilter;
import com.nx.core.filters.FormAuthenticationCaptchaFilter;
import com.nx.core.filters.JCaptchaFilter;
import com.nx.core.web.WebSessionManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neal.xu on 2014/10/10.
 */
@Configuration
public class SecurityConfiguration extends CacheConfigSupport {

    @Bean
    public RetryLimitHashedCredentialsMatcher credentialsMatcher() {
        RetryLimitHashedCredentialsMatcher matcher = new RetryLimitHashedCredentialsMatcher(cacheManager());
        matcher.setHashAlgorithmName("MD5");
        matcher.setHashIterations(1);
        matcher.setStoredCredentialsHexEncoded(true);
        return matcher;
    }

    @Bean
    public CustomSecurityRealm customSecurityRealm() {
        CustomSecurityRealm customSecurityRealm = new CustomSecurityRealm();
        customSecurityRealm.setCredentialsMatcher(credentialsMatcher());
        customSecurityRealm.setCachingEnabled(true);
        customSecurityRealm.setAuthenticationCachingEnabled(true);
        customSecurityRealm.setAuthenticationCacheName("authenticationCache");
        customSecurityRealm.setAuthorizationCachingEnabled(true);
        customSecurityRealm.setAuthorizationCacheName("authorizationCache");
        return customSecurityRealm;
    }

    @Bean
    public EnterpriseCacheSessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        sessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        return sessionDAO;
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCipherKey(org.apache.shiro.codec.Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        rememberMeManager.setCookie(rememberMeCookie());
        return rememberMeManager;
    }

    @Bean
    public SessionValidationScheduler sessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setInterval(3600000);
        return scheduler;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        WebSessionManager sessionManager = new WebSessionManager();
        sessionManager.setSessionDAO(sessionDAO());

        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationScheduler(sessionValidationScheduler());


        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie());

        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);

        ((ExecutorServiceSessionValidationScheduler) sessionManager.getSessionValidationScheduler()).setSessionManager(sessionManager);
        return sessionManager;
    }

    @Bean
    public WebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customSecurityRealm());
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(new Object[]{securityManager()});
        return methodInvokingFactoryBean;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean(name = "jCaptchaFilter")
    public Filter jCaptchaFilter() {
        return new JCaptchaFilter();
    }

    @Bean(name = "shiroFilterBean")
    public ShiroFilterFactoryBean shiroFilterBean() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager());
        shiroFilter.setLoginUrl("/login");
        shiroFilter.setSuccessUrl("/message");
        shiroFilter.setUnauthorizedUrl("/unauthorized");

        //Custom Filter
        addCustomFilters(shiroFilter);

        //Definitions
        setDefinitions(shiroFilter);

        return shiroFilter;
    }

    private void addCustomFilters(ShiroFilterFactoryBean shiroFilter) {
        shiroFilter.getFilters().put("accessFilter", new AccessFilter());

        FormAuthenticationCaptchaFilter formAuthenticationCaptchaFilter = new FormAuthenticationCaptchaFilter();
        formAuthenticationCaptchaFilter.setCaptchaParam("jCaptcha");
        shiroFilter.getFilters().put("authc", formAuthenticationCaptchaFilter);

    }

    private void setDefinitions(ShiroFilterFactoryBean shiroFilter) {
        Map<String, String> definitionsMap = new HashMap<>();
        definitionsMap.put(FormAuthenticationCaptchaFilter.DEFAULT_CAPTCHA_URL, "anon");
        definitionsMap.put("/login", "authc");
        definitionsMap.put("/unauthorized", "anon");
        definitionsMap.put("/logout", "logout");
        definitionsMap.put("/resources/**", "anon");

        //If need config path , must add accessFilter
//        definitionsMap.put("/message/**", "accessFilter,authc");
        definitionsMap.put("/**", "accessFilter,user");
        shiroFilter.setFilterChainDefinitionMap(definitionsMap);
    }
}
