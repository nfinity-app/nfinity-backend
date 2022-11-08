package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.SocialService;
import com.nfinity.vo.Result;
import com.nfinity.vo.TwitterVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nft-business/v1/social")
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;

    @GetMapping("/twitter/users/{usernames}")
    public Result<List<TwitterVO.TwitterUserVO>> lookupTwitterUsers(@PathVariable String usernames){
        List<TwitterVO.TwitterUserVO> twitterVOS = socialService.lookupTwitterUsers(usernames);
        return Result.succeed(ErrorCode.OK, twitterVOS);
    }



}
