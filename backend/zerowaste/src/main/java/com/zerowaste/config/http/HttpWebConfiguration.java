package com.zerowaste.config.http;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class HttpWebConfiguration implements WebMvcConfigurer {
    @SuppressWarnings("null")
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}