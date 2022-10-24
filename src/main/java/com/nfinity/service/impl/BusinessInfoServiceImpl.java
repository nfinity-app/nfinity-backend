package com.nfinity.service.impl;

import com.nfinity.entity.BusinessInfoEntity;
import com.nfinity.repository.BusinessInfoRepository;
import com.nfinity.service.BusinessInfoService;
import com.nfinity.util.BeansUtil;
import com.nfinity.vo.BusinessInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusinessInfoServiceImpl implements BusinessInfoService {

    private final BusinessInfoRepository businessInfoRepository;

    @Override
    public Long createBusinessInfo(BusinessInfoVO vo) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        BusinessInfoEntity entity = new BusinessInfoEntity();
        BeanUtils.copyProperties(vo, entity, BeansUtil.getNullFields(vo));
        entity.setCreateTime(timestamp);
        entity.setUpdateTime(timestamp);
        return businessInfoRepository.save(entity).getId();
    }

    @Override
    public BusinessInfoVO getBusinessInfo(Long userId, Long businessId) {
        BusinessInfoVO vo = new BusinessInfoVO();
        Optional<BusinessInfoEntity> optional = businessInfoRepository.findByIdAndUserId(businessId, userId);
        if(optional.isPresent()){
            BusinessInfoEntity entity = optional.get();
            BeanUtils.copyProperties(entity, vo, BeansUtil.getNullFields(entity));
        }
        return vo;
    }

    @Override
    public Long editBusinessInfo(BusinessInfoVO vo) {
        Optional<BusinessInfoEntity> optional = businessInfoRepository.findByIdAndUserId(vo.getId(), vo.getUserId());
        if(optional.isPresent()){
            BusinessInfoEntity entity = optional.get();
            BeanUtils.copyProperties(vo, entity, BeansUtil.getNullFields(vo));
            entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            return businessInfoRepository.save(entity).getId();
        }
        return null;
    }
}
