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
    public Result<WalletVO> getWalletFinance(@RequestHeader("Authorization") String token){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        WalletVO vo = walletService.getWalletFinance(userId);
        return Result.succeed(ErrorCode.OK, vo);
    }

    @GetMapping("/{type}/{coin}")
    public Result<PortfolioVO> getWalletByTypeAndCoin(@RequestHeader("Authorization") String token,
                                                   @PathVariable String type, @PathVariable String coin){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        PortfolioVO vo = walletService.getWalletByTypeAndCoin(userId, type, coin);
        return Result.succeed(ErrorCode.OK, vo);
    }

    @GetMapping("/address/{type}/{coin}")
    public Result<String> getChainAddress(@RequestHeader("Authorization") String token, @PathVariable String type,
                                          @PathVariable String coin){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        String address = walletService.getChainAddress(userId, type, coin);
        return Result.succeed(ErrorCode.OK, address);
    }

    @PostMapping("/withdrawal")
    public Result<Long> withdraw(@RequestHeader("Authorization") String token, @Valid @RequestBody CeWithdrawVO vo){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        vo.setUserId(userId);
        Long id = walletService.withdraw(vo);
        return Result.succeed(ErrorCode.OK, id);
    }

    @GetMapping("/transaction-history")
    public Result<PageModel<ChainBillVO>> getTransactionHistory(@RequestHeader("Authorization") String token,
                                                                @RequestParam(required = false, defaultValue = "1") int page,
                                                                @RequestParam(required = false, defaultValue = "6") int size,
                                                                @RequestParam(name = "tx_time", required = false, defaultValue = "5") int txTime,
                                                                @RequestParam(required = false, defaultValue = "all") String coin,
                                                                @RequestParam(name = "tx_type", required = false, defaultValue = "6") int txType){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        PageModel<ChainBillVO> pageModel = walletService.getTransactionHistory(userId, page, size, txTime, coin, txType);
        return Result.succeed(ErrorCode.OK, pageModel);
    }
}
