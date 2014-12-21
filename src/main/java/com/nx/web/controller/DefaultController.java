package com.nx.web.controller;

import com.nx.core.security.CustomSecurityRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Neal on 2014-10-09.
 */
@Controller
public class DefaultController {
    private CustomSecurityRealm customSecurityRealm;

    @RequestMapping("/**")
    public String notFound() {
        return "errors/404";
    }

    @RequestMapping("/manage/cacheAllClear")
    public String clear() {
        customSecurityRealm.clearAllAuthCache();
        return "login";
    }

    @RequestMapping("/extends/icons")
    public String viewIcons() {
        return "extends/icons";
    }

    @Autowired
    public void setCustomSecurityRealm(CustomSecurityRealm customSecurityRealm) {
        this.customSecurityRealm = customSecurityRealm;
    }
}

