package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.SocialService;
import com.nfinity.util.JwtUtil;
import com.nfinity.vo.Result;
import com.nfinity.vo.SocialVO;
import com.nfinity.vo.TwitterUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nft-business/v1/social")
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;
    private final JwtUtil jwtUtil;

    @GetMapping("/twitter/users/{usernames}")
    public Result<List<TwitterUserVO>> lookupTwitterUsers(@PathVariable String usernames){
        List<TwitterUserVO> twitterVOS = socialService.lookupTwitterUsers(usernames);
        return Result.succeed(ErrorCode.OK, twitterVOS);
    }

    @GetMapping("/twitter/engagement")
    public Result<List<SocialVO>> getTwitterEngagement(@RequestHeader("Authorization") String token){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        List<SocialVO> vo = socialService.getTwitterEngagement(userId);
        return Result.succeed(ErrorCode.OK, vo);
    }
}
