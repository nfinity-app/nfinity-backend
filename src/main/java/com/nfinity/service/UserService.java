package com.nfinity.service;

import com.nfinity.vo.UserVO;

public interface UserService {
    Long register(UserVO vo) throws Exception;

    String login(UserVO vo) throws Exception;

    void sendEmail(String email, int type);

    String checkVerificationCode(Long userId, String email, String code, int type);

    Long resetPassword(UserVO vo) throws Exception;

    Long editProfile(UserVO vo) throws Exception;

    UserVO getProfile(Long userId);

    String getQRCode(Long userId);

    void verifyAuthenticatorCode(Long userId, long code);
}
