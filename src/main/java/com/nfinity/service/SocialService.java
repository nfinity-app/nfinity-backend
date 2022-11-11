package com.nfinity.service;

import com.nfinity.vo.SocialVO;
import com.nfinity.vo.TwitterUserVO;

import java.util.List;

public interface SocialService {
    List<TwitterUserVO> lookupTwitterUsers(String usernames);

    List<SocialVO> getTwitterEngagement(Long userId);
}
