package com.nfinity.service;

import com.nfinity.vo.UserVO;

public interface UserService {
    Long register(UserVO vo) throws Exception;

    String login(UserVO vo) throws Exception;

    Long checkVerificationCode(String email, String code);

    Long resetPassword(UserVO vo);

    Long sendEmail(String email);

    Long editProfile(UserVO vo) throws Exception;
}
