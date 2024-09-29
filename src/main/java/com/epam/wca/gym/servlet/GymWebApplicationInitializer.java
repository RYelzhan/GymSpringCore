package com.epam.wca.gym.servlet;

import com.epam.wca.gym.config.AppConfig;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class GymWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        // Create Spring application context
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);

        //add ContextLoaderListener to the ServletContext which will be responsible to load the application context
        servletContext.addListener(new ContextLoaderListener(context));

        // Register DispatcherServlet
        ServletRegistration.Dynamic registration =
                servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        registration.setLoadOnStartup(1);
        registration.addMapping("/gym/*"); // Map all requests to the DispatcherServlet

        // Register the Spring-managed filter for authentication
        FilterRegistration.Dynamic authenticationFilter = servletContext.addFilter("authenticationFilter",
                new DelegatingFilterProxy("authenticationFilter"));
        authenticationFilter.addMappingForUrlPatterns(null, false, "/gym/*");

        // Register the Spring-managed filter for access logging
        FilterRegistration.Dynamic loggerFilter = servletContext.addFilter("loggerFilter",
                new DelegatingFilterProxy("loggerFilter"));
        loggerFilter.addMappingForUrlPatterns(null, false, "/gym/*");

        //add specific encoding (e.g. UTF-8) via CharacterEncodingFilter
        FilterRegistration.Dynamic encodingFilter =
                servletContext.addFilter("encoding-filter", new CharacterEncodingFilter());
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(null, true, "/gym/*");
    }
}
