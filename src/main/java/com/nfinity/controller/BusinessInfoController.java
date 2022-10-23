package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.BusinessInfoService;
import com.nfinity.util.JwtUtil;
import com.nfinity.vo.BusinessInfoVO;
import com.nfinity.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nft-business/v1")
@RequiredArgsConstructor
public class BusinessInfoController {
    private final BusinessInfoService businessInfoService;

    @PostMapping("/business-info")
    public Result<Long> createBusinessInfo(@RequestHeader("Authentication") String token, @RequestBody BusinessInfoVO vo){
        Long id = (Long) JwtUtil.validateToken(token).get("id");
        vo.setUserId(id);
        Long businessId = businessInfoService.createBusinessInfo(vo);
        return Result.succeed(ErrorCode.OK, businessId);
    }
}
