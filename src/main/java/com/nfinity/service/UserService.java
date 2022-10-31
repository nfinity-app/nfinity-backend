package com.nfinity.service;

import com.nfinity.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    Long register(UserVO vo) throws Exception;

    String login(UserVO vo) throws Exception;

    Object checkVerificationCode(String email, String code, int type);

    Long resetPassword(UserVO vo) throws Exception;

    Long sendEmail(String email);

    Long editProfile(UserVO vo) throws Exception;

    Long uploadPhoto(List<MultipartFile> multipartFile, Long id) throws Exception;
}
