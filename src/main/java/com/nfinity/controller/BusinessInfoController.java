package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.BusinessInfoService;
import com.nfinity.util.JwtUtil;
import com.nfinity.vo.BusinessInfoVO;
import com.nfinity.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/nft-business/v1/business-info")
@RequiredArgsConstructor
public class BusinessInfoController {
    private final BusinessInfoService businessInfoService;

    private final JwtUtil jwtUtil;

    @PutMapping
    public Result<Boolean> existsBusinessInfo(@RequestHeader("Authorization") String token){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        boolean flag = businessInfoService.existsBusinessInfo(userId);
        return Result.succeed(ErrorCode.OK, flag);
    }

    @PostMapping
    public Result<Long> createBusinessInfo(@RequestHeader("Authorization") String token, @Valid @RequestBody BusinessInfoVO vo) {
        Long id = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        vo.setUserId(id);
        Long businessId = businessInfoService.createBusinessInfo(vo);
        return Result.succeed(ErrorCode.OK, businessId);
    }

    @GetMapping
    public Result<BusinessInfoVO> getBusinessInfo(@RequestHeader("Authorization") String token){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        BusinessInfoVO vo = businessInfoService.getBusinessInfo(userId);
        return Result.succeed(ErrorCode.OK, vo);
    }

    @PatchMapping
    public Result<Long> editBusinessInfo(@RequestHeader("Authorization") String token, @RequestBody BusinessInfoVO vo) {
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        vo.setUserId(userId);
        Long businessId = businessInfoService.editBusinessInfo(vo);
        return Result.succeed(ErrorCode.OK, businessId);
    }
}
