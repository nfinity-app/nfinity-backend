package com.nfinity.service;

import com.nfinity.vo.BusinessInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BusinessInfoService {
    boolean existsBusinessInfo(Long userId);
    Long createBusinessInfo(BusinessInfoVO vo);

    BusinessInfoVO getBusinessInfo(Long userId, Long businessId);

    Long editBusinessInfo(BusinessInfoVO vo);

    String uploadLogo(List<MultipartFile> multipartFile, Long userId, Long businessId) throws Exception;
}
