package com.nx.core.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Neal on 10/21 021.
 */
@Controller
@RequestMapping("/cacheManager")
public class CacheController {

    private CacheManager cacheManager;

    @RequestMapping(value = "/status/{cacheName}")
    public ModelAndView getCacheStatus(@PathVariable("cacheName") String cacheName) {
        ModelAndView modelAndView = new ModelAndView("/cacheStatus");
        modelAndView.addObject("status", cacheManager.getCache(cacheName));
        return modelAndView;
    }

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
