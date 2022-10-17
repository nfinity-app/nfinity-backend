package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.enums.LoginType;
import com.nfinity.service.UserService;
import com.nfinity.vo.Result;
import com.nfinity.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/nft-business/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user")
    public Result<Long> registerOrLogin(@Valid @RequestBody UserVO vo) throws Exception {
        Long userId;
        if(LoginType.REGISTER.getValue() == vo.getType()){
            if(StringUtils.isBlank(vo.getEmail())){
                return Result.fail(ErrorCode.ERROR.getCode(), "The email is empty");
            }
            userId = userService.register(vo);
        }else if(LoginType.LOGIN.getValue() == vo.getType()){
            if(StringUtils.isBlank(vo.getEmail()) || StringUtils.isBlank(vo.getUsername())){
                return Result.fail(ErrorCode.ERROR.getCode(), "The email or user name is empty");
            }
            userId = userService.login(vo);
        }else{
            return Result.fail(ErrorCode.ERROR.getCode(), "type is incorrect");
        }
        return Result.succeed(ErrorCode.OK, userId);
    }

    @GetMapping("users/{username}/emails/{email}/verification-codes/{code}")
    public Result<Long> verifyCode(@PathVariable String username, @PathVariable String email,
                                   @PathVariable String code){
        Long userId = userService.checkVerificationCode(username, email, code);
        return Result.succeed(ErrorCode.OK, userId);
    }
}
