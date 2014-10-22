package com.nx.core.security;

import com.nx.core.exceptions.CaptchaException;
import com.nx.core.filters.JCaptchaFilter;
import com.nx.domain.security.User;
import com.nx.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by neal.xu on 2014/10/10.
 */
public class CustomSecurityRealm extends AuthorizingRealm {
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        roles.add("admin");
        permissions.add("message");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authenticationToken;

        String captcha = token.getCaptcha();
        String exitCode = SecurityUtils.getSubject().getSession().getAttribute(JCaptchaFilter.KEY_CAPTCHA).toString();
        if (null == captcha || !captcha.equalsIgnoreCase(exitCode)) {
            throw new CaptchaException("captcha error");
        }
        User user;
        try {
            user = userService.findByName(token.getUsername());
            if (user == null) {
                throw new UnknownAccountException("account not exist");
            }
        } catch (Exception e) {
            throw new UnknownAccountException();
        }


        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getName(),
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }

    @Override
    protected void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllAuthCache() {
        clearAllCachedAuthorizationInfo();
        clearAllCachedAuthenticationInfo();

    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}