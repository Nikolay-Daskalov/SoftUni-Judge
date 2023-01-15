package com.trading212.judge.config;

import com.trading212.judge.web.controller.CodeExecutionController;
import com.trading212.judge.web.controller.DocumentController;
import com.trading212.judge.web.controller.TaskController;
import com.trading212.judge.web.controller.UserController;
import com.trading212.judge.web.filter.JWTFilter;
import com.trading212.judge.web.filter.JWTUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {

    private static final String ALL_SUB_ROUTES = "/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   JWTUsernamePasswordAuthenticationFilter authenticationFilter,
                                                   JWTFilter jwtFilter) throws Exception {
        httpSecurity
                .sessionManagement() // Set so Spring Security will not create session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests() // Set routes matchers
                .requestMatchers(UserController.Routes.BASE + UserController.Routes.REGISTER).permitAll()
                .requestMatchers(CodeExecutionController.Routes.BASE).fullyAuthenticated()
                .requestMatchers(HttpMethod.GET, DocumentController.Routes.BASE).permitAll()
                .requestMatchers(DocumentController.Routes.BASE + ALL_SUB_ROUTES).fullyAuthenticated()
                .requestMatchers(TaskController.Routes.BASE + ALL_SUB_ROUTES).fullyAuthenticated()
                .requestMatchers(CodeExecutionController.Routes.BASE + ALL_SUB_ROUTES).fullyAuthenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add Custom filters in the SecurityFilterChain
                .addFilterBefore(jwtFilter, AnonymousAuthenticationFilter.class)
                .cors() // Enable cors for Spring Security
                .and()
                .csrf() // Disable CSRF Protection (Enabled by default)
                .disable()
                .logout() // Disable Logout Filter (Enabled by default)
                .disable()
                .httpBasic() // Disable auth (Enabled by default), (Add custom auth)
                .disable();

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
