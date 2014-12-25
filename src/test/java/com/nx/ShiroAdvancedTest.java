package com.nx;

import junit.framework.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Neal on 12/25 025.
 */
public class ShiroAdvancedTest {
    Logger logger = LoggerFactory.getLogger(ShiroAdvancedTest.class);

    @Test
    public void testIni() {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-advanced.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();



        UsernamePasswordToken passwordToken = new UsernamePasswordToken("admin", "password");
        try {
            subject.login(passwordToken);
        } catch (UnknownAccountException uae) {
            uae.printStackTrace();
        } catch (IncorrectCredentialsException ice) {
            ice.printStackTrace();
        } catch (LockedAccountException lae) {
            lae.printStackTrace();
        } catch (ExcessiveAttemptsException eae) {
            eae.printStackTrace();
        } catch (AuthenticationException ae) {
            ae.printStackTrace();
        }
        subject.logout();
    }

    @Test
    public void testCustomRealm() {
        Factory<org.apache.shiro.mgt.SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro-realm.ini");

        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "password");

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(true, subject.isAuthenticated());

        subject.logout();
    }

    @Test
    public void testCustomMultiRealm() {
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro-multi-realm.ini");

        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "password");

        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
            e.printStackTrace();
        }

        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录

        //6、退出
        subject.logout();
    }
}
