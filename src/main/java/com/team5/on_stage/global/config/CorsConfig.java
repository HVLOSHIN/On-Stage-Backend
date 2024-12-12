package com.team5.on_stage.global.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

public class CorsConfig implements CorsConfigurationSource {

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5000",
                "http://localhost:8080",
                "http://59.8.139.239",
                "http://59.8.139.239:3000",
                "http://59.8.139.239:5000",
                "http://www.on-stage.link",
                "http://www.on-stage.link:3000",
                "http://www.on-stage.link:5000"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        return config;
    }
}
