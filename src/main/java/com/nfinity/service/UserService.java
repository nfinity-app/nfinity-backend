package com.nfinity.service;

import com.nfinity.vo.UserVO;

public interface UserService {
    Long register(UserVO vo) throws Exception;

    Long login(UserVO vo) throws Exception;

}
