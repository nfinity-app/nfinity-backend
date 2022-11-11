package com.nfinity.service.impl;

import com.nfinity.entity.CollectionEntity;
import com.nfinity.entity.LoyaltyProgramCollectionEntity;
import com.nfinity.entity.LoyaltyProgramEntity;
import com.nfinity.enums.ErrorCode;
import com.nfinity.exception.BusinessException;
import com.nfinity.repository.CollectionRepository;
import com.nfinity.repository.LoyaltyProgramCollectionRepository;
import com.nfinity.repository.LoyaltyProgramRepository;
import com.nfinity.service.SocialService;
import com.nfinity.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {
    @Value("${twitter.bearer-token}")
    private String bearerToken;
    private final RestTemplate restTemplate = new RestTemplate();
    private final LoyaltyProgramRepository loyaltyProgramRepository;
    private final LoyaltyProgramCollectionRepository loyaltyProgramCollectionRepository;
    private final CollectionRepository collectionRepository;

    @SneakyThrows
    @Override
    public List<TwitterUserVO> lookupTwitterUsers(String usernames) {
        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/users/by");
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("usernames", usernames));
        queryParameters.add(new BasicNameValuePair("user.fields", "profile_image_url"));
        uriBuilder.addParameters(queryParameters);
        String url = URLDecoder.decode(String.valueOf(uriBuilder), StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        headers.set("Authorization", String.format("Bearer %s", bearerToken));
        ResponseEntity<TwitterVO<TwitterUserVO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }

    //TODO: too many request to twitter api, scheduler better
    @Override
    public List<SocialVO> getTwitterEngagement(Long userId) {
        Optional<LoyaltyProgramEntity> loyaltyProgramEntityOptional = loyaltyProgramRepository.findByUserId(userId);
        if(loyaltyProgramEntityOptional.isEmpty()){
            throw new BusinessException(ErrorCode.LOYALTY_PROGRAM_NOT_FOUND);
        }

        Long programId = loyaltyProgramEntityOptional.get().getId();
        List<LoyaltyProgramCollectionEntity> loyaltyProgramCollectionEntities = loyaltyProgramCollectionRepository.findAllByProgramId(programId);
        List<SocialVO> socials = new ArrayList<>(loyaltyProgramCollectionEntities.size());
        if(!CollectionUtils.isEmpty(loyaltyProgramCollectionEntities)){
            for(LoyaltyProgramCollectionEntity loyaltyProgramCollectionEntity : loyaltyProgramCollectionEntities){
                SocialVO socialVO = new SocialVO();
                Long collectionId = loyaltyProgramCollectionEntity.getCollectionId();
                socialVO.setCollectionId(collectionId);

                Optional<CollectionEntity> collectionEntityOptional = collectionRepository.findById(collectionId);
                if(collectionEntityOptional.isPresent()){
                    String collectionName = collectionEntityOptional.get().getName();
                    socialVO.setCollectionName(collectionName);
                }

                String twitterUserId = loyaltyProgramCollectionEntity.getTwitterUserId();
                long follows = getTwitterFollowers(twitterUserId);
                socialVO.setFollowers(follows);

                TweetsVO tweetsVO = getTweets(twitterUserId);
                socialVO.setTweets(tweetsVO.getTotalTweets());

                long likes = getTweetsLikes(tweetsVO.getTweetIds());
                socialVO.setLikes(likes);

                long retweets = getRetweets(tweetsVO.getTweetIds());
                socialVO.setRetweets(retweets);


                int followPoints = loyaltyProgramCollectionEntity.getTwitterFollowPoints();
                int likePoints = loyaltyProgramCollectionEntity.getTwitterPerPostLikePoints();
                int retweetPoints = loyaltyProgramCollectionEntity.getTwitterRetweetPoints();

                long totalPoints = follows * followPoints + likes * likePoints + retweets * retweetPoints;
                socialVO.setTotalPoints(totalPoints);

                socials.add(socialVO);
            }
        }
        return socials;
    }

    @SneakyThrows
    private long getTwitterFollowers(String twitterUserId) {
        URIBuilder uriBuilder = new URIBuilder(String.format("https://api.twitter.com/2/users/%s/followers", twitterUserId));
        String url = String.valueOf(uriBuilder);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        headers.set("Authorization", String.format("Bearer %s", bearerToken));
        ResponseEntity<TwitterVO<TwitterUserVO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>(){});
        TwitterVO<TwitterUserVO> twitterVO = responseEntity.getBody();

        assert twitterVO != null;
        assert twitterVO.getMeta() != null;
        return twitterVO.getMeta().getResultCount();
    }

    @SneakyThrows
    private TweetsVO getTweets(String twitterUserId) {
        TwitterVO<TweetVO> twitterVO = requestRecentTweets(twitterUserId, null);

        TweetsVO tweetsVO = new TweetsVO();
        tweetsVO.setTotalTweets(0);
        tweetsVO.setTweetIds(new ArrayList<>());

        while (StringUtils.isNoneBlank(twitterVO.getMeta().getNextToken())){
            culTweetsAndLikes(twitterVO, tweetsVO);

            String nextToken = twitterVO.getMeta().getNextToken();
            twitterVO = requestRecentTweets(twitterUserId, nextToken);
        }

        culTweetsAndLikes(twitterVO, tweetsVO);

        return tweetsVO;
    }

    private void culTweetsAndLikes(TwitterVO<TweetVO> twitterVO, TweetsVO tweetsVO){
        List<TweetVO> tweetVOList = twitterVO.getData();
        tweetsVO.getTweetIds().addAll(tweetVOList);

        long totalTweets = tweetsVO.getTotalTweets();
        totalTweets += twitterVO.getMeta().getResultCount();

        tweetsVO.setTotalTweets(totalTweets);
    }

    @SneakyThrows
    private TwitterVO<TweetVO> requestRecentTweets(String twitterUserId, String nextToken){
        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent");
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("query", "from:" + twitterUserId));
        if(StringUtils.isNoneBlank(nextToken)) {
            queryParameters.add(new BasicNameValuePair("next_token", nextToken));
        }
        uriBuilder.addParameters(queryParameters);
        String url = URLDecoder.decode(String.valueOf(uriBuilder), StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        headers.set("Authorization", String.format("Bearer %s", bearerToken));
        ResponseEntity<TwitterVO<TweetVO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});
        assert responseEntity.getBody() != null;
        return responseEntity.getBody();
    }


    private long getTweetsLikes(List<TweetVO> tweetIds) {
        long totalLikes = 0;
        if(!CollectionUtils.isEmpty(tweetIds)) {
            TwitterVO<TwitterUserVO> twitterVO;
            for(TweetVO vo : tweetIds) {
                 twitterVO = requestTweetLikes(vo.getId(), null);

                while (StringUtils.isNoneBlank(twitterVO.getMeta().getNextToken())) {
                    totalLikes += twitterVO.getMeta().getResultCount();

                    twitterVO = requestTweetLikes(vo.getId(), twitterVO.getMeta().getNextToken());
                }
                totalLikes += twitterVO.getMeta().getResultCount();
            }
        }
        return totalLikes;
    }

    @SneakyThrows
    private TwitterVO<TwitterUserVO> requestTweetLikes(String tweetId, String nextToken){
        URIBuilder uriBuilder = new URIBuilder(String.format("https://api.twitter.com/2/tweets/%s/liking_users", tweetId));
        if(StringUtils.isNoneBlank(nextToken)) {
            ArrayList<NameValuePair> queryParameters;
            queryParameters = new ArrayList<>();
            queryParameters.add(new BasicNameValuePair("pagination_token", nextToken));
            uriBuilder.addParameters(queryParameters);
        }
        String url = URLDecoder.decode(String.valueOf(uriBuilder), StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        headers.set("Authorization", String.format("Bearer %s", bearerToken));
        ResponseEntity<TwitterVO<TwitterUserVO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});

        assert responseEntity.getBody() != null;
        return responseEntity.getBody();
    }

    private long getRetweets(List<TweetVO> tweetIds) {
        long totalRetweets = 0;
        if(!CollectionUtils.isEmpty(tweetIds)) {
            TwitterVO<TwitterUserVO> twitterVO;
            for (TweetVO vo : tweetIds) {
                twitterVO = requestRetweets(vo.getId(), null);

                while (StringUtils.isNoneBlank(twitterVO.getMeta().getNextToken())){
                    totalRetweets += twitterVO.getMeta().getResultCount();

                    requestRetweets(vo.getId(), twitterVO.getMeta().getNextToken());
                }
                totalRetweets += twitterVO.getMeta().getResultCount();
            }
        }
        return totalRetweets;
    }

    @SneakyThrows
    private TwitterVO<TwitterUserVO> requestRetweets(String tweetId, String nextToken){
        URIBuilder uriBuilder = new URIBuilder(String.format("https://api.twitter.com/2/tweets/%s/retweeted_by", tweetId));
        if(StringUtils.isNoneBlank(nextToken)) {
            ArrayList<NameValuePair> queryParameters;
            queryParameters = new ArrayList<>();
            queryParameters.add(new BasicNameValuePair("pagination_token", nextToken));
            uriBuilder.addParameters(queryParameters);
        }
        String url = URLDecoder.decode(String.valueOf(uriBuilder), StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        headers.set("Authorization", String.format("Bearer %s", bearerToken));
        ResponseEntity<TwitterVO<TwitterUserVO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});

        assert responseEntity.getBody() != null;
        return responseEntity.getBody();
    }
}
