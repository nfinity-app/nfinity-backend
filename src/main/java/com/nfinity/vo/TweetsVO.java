package com.nfinity.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TweetsVO {
    private long totalTweets;
    private List<TweetVO> tweetIds;
}
