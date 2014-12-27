package com.nx.shiro.realms;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Neal on 2014/12/22.
 */
public class MyRealm1 implements Realm {

    private final Logger logger = LoggerFactory.getLogger(MyRealm1.class);

    @Override
    public String getName() {
        return "realm1";
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
        if (!"admin".equals(username)) {
            throw new UnknownAccountException();
        }
        if (!"password".equals(password)) {
            throw new IncorrectCredentialsException();
        }
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
