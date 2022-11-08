package com.nfinity.util;

import com.nfinity.enums.ErrorCode;
import com.nfinity.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateToken(Map<String, Object> claims){
        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(Instant.now().toEpochMilli() + Duration.ofDays(7).toMillis()))//expire time is 7 days
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Claims validateToken(String authorization) {
        if(StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")){
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        }
        String token = authorization.substring(7);
        return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
    }
}
