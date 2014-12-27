package com.nx.shiro;

import junit.framework.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Neal on 2014/12/22.
 */
public class ShiroRealmTest {
    private final Logger logger = LoggerFactory.getLogger(ShiroRealmTest.class);

    @Test
    public void testCustomRealm() {
        login("classpath:shiro-realm.ini", "admin", "password");
    }

    @Test
    public void testMultiRealm() {
        login("classpath:shiro-multi-realm.ini", "admin", "password");
    }

    @Test
    public void testLeastOneSuccess() {
        login("classpath:shiro-atLeastOne-success.ini", "admin", "password");
    }

    @Test
    public void testAllSuccess() {
        login("classpath:shiro-all-success.ini", "admin", "password");
    }

    @Test
    public void testAllFail() {
        login("classpath:shiro-all-fail.ini", "admin", "password");
    }

    @Test
    public void testCredential() {
        login("classpath:shiro-Credentials.ini", "neal", "password");
    }


    private void login(String iniPath, String userName, String password) {
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(iniPath);

        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

        try {
            subject.login(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(true, subject.isAuthenticated());
        subject.logout();
    }


}
