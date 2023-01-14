package com.trading212.judge.web.filter;

import com.trading212.judge.model.dto.user.UserAccessToken;
import com.trading212.judge.util.jwt.JWTUtil;
import com.trading212.judge.web.exception.AuthorizationHeaderNotValidException;
import com.trading212.judge.web.exception.InvalidAccessTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String EMPTY_PASSWORD = "";

    private final JWTUtil jwtUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JWTFilter(JWTUtil jwtUtil, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtUtil = jwtUtil;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = isAuthorizationHeaderValid(authorizationHeader, request, response);

        if (accessToken == null) {
            return;
        }

        UserAccessToken userAccessToken = jwtUtil.decodeAccessToken(accessToken);

        UserDetails user = User.builder()
                .username(userAccessToken.username())
                .password(EMPTY_PASSWORD)
                .roles(userAccessToken.roles().toArray(String[]::new))
                .build();


        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                user, EMPTY_PASSWORD, user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, RuntimeException exception) {
        handlerExceptionResolver.resolveException(request, response, null, exception);
    }

    private String isAuthorizationHeaderValid(String authorizationHeader, HttpServletRequest request, HttpServletResponse response) {
        if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
            sendErrorResponse(request, response, new AuthorizationHeaderNotValidException());
            return null;
        }

        String accessToken = authorizationHeader.split(BEARER_PREFIX)[1].trim();

        if (accessToken.isEmpty()) {
            sendErrorResponse(request, response, new AuthorizationHeaderNotValidException());
            return null;
        }

        boolean isAccessTokenValid = jwtUtil.isAccessTokenValid(accessToken);

        if (!isAccessTokenValid) {
            sendErrorResponse(request, response, new InvalidAccessTokenException());
            return null;
        }

        return accessToken;
    }
}
