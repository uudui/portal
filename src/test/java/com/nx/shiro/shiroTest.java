package com.nx.shiro;

import junit.framework.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Neal on 2014/12/22.
 */
public class ShiroTest {
    private final Logger logger = LoggerFactory.getLogger(ShiroTest.class);

    @Before
    public void initSecurityManager() {
        Factory<SecurityManager> securityManagerFactory = new IniSecurityManagerFactory("classpath:shiro-test.ini");
        SecurityManager securityManager = securityManagerFactory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
    }

    @Test
    public void begin() {
        logger.info("begin!");
    }

    @Test
    public void testSubject() {
        Subject currentUser = SecurityUtils.getSubject();

        Session session = currentUser.getSession();
        logger.info("Session Id : " + currentUser.getSession().getId());

        session.setAttribute("testKey", "test");

        String value = (String) session.getAttribute("testKey");
        if (value.equals("test")) {
            logger.info("Retrieved the correct vlaue! [{}]", value);
        }
    }

    @Test
    public void testAuthenticated() {
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("neal", "password");
            try {
                currentUser.login(usernamePasswordToken);
            } catch (UnknownAccountException e) {
                logger.error("UnknownAccount");
            } catch (IncorrectCredentialsException e) {
                logger.error("Incorrect Credential");
            } catch (AuthenticationException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (currentUser.isAuthenticated()) {
            logger.info(currentUser.getPrincipal().toString() + " authenticated pass");
        } else {
            logger.info("authenticated fail");
        }

        currentUser.logout();
    }

    @Test
    public void testNealAccount() {
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("neal", "password");
        currentUser.login(usernamePasswordToken);

        //check role
        if (currentUser.hasRole("user1")) {
            logger.info("neal has role user1");
        }

        //check roles
        List<String> roleIdentifiers = Arrays.asList("user", "user1", "user2", "user3");
        boolean[] booleans = currentUser.hasRoles(roleIdentifiers);
        Assert.assertFalse(booleans[0]);
        Assert.assertTrue(booleans[1]);
        Assert.assertTrue(booleans[2]);
        Assert.assertTrue(booleans[3]);

        boolean b = currentUser.hasAllRoles(roleIdentifiers);
        Assert.assertFalse(b);


        //check permission
        Assert.assertTrue(currentUser.isPermitted("login"));
        Assert.assertTrue(currentUser.isPermitted("user:create"));
        Assert.assertTrue(currentUser.isPermitted("user:update"));
        Assert.assertTrue(currentUser.isPermitted("user:remove"));

        //logout
        currentUser.logout();
    }

    @Test
    public void testAdminAccount() {
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("admin", "password");
        currentUser.login(usernamePasswordToken);

    }
}