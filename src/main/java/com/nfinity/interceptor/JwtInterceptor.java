package com.nfinity.interceptor;

import com.nfinity.exception.AuthException;
import com.nfinity.util.JwtUtil;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@CrossOrigin
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if("OPTIONS".equals(request.getMethod())){
            return true;
        }
        String token = request.getHeader("token");
        System.out.println("token = " + token);

        try{
            JwtUtil.validateToken(token);
            return true;
        }catch (SignatureException e){
            throw new AuthException("Invalid JWT signature");
        }catch (MalformedJwtException e){
            throw new AuthException("Invalid JWT token");
        }catch (ExpiredJwtException e){
            throw new AuthException("Expire JWT token");
        }catch (UnsupportedJwtException e){
            throw new AuthException("Unsupported JWT token");
        }catch (IllegalArgumentException e){
            throw new AuthException("JWT token compact of handler are invalid");
        }
    }
}
