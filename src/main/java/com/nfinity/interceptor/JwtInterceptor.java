package com.nfinity.interceptor;

import com.nfinity.enums.ErrorCode;
import com.nfinity.exception.AuthException;
import com.nfinity.util.JwtUtil;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@CrossOrigin
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    private static final String userUri = "/nft-business/v1/user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if("OPTIONS".equals(request.getMethod())){
            return true;
        }

        if(userUri.equals(request.getRequestURI()) && "POST".equals(request.getMethod())){
            return true;
        }

        String authorization = request.getHeader("Authorization");

        try{
            jwtUtil.validateToken(authorization);
            return true;
        }catch (MalformedJwtException e){
            log.error("Invalid JWT token.", e);
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        }catch (ExpiredJwtException e){
            log.error("Expired JWT token.", e);
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        }catch (UnsupportedJwtException e){
            log.error("Unsupported JWT token.", e);
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        }catch (IllegalArgumentException e){
            log.error("JWT token compact of handler are invalid.", e);
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        }
    }
}
