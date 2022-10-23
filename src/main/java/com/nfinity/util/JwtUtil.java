package com.nfinity.util;

import com.nfinity.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    @Value("${jwt.secret.key}")
    private static String secretKey;

    public static String generateToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(Instant.now().toEpochMilli() + Duration.ofMinutes(30).toMillis()))//expire time is 30min
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public static Claims validateToken(String token) throws AuthException {
        return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
    }
}
