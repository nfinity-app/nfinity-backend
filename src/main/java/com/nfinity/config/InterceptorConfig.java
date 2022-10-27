package com.nfinity.config;

import com.nfinity.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        List<String> pathPatterns = new ArrayList<>();
        pathPatterns.add("/**");
        List<String> excludePathPatterns = new ArrayList<>();
        excludePathPatterns.add("/nft-business/v1/user/emails/*/verification-codes/*/types/*");
        excludePathPatterns.add("/nft-business/v1/user/emails/*");
        excludePathPatterns.add("/nft-business/v1/user/password");
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns(pathPatterns)
                .excludePathPatterns(excludePathPatterns);
    }
}
