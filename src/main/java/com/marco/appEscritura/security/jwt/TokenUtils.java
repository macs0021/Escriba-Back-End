package com.marco.appEscritura.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;

public class TokenUtils {

    private static String ACCESS_TOKEN_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256).toString();
    private static Long ACCESS_TOKEN_VALIDITY_SECONDS = 2_592_000L;

    public static String createToken(String username) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().
                    setSigningKey(ACCESS_TOKEN_SECRET.getBytes()).build()
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
        } catch (JwtException e) {
            return null;

        }
    }
}