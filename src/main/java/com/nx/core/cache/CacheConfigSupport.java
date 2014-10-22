package com.nx.core.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

/**
 * Created by Neal on 10/20 020.
 */
@Configuration
@EnableCaching
public class CacheConfigSupport {

    @Bean
    public net.sf.ehcache.CacheManager ehcacheManager() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new PathResource("/ehcache.xml"));
        factoryBean.setShared(true);
        return factoryBean.getObject();
    }

    @Bean
    public EhCacheCacheManager springCacheManager() {
        return new EhCacheCacheManager(ehcacheManager());
    }

    @Bean(name = "springCache")
    public org.apache.shiro.cache.CacheManager cacheManager() {
        SpringCacheManagerWrapper springCacheManagerWrapper = new SpringCacheManagerWrapper(springCacheManager());
        return springCacheManagerWrapper;
    }

}
