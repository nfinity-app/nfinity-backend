package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.enums.LoginType;
import com.nfinity.service.UserService;
import com.nfinity.util.JwtUtil;
import com.nfinity.vo.Result;
import com.nfinity.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/nft-business/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final JwtUtil jwtUtil;

    @PostMapping
    public Result<Object> registerOrLogin(@Valid @RequestBody UserVO vo) throws Exception {
        if(LoginType.REGISTER.getValue() == vo.getType()){
            Long userId = userService.register(vo);
            return Result.succeed(ErrorCode.OK, userId);
        }else if(LoginType.LOGIN.getValue() == vo.getType()){
            String token = userService.login(vo);
            return Result.succeed(ErrorCode.OK, token);
        }else{
            return Result.fail(ErrorCode.ERROR.getCode(), "type is incorrect");
        }
    }

    @PostMapping("/emails/{email}/verification-codes/{code}/types/{type}")
    public Result<Object> verifyCode( @PathVariable String email, @PathVariable String code, @PathVariable int type){
        if(LoginType.REGISTER.getValue() == type) {
            String token = (String) userService.checkVerificationCode(email, code, type);
            return Result.succeed(ErrorCode.OK, token);
        }else {
            Long userId = (Long) userService.checkVerificationCode(email, code, type);
            return Result.succeed(ErrorCode.OK, userId);
        }
    }

    @PostMapping("/emails/{email}")
    public Result<Long> sendEmail(@PathVariable String email){
        Long userId = userService.sendEmail(email);
        return Result.succeed(ErrorCode.OK, userId);
    }

    @PostMapping("/password")
    public Result<Long> resetPassword(@RequestBody UserVO vo) throws Exception {
        if(StringUtils.isBlank(vo.getEmail())){
            Result.fail(ErrorCode.ERROR.getCode(), "email must be not blank");
        }
        if(StringUtils.isBlank(vo.getPassword())){
            Result.fail(ErrorCode.ERROR.getCode(), "password must be not blank");
        }
        if(StringUtils.isBlank(vo.getVerificationCode())){
            Result.fail(ErrorCode.ERROR.getCode(), "verification code must be not blank");
        }
        Long userId = userService.resetPassword(vo);
        return Result.succeed(ErrorCode.OK, userId);
    }

    @GetMapping
    public Result<UserVO> getProfile(@RequestHeader("Authentication") String token){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        UserVO vo = userService.getProfile(userId);
        return Result.succeed(ErrorCode.OK, vo);
    }

    @PatchMapping
    public Result<Long> editProfile(@RequestHeader("Authentication") String token, @RequestBody UserVO vo) throws Exception {
        Long id = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        vo.setId(id);
        Long userId = userService.editProfile(vo);
        return Result.succeed(ErrorCode.OK, userId);
    }
}
