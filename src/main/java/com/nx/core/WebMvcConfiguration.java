package com.nx.core;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafView;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * Created by Neal on 2014-09-28.
 */
@Configurable
@EnableWebMvc
@ComponentScan(basePackages = {"com.nx.web.controller", "com.nx.core.web.controller"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    private boolean develop = true;

    @Autowired
    private FormattingConversionService mvcConversionService;

    @Bean(name = "conversionService")
    public FormattingConversionService conversionService() {
        FormattingConversionService factoryBean = new FormattingConversionService();
        return factoryBean;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/dashboard");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/").setCachePeriod(31556926);
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public TemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(!develop);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.addDialect(new ShiroDialect());
        return templateEngine;
    }

    @Bean
    public ViewResolver tilesViewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setContentType("text/html;charset=UTF-8");
        viewResolver.setOrder(Ordered.LOWEST_PRECEDENCE);
        viewResolver.setCache(!develop);
        viewResolver.setViewClass(ThymeleafView.class);
        return viewResolver;
    }

    @Bean
    public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
        return new ExceptionHandlerExceptionResolver();
    }

    @Bean
    public DomainClassConverter<?> domainClassConverter() {
        return new DomainClassConverter<>(mvcConversionService);
    }
}
