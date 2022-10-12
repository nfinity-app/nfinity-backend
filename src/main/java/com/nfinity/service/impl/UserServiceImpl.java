package com.nfinity.service.impl;

import com.nfinity.entity.UserEntity;
import com.nfinity.enums.ErrorCode;
import com.nfinity.exception.BusinessException;
import com.nfinity.repository.UserRepository;
import com.nfinity.service.UserService;
import com.nfinity.util.AESEncryption;
import com.nfinity.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.cryptonode.jncryptor.CryptorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${aes.secret.key}")
    private String aesKey;
    private final UserRepository userRepository;
    @Override
    public Long register(UserVO vo) throws CryptorException {
        UserEntity userEntity = userRepository.findByEmail(vo.getEmail());

        if(Objects.isNull(userEntity)){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            UserEntity entity = new UserEntity();
            entity.setEmail(vo.getEmail());
            entity.setPassword(getMd5Password(vo.getPassword()));
            entity.setCreateTime(timestamp);
            entity.setUpdateTime(timestamp);
            return userRepository.save(entity).getId();
        }else{
            throw new BusinessException(ErrorCode.CONFLICT);
        }
    }

    @Override
    public Long login(UserVO vo) throws CryptorException {
        UserEntity userEntity = userRepository.findByEmailOrUserName(vo.getEmail(), vo.getUserName());

        if(Objects.isNull(userEntity)){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }else{
            String md5Password = getMd5Password(vo.getPassword());
            if(md5Password.equals(userEntity.getPassword())){
                return userEntity.getId();
            }else{
                throw new BusinessException(ErrorCode.UNAUTHORIZED);
            }
        }
    }

    private String getMd5Password(String password) throws CryptorException {
        String decodedPassword = AESEncryption.decrypt(password, aesKey);
        return DigestUtils.md5DigestAsHex(decodedPassword.getBytes(StandardCharsets.UTF_8));
    }
}
