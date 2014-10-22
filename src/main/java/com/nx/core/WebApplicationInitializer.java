package com.nx.core;

import com.nx.core.filters.JCaptchaFilter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by Neal on 2014-09-28.
 */
public class WebApplicationInitializer implements org.springframework.web.WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfiguration.class);

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));

        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.setServletContext(container);
        dispatcherContext.setParent(rootContext);
        dispatcherContext.register(WebMvcConfiguration.class);

        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        // Register shiro filter
        DelegatingFilterProxy shiroFilter = new DelegatingFilterProxy("shiroFilterBean", dispatcherContext);
        shiroFilter.setTargetFilterLifecycle(true);
        container.addFilter("shiroFilter", shiroFilter).addMappingForUrlPatterns(null, false, "/*");

        // Register jCaptcha filter
        container.addFilter("jCaptchaFilter", JCaptchaFilter.class).addMappingForUrlPatterns(null, false, "/captchaImg");
    }
}
