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
@RequestMapping("/nft-business/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final JwtUtil jwtUtil;

    @PostMapping("/user")
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

    @PostMapping("/user/emails/{email}/verification-codes/{code}/types/{type}")
    public Result<Long> verifyCode( @PathVariable String email, @PathVariable String code, @PathVariable int type){
        Long userId = userService.checkVerificationCode(email, code, type);
        return Result.succeed(ErrorCode.OK, userId);
    }

    @PostMapping("/user/emails/{email}")
    public Result<Long> sendEmail(@PathVariable String email){
        Long userId = userService.sendEmail(email);
        return Result.succeed(ErrorCode.OK, userId);
    }

    @PostMapping("/user/password")
    public Result<Long> resetPassword(@RequestBody UserVO vo) throws Exception {
        if(StringUtils.isBlank(vo.getEmail())){
            Result.fail(ErrorCode.ERROR.getCode(), "email must be not blank");
        }
        if(StringUtils.isBlank(vo.getPassword())){
            Result.fail(ErrorCode.ERROR.getCode(), "password must be not blank");
        }
        Long userId = userService.resetPassword(vo);
        return Result.succeed(ErrorCode.OK, userId);
    }

    @PatchMapping("/user")
    public Result<Long> editProfile(@RequestHeader("Authentication") String token, @RequestBody UserVO vo) throws Exception {
        Long id = (Long) jwtUtil.validateToken(token).get("id");
        vo.setId(id);
        Long userId = userService.editProfile(vo);
        return Result.succeed(ErrorCode.OK, userId);
    }
}
