package com.nx.shiro.realms;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;

/**
 * Created by Neal on 2014/12/22.
 */
public class MyRealm2 implements Realm {
    private final Logger logger = LoggerFactory.getLogger(MyRealm2.class);
    private Integer age;

    @Override
    public String getName() {
        return "realm2";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return true;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        logger.info("Realm#{} auth User#{}", getName(), username);
        String password = new String((char[]) token.getCredentials());
        if (age == null || age < 18) {
            throw new InvalidParameterException("Age can't less 18");
        }
        return new SimpleAuthenticationInfo(username, password, getName());
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
