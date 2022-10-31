package com.nfinity.service.impl;

import com.nfinity.aws.S3Util;
import com.nfinity.entity.BusinessInfoEntity;
import com.nfinity.entity.UserEntity;
import com.nfinity.enums.ErrorCode;
import com.nfinity.enums.UploadType;
import com.nfinity.exception.BusinessException;
import com.nfinity.repository.BusinessInfoRepository;
import com.nfinity.repository.UserRepository;
import com.nfinity.service.BusinessInfoService;
import com.nfinity.util.BeansUtil;
import com.nfinity.vo.BusinessInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static com.nfinity.constant.Constant.S3_FILE_PATH;

@Service
@RequiredArgsConstructor
public class BusinessInfoServiceImpl implements BusinessInfoService {
    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final BusinessInfoRepository businessInfoRepository;
    private final UserRepository userRepository;
    private final S3Util s3Util;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public boolean existsBusinessInfo(Long userId) {
        return businessInfoRepository.existsByUserId(userId);
    }

    @Override
    public Long createBusinessInfo(BusinessInfoVO vo) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        BusinessInfoEntity entity = new BusinessInfoEntity();
        BeanUtils.copyProperties(vo, entity, BeansUtil.getNullFields(vo));
        entity.setBirthDate(Timestamp.valueOf(vo.getBirthDate()));
        entity.setCreateTime(timestamp);
        entity.setUpdateTime(timestamp);
        return businessInfoRepository.save(entity).getId();
    }

    @Override
    public BusinessInfoVO getBusinessInfo(Long userId) {
        BusinessInfoVO vo = new BusinessInfoVO();

        Optional<BusinessInfoEntity> optional = businessInfoRepository.findByUserId(userId);
        if(optional.isPresent()){
            BusinessInfoEntity entity = optional.get();
            BeanUtils.copyProperties(entity, vo, BeansUtil.getNullFields(entity));
            vo.setBirthDate(sdf.format(entity.getBirthDate()));
            return vo;
        }else{
            throw new BusinessException(ErrorCode.BUSINESS_INFO_NOT_FOUND);
        }
    }

    @Override
    public Long editBusinessInfo(BusinessInfoVO vo) {
        Optional<BusinessInfoEntity> optional = businessInfoRepository.findByUserId(vo.getUserId());
        if(optional.isPresent()){
            BusinessInfoEntity entity = optional.get();
            BeanUtils.copyProperties(vo, entity, BeansUtil.getNullFields(vo));
            entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            return businessInfoRepository.save(entity).getId();
        }else {
            throw new BusinessException(ErrorCode.BUSINESS_INFO_NOT_FOUND);
        }
    }

    @Override
    public String uploadLogo(List<MultipartFile> multipartFile, Long userId) throws Exception {
        UserEntity userEntity;
        Optional<UserEntity> optional = userRepository.findById(userId);
        if(optional.isPresent()) {
            userEntity = optional.get();
        }else{
            throw new BusinessException(ErrorCode.NOT_REGISTERED);
        }

        String s3Dir = s3Util.preUploadFiles(multipartFile, userEntity.getEmail(), UploadType.BUSINESS_LOGO.getValue());
        String logo = S3_FILE_PATH + bucketName + File.separator + s3Dir + File.separator + multipartFile.get(0).getOriginalFilename();

        Optional<BusinessInfoEntity> businessInfoEntityOptional = businessInfoRepository.findByUserId(userId);
        if(businessInfoEntityOptional.isPresent()){
            BusinessInfoEntity businessInfoEntity = businessInfoEntityOptional.get();
            businessInfoEntity.setLogo(logo);
            businessInfoEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            businessInfoRepository.save(businessInfoEntity);
            return logo;
        }else {
            throw new BusinessException(ErrorCode.BUSINESS_INFO_NOT_FOUND);
        }
    }
}
