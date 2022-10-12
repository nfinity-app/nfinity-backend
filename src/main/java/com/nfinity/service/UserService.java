package com.nfinity.service;

import com.nfinity.vo.UserVO;

public interface UserService {
    Long register(UserVO vo);

    Long login(UserVO vo);

}
