package com.nx;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Neal on 12/25 025.
 */
public class UserRealm implements Realm {
    private final Logger logger = LoggerFactory.getLogger(UserRealm.class);
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    @Override
    public String getName() {
        return "UserRealm";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        logger.info("Realm#{} login user#{}", getName(), username);
        if (!"user".equals(username)) {
            throw new UnknownAccountException();
        }
        if (!"password".equals(password)) {
            throw new IncorrectCredentialsException();
        }
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}

