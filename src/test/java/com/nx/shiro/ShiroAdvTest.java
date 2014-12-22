package com.nx.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Neal on 2014/12/22.
 */
public class ShiroAdvTest {
    private final Logger logger = LoggerFactory.getLogger(ShiroAdvTest.class);

    @Test
    public void test1() {
        Factory<SecurityManager> securityManagerFactory = new IniSecurityManagerFactory("classpath:shiro-adv-test.ini");
        SecurityManager securityManager = securityManagerFactory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();


        subject.logout();
    }

}
