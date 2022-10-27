package com.nfinity.service.impl;

import com.nfinity.aws.PinPointV2Util;
import com.nfinity.entity.CeFinanceEntity;
import com.nfinity.entity.ChainCoinEntity;
import com.nfinity.entity.UserEntity;
import com.nfinity.enums.ErrorCode;
import com.nfinity.enums.LoginType;
import com.nfinity.enums.Status;
import com.nfinity.exception.BusinessException;
import com.nfinity.repository.CeFinanceRepository;
import com.nfinity.repository.ChainCoinRepository;
import com.nfinity.repository.UserRepository;
import com.nfinity.service.UserService;
import com.nfinity.util.AESEncryption;
import com.nfinity.util.BeansUtil;
import com.nfinity.util.JwtUtil;
import com.nfinity.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;


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
    private final CeFinanceRepository ceFinanceRepository;
    private final ChainCoinRepository chainCoinRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private final JwtUtil jwtUtil;

    private final PinPointV2Util pinPointV2Util;

    @Override
    public Long register(UserVO vo) throws Exception {
        UserEntity userEntityByEmail = userRepository.findByEmailAndStatus(vo.getEmail(), Status.ENABLE.getValue());
        UserEntity userEntityByUsername = userRepository.findByUsernameAndStatus(vo.getUsername(), Status.ENABLE.getValue());
        if(Objects.nonNull(userEntityByEmail)){
            throw new BusinessException(ErrorCode.EMAIL_REGISTERED);
        }
        if(Objects.nonNull((userEntityByUsername))){
            throw new BusinessException(ErrorCode.USERNAME_REGISTERED);
        }


        UserEntity entity;
        UserEntity disableUser = userRepository.findByEmailOrUsernameAndStatus(Status.DISABLE.getValue(), vo.getEmail(), vo.getUsername());
        if(Objects.nonNull(disableUser)){
            entity = disableUser;
        }else{
            entity = new UserEntity();
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //save a pending status user to db
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
        pinPointV2Util.sendEmail(vo.getEmail(), verificationCode, "register");

        return id;

    }

    public Object checkVerificationCode(String email, String verificationCode, int type){
        UserEntity entity = userRepository.findByEmail(email);
        if(Objects.nonNull(entity)){
            String redisVerificationCode = redisTemplate.opsForValue().get(email);
            if(verificationCode.equals(redisVerificationCode)){
                if(LoginType.REGISTER.getValue() == type) {
                    return doAfterRegister(entity);
                }else {
                    return entity.getId();
                }
            }else{
                throw new BusinessException(ErrorCode.INVALID_VERIFICATION_CODE);
            }
        }else{
            throw new BusinessException(ErrorCode.NOT_REGISTERED);
        }
    }

    private String doAfterRegister(UserEntity userEntity){
        userEntity.setStatus(Status.ENABLE.getValue());
        userEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        Long userId = userRepository.save(userEntity).getId();

        //add empty data to table ce_finance
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        List<ChainCoinEntity> chainCoinEntityList = chainCoinRepository.findAll();
        List<CeFinanceEntity> ceFinanceEntityList = new ArrayList<>(chainCoinEntityList.size());
        for(ChainCoinEntity entity : chainCoinEntityList){
            CeFinanceEntity ceFinanceEntity = new CeFinanceEntity();
            ceFinanceEntity.setCoinId(entity.getId());
            ceFinanceEntity.setUserId(userId);
            ceFinanceEntity.setUseAmount(BigDecimal.ZERO);
            ceFinanceEntity.setChainFrozen(BigDecimal.ZERO);
            ceFinanceEntity.setOtcFrozen(BigDecimal.ZERO);
            ceFinanceEntity.setCreateTime(timestamp);
            ceFinanceEntity.setUpdateTime(timestamp);
            ceFinanceEntityList.add(ceFinanceEntity);
        }
        ceFinanceRepository.saveAll(ceFinanceEntityList);

        //register returns token, user will go the home page.
        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        return jwtUtil.generateToken(map);
    }

    @Override
    public Long sendEmail(String email) {
        UserEntity entity = userRepository.findByEmailAndStatus(email, Status.ENABLE.getValue());
        if(Objects.nonNull(entity)) {
            //save verification code to redis
            String verificationCode = generateVerificationCode();
            redisTemplate.opsForValue().set(email, verificationCode, Duration.ofMinutes(30));

            //send email to user
            pinPointV2Util.sendEmail(email, verificationCode, "reset");

            return entity.getId();
        }else{
            throw new BusinessException(ErrorCode.NOT_REGISTERED);
        }
    }

    @Override
    public Long resetPassword(UserVO vo) throws Exception {
        String redisVerificationCode = redisTemplate.opsForValue().get(vo.getEmail());
        if(!vo.getVerificationCode().equals(redisVerificationCode)) {
            throw new BusinessException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        UserEntity entity = userRepository.findByEmailAndStatus(vo.getEmail(), Status.ENABLE.getValue());
        if(Objects.nonNull(entity)) {
            entity.setPassword(getMd5Password(vo.getPassword()));
            entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            return userRepository.save(entity).getId();
        }else{
            throw new BusinessException(ErrorCode.NOT_REGISTERED);
        }
    }

    @Override
    public String login(UserVO vo) throws Exception {
        UserEntity userEntity = userRepository.findByEmailOrUsernameAndStatus(Status.ENABLE.getValue(), vo.getEmail(), vo.getUsername());

        if(Objects.isNull(userEntity)){
            throw new BusinessException(ErrorCode.NOT_REGISTERED);
        }else{
            String md5Password = getMd5Password(vo.getPassword());
            if(md5Password.equals(userEntity.getPassword())){
                Map<String, Object> map = new HashMap<>();
                map.put("id", userEntity.getId());
                return jwtUtil.generateToken(map);
            }else{
                throw new BusinessException(ErrorCode.INCORRECT_INPUT);
            }
        }
    }

    @Override
    public Long editProfile(UserVO vo) throws Exception {
        UserEntity entity;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(Objects.nonNull(vo.getId())){
            Optional<UserEntity> optional = userRepository.findById(vo.getId());
            entity = optional.orElseGet(UserEntity::new);
        }else {
            throw new BusinessException(ErrorCode.NOT_REGISTERED);
        }

        BeanUtils.copyProperties(vo, entity, BeansUtil.getNullFields(vo));
        if(StringUtils.isNoneBlank(vo.getOldPassword()) && StringUtils.isNoneBlank(vo.getNewPassword())){
            if(entity.getPassword().equals(vo.getOldPassword())) {
                entity.setPassword(getMd5Password(vo.getNewPassword()));
            }else{
                throw new BusinessException(ErrorCode.INCORRECT_INPUT);
            }
        }

        //TODO: add telephone

        entity.setCreateTime(timestamp);
        entity.setUpdateTime(timestamp);
        return userRepository.save(entity).getId();
    }

    private String getMd5Password(String password) throws Exception {
        String decodedPassword = AESEncryption.decrypt(password, aesKey, aesIv);
        return DigestUtils.md5DigestAsHex((decodedPassword + md5Salt).getBytes(StandardCharsets.UTF_8));
    }

    private String generateVerificationCode(){
        Random random = new Random();
        int number = random.nextInt(999999);
        return String.format("%06d", number);
    }
}
