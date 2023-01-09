package com.trading212.judge.util.path;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class ResourcePathUtil {

    public ResourcePathUtil() {
    }

    public URI buildResourcePath(HttpServletRequest httpServletRequest, Integer id) {
        String baseUrl = ServletUriComponentsBuilder.fromContextPath(httpServletRequest).build().toUriString();
        baseUrl = baseUrl + httpServletRequest.getServletPath();

        return URI.create(baseUrl + "/" + id);
    }
}
