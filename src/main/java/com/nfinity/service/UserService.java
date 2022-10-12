package com.nfinity.service;

import com.nfinity.vo.UserVO;
import org.cryptonode.jncryptor.CryptorException;

public interface UserService {
    Long register(UserVO vo) throws CryptorException;

    Long login(UserVO vo) throws CryptorException;

}
