package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.WalletService;
import com.nfinity.util.JwtUtil;
import com.nfinity.vo.CeWithdrawVO;
import com.nfinity.vo.WalletVO;
import com.nfinity.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/nft-business/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public Result<WalletVO> getWalletFinance(@RequestHeader("Authentication") String token){
        Long userId = (Long) jwtUtil.validateToken(token).get("id");
        WalletVO vo = walletService.getWalletFinance(userId);
        return Result.succeed(ErrorCode.OK, vo);
    }

    @GetMapping("/address/types/{type}")
    public Result<String> getChainAddress(@RequestHeader("Authentication") String token, @PathVariable String type){
        Long userId = (Long) jwtUtil.validateToken(token).get("id");
        String address = walletService.getChainAddress(userId, type);
        return Result.succeed(ErrorCode.OK, address);
    }

    @PostMapping("/withdrawal")
    public Result<Long> withdraw(@RequestHeader("Authentication") String token, @Valid @RequestBody CeWithdrawVO vo){
        Long userId = (Long) jwtUtil.validateToken(token).get("id");
        return Result.succeed(ErrorCode.OK, userId);
    }
}
