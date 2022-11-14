package com.nfinity.service;

import com.nfinity.vo.LoyaltyProgramCollectionsVO;
import com.nfinity.vo.PageModel;
import com.nfinity.vo.TierUserVO;

public interface LoyaltyProgramService {
    Long saveLoyaltyProgram(LoyaltyProgramCollectionsVO vo);

    Long createLoyaltyProgram(LoyaltyProgramCollectionsVO vo);

    Long editLoyaltyProgram(LoyaltyProgramCollectionsVO vo);

    LoyaltyProgramCollectionsVO getLoyaltyProgram(Long userId);

    PageModel<TierUserVO> getTierMembers(Long userId);

    Long deleteLoyaltyProgram(Long userId);
}
