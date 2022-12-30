package com.trading212.judge.Server.config;

import com.trading212.judge.Server.web.controller.DocumentController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String ALL_SUB_ROUTES = "/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(DocumentController.Routes.BASE_ROUTE + ALL_SUB_ROUTES).permitAll()
                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .disable();

        return httpSecurity.build();
    }
}
