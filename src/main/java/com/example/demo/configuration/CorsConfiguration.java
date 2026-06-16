package com.example.demo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "https://www.jachcloud.pe",  // ✅ dominio principal en producción
                        "https://jachcloud.pe",      // ✅ sin www (por si Cloudflare lo reescribe)
                        "http://10.69.1.137",        // para pruebas locales
                        "http://localhost:*",
                        "http://localhost:*",
                        "*:8080", "*:8081", "*:4200", "*:4300", "*:5500"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

