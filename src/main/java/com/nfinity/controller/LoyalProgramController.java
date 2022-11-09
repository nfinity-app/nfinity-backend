package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.LoyaltyProgramService;
import com.nfinity.util.JwtUtil;
import com.nfinity.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/nft-business/v1/loyalty-program")
@RequiredArgsConstructor
public class LoyalProgramController {
    private final JwtUtil jwtUtil;

    private final LoyaltyProgramService loyaltyProgramService;

    @PostMapping("/draft")
    public Result<Long> saveLoyaltyProgram(@RequestHeader("Authorization") String token, @RequestBody LoyaltyProgramVO vo) {
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        vo.setUserId(userId);
        Long programId = loyaltyProgramService.saveLoyaltyProgram(vo);
        return Result.succeed(ErrorCode.OK, programId);
    }

    @PostMapping
    public Result<Long> createLoyaltyProgram(@RequestHeader("Authorization") String token, @Valid @RequestBody LoyaltyProgramVO vo){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        vo.setUserId(userId);
        Long programId = loyaltyProgramService.createLoyaltyProgram(vo);
        return Result.succeed(ErrorCode.OK, programId);
    }

    @GetMapping
    public Result<LoyaltyProgramCollectionsVO> getLoyaltyProgram(@RequestHeader("Authorization") String token) {
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        LoyaltyProgramCollectionsVO vo = loyaltyProgramService.getLoyaltyProgram(userId);
        return Result.succeed(ErrorCode.OK, vo);
    }

    @PatchMapping
    public Result<Long> editLoyaltyProgram(@RequestHeader("Authorization") String token, @RequestBody LoyaltyProgramVO vo) {
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        vo.setUserId(userId);
        Long programId = loyaltyProgramService.editLoyaltyProgram(vo);
        return Result.succeed(ErrorCode.OK, programId);
    }

    @GetMapping("/tier/members")
    public Result<PageModel<TierUserVO>> getTierMembers(@RequestHeader("Authorization") String token){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        PageModel<TierUserVO> tierUserVOS = loyaltyProgramService.getTierMembers(userId);
        return Result.succeed(ErrorCode.OK, tierUserVOS);
    }
}
