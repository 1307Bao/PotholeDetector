package com.example.demo.configuration;

import com.example.demo.LogCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LogCat logCat;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logCat);
    }
}