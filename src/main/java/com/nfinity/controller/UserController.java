package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.enums.LoginType;
import com.nfinity.exception.BusinessException;
import com.nfinity.service.UserService;
import com.nfinity.vo.Result;
import com.nfinity.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/nft-business/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user")
    public Result<Long> registerOrLogin(@Valid @RequestBody UserVO vo){
        try {
            Long userId;
            if(LoginType.REGISTER.getValue() == vo.getType()){
                userId = userService.register(vo);
            }else if(LoginType.LOGIN.getValue() == vo.getType()){
                userId = userService.login(vo);
            }else{
                throw new BusinessException(ErrorCode.NOT_FOUND.getMessage());
            }
            return Result.succeed(ErrorCode.OK, userId);
        }catch (Exception e){
            log.error("register failed.", e);
            return Result.fail(ErrorCode.ERROR.getCode(), e.getMessage());
        }
    }
}
