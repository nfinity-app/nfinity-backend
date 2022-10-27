package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.WalletService;
import com.nfinity.util.JwtUtil;
import com.nfinity.vo.*;
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
        vo.setUserId(userId);
        Long id = walletService.withdraw(vo);
        return Result.succeed(ErrorCode.OK, id);
    }

    @GetMapping("/transaction-history")
    public Result<PageModel<ChainBillVO>> getTransactionHistory(@RequestHeader("Authentication") String token,
                                                                @RequestParam(required = false, defaultValue = "1") int page,
                                                                @RequestParam(required = false, defaultValue = "5") int size){
        Long userId = (Long) jwtUtil.validateToken(token).get("id");
        PageModel<ChainBillVO> pageModel = walletService.getTransactionHistory(userId, page, size);
        return Result.succeed(ErrorCode.OK, pageModel);
    }
}
