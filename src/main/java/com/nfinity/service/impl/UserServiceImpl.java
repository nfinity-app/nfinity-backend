package com.nfinity.service.impl;

import com.nfinity.entity.UserEntity;
import com.nfinity.exception.BusinessException;
import com.nfinity.repository.UserRepository;
import com.nfinity.service.UserService;
import com.nfinity.util.AESEncryption;
import com.nfinity.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
        if(StringUtils.isBlank(vo.getEmail())){
            throw new BusinessException("The email is empty");
        }

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
            throw new BusinessException("This email has already been registered");
        }
    }

    @Override
    public Long login(UserVO vo) throws CryptorException {
        if(StringUtils.isBlank(vo.getEmail()) || StringUtils.isBlank(vo.getUserName())){
            throw new BusinessException("The email or user name is empty");
        }
        UserEntity userEntity = userRepository.findByEmailOrUserName(vo.getEmail(), vo.getUserName());

        if(Objects.isNull(userEntity)){
            throw new BusinessException("This email has not been registered");
        }else{
            String md5Password = getMd5Password(vo.getPassword());
            if(md5Password.equals(userEntity.getPassword())){
                return userEntity.getId();
            }else{
                throw new BusinessException("Incorrect email or user name or password");
            }
        }
    }

    private String getMd5Password(String password) throws CryptorException {
        String decodedPassword = AESEncryption.decrypt(password, aesKey);
        return DigestUtils.md5DigestAsHex(decodedPassword.getBytes(StandardCharsets.UTF_8));
    }
}
