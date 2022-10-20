package com.nfinity.service.impl;

import com.nfinity.aws.PinPointUtil;
import com.nfinity.entity.UserEntity;
import com.nfinity.enums.ErrorCode;
import com.nfinity.enums.Status;
import com.nfinity.exception.BusinessException;
import com.nfinity.repository.UserRepository;
import com.nfinity.service.UserService;
import com.nfinity.util.AESEncryption;
import com.nfinity.util.BeansUtil;
import com.nfinity.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${aes.secret.key}")
    private String aesKey;
    @Value("${aes.secret.iv}")
    private String aesIv;
    @Value("${md5.salt}")
    private String md5Salt;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Long register(UserVO vo) throws Exception {
        UserEntity userEntity = userRepository.findByEmail(vo.getEmail());

        if(Objects.isNull(userEntity)){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            //save a pending status user to db
            UserEntity entity = new UserEntity();
            BeanUtils.copyProperties(vo, entity, BeansUtil.getNullFields(vo));
            entity.setPassword(getMd5Password(vo.getPassword()));
            entity.setStatus(Status.DISABLE.getValue());
            entity.setCreateTime(timestamp);
            entity.setUpdateTime(timestamp);

            Long id = userRepository.save(entity).getId();

            //save verification code to redis
            String verificationCode = generateVerificationCode();
            redisTemplate.opsForValue().set(vo.getEmail(), verificationCode, Duration.ofMinutes(30));

            //send email to user
            PinPointUtil.sendEmail(vo.getUsername(), vo.getEmail(), verificationCode);

            return id;
        }else{
            throw new BusinessException(ErrorCode.CONFLICT);
        }
    }

    public Long checkVerificationCode(String username, String email, String verificationCode){
        UserEntity entity = userRepository.findByEmailOrUsername(email, username);
        if(Objects.nonNull(entity)){
            String redisVerificationCode = redisTemplate.opsForValue().get(email);
            if(verificationCode.equals(redisVerificationCode)){
                entity.setStatus(Status.ENABLE.getValue());
                entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                return userRepository.save(entity).getId();
            }else{
                throw new BusinessException(ErrorCode.UNAUTHORIZED);
            }
        }else{
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
    }

    @Override
    public Long login(UserVO vo) throws Exception {
        UserEntity userEntity = userRepository.findByEmailOrUsername(vo.getEmail(), vo.getUsername());

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

    public Long EditProfile(UserVO vo) throws Exception {
        UserEntity entity;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(Objects.nonNull(vo.getId())){
            Optional<UserEntity> optional = userRepository.findById(vo.getId());
            entity = optional.orElseGet(UserEntity::new);
            entity.setCreateTime(timestamp);
            entity.setUpdateTime(timestamp);
        }else {
            entity = new UserEntity();
            entity.setUpdateTime(timestamp);
        }

        BeanUtils.copyProperties(vo, entity, BeansUtil.getNullFields(vo));
        if(StringUtils.isNoneBlank(vo.getPassword())){
            entity.setPassword(getMd5Password(vo.getPassword()));
        }

        return userRepository.save(entity).getId();
    }

    private String getMd5Password(String password) throws Exception {
        String decodedPassword = AESEncryption.decrypt(password, aesKey, aesIv);
        return DigestUtils.md5DigestAsHex((decodedPassword + md5Salt).getBytes(StandardCharsets.UTF_8));
    }

    private static String generateVerificationCode(){
        Random random = new Random();
        int number = random.nextInt(99999999);
        return String.format("%08d", number);
    }
}
