package com.nfinity.service;

import com.nfinity.vo.BusinessInfoVO;

public interface BusinessInfoService {
    boolean existsBusinessInfo(Long userId);
    Long createBusinessInfo(BusinessInfoVO vo);

    BusinessInfoVO getBusinessInfo(Long userId, Long businessId);

    Long editBusinessInfo(BusinessInfoVO vo);
}
