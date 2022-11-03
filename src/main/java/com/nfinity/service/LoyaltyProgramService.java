package com.nfinity.service;

import com.nfinity.vo.LoyaltyProgramVO;

public interface LoyaltyProgramService {
    Long saveLoyaltyProgram(LoyaltyProgramVO vo, Long userId);

    Long createLoyaltyProgram(LoyaltyProgramVO vo, Long userId);

    Long editLoyaltyProgram(LoyaltyProgramVO vo, Long userId);

    LoyaltyProgramVO getLoyaltyProgram(Long userId);
}
