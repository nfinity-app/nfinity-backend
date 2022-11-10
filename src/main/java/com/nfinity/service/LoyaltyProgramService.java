package com.nfinity.service;

import com.nfinity.vo.LoyaltyProgramCollectionsVO;
import com.nfinity.vo.LoyaltyProgramVO;
import com.nfinity.vo.PageModel;
import com.nfinity.vo.TierUserVO;

public interface LoyaltyProgramService {
    Long saveLoyaltyProgram(LoyaltyProgramVO vo);

    Long createLoyaltyProgram(LoyaltyProgramVO vo);

    Long editLoyaltyProgram(LoyaltyProgramVO vo);

    LoyaltyProgramCollectionsVO getLoyaltyProgram(Long userId);

    PageModel<TierUserVO> getTierMembers(Long userId);

    Long deleteLoyaltyProgram(Long userId);
}
