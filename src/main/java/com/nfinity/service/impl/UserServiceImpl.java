package com.nfinity.service.impl;

import com.nfinity.aws.PinPointUtil;
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
import com.nfinity.util.GoogleAuthenticator;
import com.nfinity.util.JwtUtil;
import com.nfinity.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${aes.secret.key}")
    private String aesKey;
    @Value("${aes.secret.iv}")
    private String aesIv;
    @Value("${md5.salt}")
    private String md5Salt;
    @Value("${website.host}")
    private String websiteHost;

    private final UserRepository userRepository;
    private final CeFinanceRepository ceFinanceRepository;
    private final ChainCoinRepository chainCoinRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private final JwtUtil jwtUtil;

    private final PinPointUtil pinPointUtil;

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
        UserEntity disableUser = userRepository.findByEmailAndStatus(vo.getEmail(), Status.DISABLE.getValue());
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

        return userRepository.save(entity).getId();
    }

    @Override
    public void sendEmail(String email, int type) {
        //save verification code to redis
        String verificationCode = generateVerificationCode();
        redisTemplate.opsForValue().set(email, verificationCode, Duration.ofMinutes(30));

        //send email to user
        pinPointUtil.sendEmail(email, verificationCode, type);
    }

    public String checkVerificationCode(String email, String verificationCode, int type){
        String redisVerificationCode = redisTemplate.opsForValue().get(email);
        if(verificationCode.equals(redisVerificationCode)){
            if(LoginType.REGISTER.getValue() == type) {
                return doAfterRegister(email);
            }else {
                return null;
            }
        }else{
            throw new BusinessException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
    }

    @Transactional
    String doAfterRegister(String email){
        UserEntity userEntity = userRepository.findByEmailAndStatus(email, Status.DISABLE.getValue());
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
        return generateToken(userId);
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
                return generateToken(userEntity.getId());
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
            String oldPassword = getMd5Password(vo.getOldPassword());
            if(entity.getPassword().equals(oldPassword)) {
                entity.setPassword(getMd5Password(vo.getNewPassword()));
            }else{
                throw new BusinessException(ErrorCode.INCORRECT_PASSWORD);
            }
        }

        //TODO: add telephone

        entity.setUpdateTime(timestamp);
        return userRepository.save(entity).getId();
    }

    @Override
    public UserVO getProfile(Long userId) {
        UserVO vo = new UserVO();
        Optional<UserEntity> optional = userRepository.findById(userId);
        if(optional.isPresent()){
            UserEntity userEntity = optional.get();
            BeanUtils.copyProperties(userEntity, vo, BeansUtil.getNullFields(userEntity));
            return vo;
        }else{
            throw new BusinessException(ErrorCode.NOT_REGISTERED);
        }
    }

    @Override
    public String getQRCode(Long userId) {
        Optional<UserEntity> optional = userRepository.findById(userId);
        if(optional.isPresent()){
            UserEntity userEntity = optional.get();

            String key = GoogleAuthenticator.generateSecretKey();
            userEntity.setGoogleAuth(key);
            userEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            userRepository.save(userEntity);

            return GoogleAuthenticator.getQRBarcodeURL(userEntity.getUsername(), websiteHost, key);
        }else{
            throw new BusinessException(ErrorCode.NOT_REGISTERED);
        }
    }

    @Override
    public boolean verifyAuthenticatorCode(Long userId, long code) {
        Optional<UserEntity> optional = userRepository.findById(userId);
        if(optional.isPresent()) {
            UserEntity userEntity = optional.get();
            String key = userEntity.getGoogleAuth();
            boolean flag = GoogleAuthenticator.checkCode(key, code, System.currentTimeMillis());
            if(!flag){
                throw new BusinessException(ErrorCode.INVALID_VERIFICATION_CODE);
            }
            return true;
        }else{
            throw new BusinessException(ErrorCode.NOT_REGISTERED);
        }
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

    private String generateToken(Long userId){
        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        //TODO: limit token generation
        return jwtUtil.generateToken(map);
    }
}
