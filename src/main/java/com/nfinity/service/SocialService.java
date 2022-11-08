package com.nfinity.service;

import com.nfinity.vo.TwitterVO;

import java.util.List;

public interface SocialService {
    List<TwitterVO.TwitterUserVO> lookupTwitterUsers(String usernames);
}
