package com.trading212.judge.util.jwt.Impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.trading212.judge.model.dto.user.UserAccessToken;
import com.trading212.judge.util.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class JWTUtilImpl implements JWTUtil {


    private static final String ISS_CLAIM = "Trading212-Judge";
    private static final String ROLES_CLAIM = "roles";

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final JWTCreator.Builder JWTBuilder = JWT.create().withIssuer(ISS_CLAIM);

    public JWTUtilImpl(@Value("${jwt-secret}") String secret) {
        algorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(algorithm).withIssuer(ISS_CLAIM).build();
    }

    @Override
    public String createAccessToken(String sub, List<String> roles) {
        return JWTBuilder
                .withSubject(sub)
                .withClaim(ROLES_CLAIM, roles)
                .withExpiresAt(getExpirationTime())
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    @Override
    public boolean isAccessTokenValid(String accessToken) {
        try {
            verifier.verify(accessToken);
            return true;
        } catch (JWTVerificationException ignored) {
            return false;
        }
    }

    @Override
    public UserAccessToken decodeAccessToken(String accessToken) {
        DecodedJWT decodedJWT = JWT.decode(accessToken);
        String subject = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim(ROLES_CLAIM).asList(String.class);

        return new UserAccessToken(subject, roles);
    }

    private Instant getExpirationTime() {
        final Long amount = 20L;
        final ChronoUnit type = ChronoUnit.MINUTES;

        return Instant.now().plus(amount, type);
    }
}
