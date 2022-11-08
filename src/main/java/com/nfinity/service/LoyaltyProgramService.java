package com.nfinity.service;

import com.nfinity.vo.LoyaltyProgramCollectionsVO;
import com.nfinity.vo.LoyaltyProgramVO;
import com.nfinity.vo.TierUserVO;

import java.util.List;

public interface LoyaltyProgramService {
    Long saveLoyaltyProgram(LoyaltyProgramVO vo);

    Long createLoyaltyProgram(LoyaltyProgramVO vo);

    Long editLoyaltyProgram(LoyaltyProgramVO vo);

    LoyaltyProgramCollectionsVO getLoyaltyProgram(Long userId);

    List<TierUserVO> getTierMembers(Long userId);
}
