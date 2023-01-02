package com.trading212.judge.Server.util.jwt;

import com.trading212.judge.Server.model.dto.UserAccessToken;

import java.util.List;

public interface JWTUtil {

    String createAccessToken(String sub, List<String> roles);

    boolean isAccessTokenValid(String accessToken);

    UserAccessToken decodeAccessToken(String accessToken, String rolePrefix);
}
