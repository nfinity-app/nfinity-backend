package com.nfinity.service;

import com.nfinity.vo.LoyaltyProgramCollectionsVO;
import com.nfinity.vo.LoyaltyProgramVO;

public interface LoyaltyProgramService {
    Long saveLoyaltyProgram(LoyaltyProgramVO vo);

    Long createLoyaltyProgram(LoyaltyProgramVO vo);

    Long editLoyaltyProgram(LoyaltyProgramVO vo);

    LoyaltyProgramCollectionsVO getLoyaltyProgram(Long userId);
}
