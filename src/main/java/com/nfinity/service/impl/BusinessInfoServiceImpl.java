package com.nfinity.service.impl;

import com.nfinity.entity.BusinessInfoEntity;
import com.nfinity.repository.BusinessInfoRepository;
import com.nfinity.service.BusinessInfoService;
import com.nfinity.util.BeansUtil;
import com.nfinity.vo.BusinessInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessInfoServiceImpl implements BusinessInfoService {

    private final BusinessInfoRepository businessInfoRepository;

    @Override
    public Long createBusinessInfo(BusinessInfoVO vo) {
        BusinessInfoEntity entity = new BusinessInfoEntity();
        BeanUtils.copyProperties(vo, entity, BeansUtil.getNullFields(vo));
        return businessInfoRepository.save(entity).getId();
    }
}
