package com.nfinity.service.impl;

import com.nfinity.entity.BusinessInfoEntity;
import com.nfinity.enums.ErrorCode;
import com.nfinity.exception.BusinessException;
import com.nfinity.repository.BusinessInfoRepository;
import com.nfinity.service.BusinessInfoService;
import com.nfinity.util.BeansUtil;
import com.nfinity.vo.BusinessInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusinessInfoServiceImpl implements BusinessInfoService {
    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final BusinessInfoRepository businessInfoRepository;

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
}
