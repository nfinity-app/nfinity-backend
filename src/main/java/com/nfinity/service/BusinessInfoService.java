package com.nfinity.service;

import com.nfinity.vo.BusinessInfoVO;

public interface BusinessInfoService {
    Long createBusinessInfo(BusinessInfoVO vo);

    BusinessInfoVO getBusinessInfo(Long userId, Long businessId);

    Long editBusinessInfo(BusinessInfoVO vo);
}
