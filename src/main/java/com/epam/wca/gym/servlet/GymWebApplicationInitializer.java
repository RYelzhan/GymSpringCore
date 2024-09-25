package com.epam.wca.gym.servlet;

import com.epam.wca.gym.config.AppConfig;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class GymWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        // Create Spring application context
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);

        // Create DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);

        // Register DispatcherServlet
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/gym/*"); // Map all requests to the DispatcherServlet

        // Register the Spring-managed filter
        FilterRegistration.Dynamic springFilter = servletContext.addFilter("authenticationFilter",
                new DelegatingFilterProxy("authenticationFilter"));
        springFilter.addMappingForUrlPatterns(null, false, "/gym/*");
    }
}
