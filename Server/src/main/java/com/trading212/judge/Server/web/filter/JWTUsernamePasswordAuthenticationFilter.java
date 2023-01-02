package com.trading212.judge.Server.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading212.judge.Server.util.jwt.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String FILTER_PROCESSING_URL = "/api/users/login";

    private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;

    public JWTUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, JWTUtil jwtUtil) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl(FILTER_PROCESSING_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String httpMethod = request.getMethod();

        if (!httpMethod.equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Http method is not POST!");
        }

        UserLoginModel userLoginModel = parseJson(request);

        if (userLoginModel == null) {
            throw new AuthenticationServiceException("Invalid JSON data!");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userLoginModel.username(), userLoginModel.password());

        return getAuthenticationManager().authenticate(token);
    }

    private UserLoginModel parseJson(HttpServletRequest request) {
        UserLoginModel userLoginModel = null;

        try (BufferedReader bodyReader = request.getReader()) {
            String json = bodyReader.lines().collect(Collectors.joining());
            userLoginModel = objectMapper.readValue(json, UserLoginModel.class);
        } catch (IOException ignored) {

        }

        return userLoginModel;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetails principal = (UserDetails) authResult.getPrincipal();
        List<String> authorities = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String accessToken = jwtUtil.createAccessToken(principal.getUsername(), authorities);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new AccessTokenHolder(accessToken)));
        response.getWriter().flush();
    }

    private record AccessTokenHolder(String accessToken) {

    }

    private record UserLoginModel(
            String username,
            String password) {

    }
}
