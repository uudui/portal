package com.nx.web.controller.system;

import com.nx.domain.security.User;
import com.nx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Neal on 12/21 021.
 */
@Controller
@RequestMapping("/system/user")
public class UserController {

    private UserService userService;

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String setup() {
        return "/system/user/addUser";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String submit(@ModelAttribute User user) {
        return "redirect:list";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "/system/user/listUsers";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove() {
        return "redirect:list";
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
