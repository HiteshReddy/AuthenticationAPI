package com.assessment.authentication.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${app.jwt.secret}")
    private String secret;

    /**
     * generates a new JWT token for the user
     *
     * @param userName
     * @return
     */
    public String generateJWTToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("UserName", userName);
        return doGenerateToken(claims);
    }

    private String doGenerateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret != null ? secret : "secret")
                .compact();
    }

}
