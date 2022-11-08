package com.nfinity.service.impl;

import com.nfinity.service.SocialService;
import com.nfinity.vo.TwitterVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {
    @Value("${twitter.bearer-token}")
    private String bearerToken;

    private final RestTemplate restTemplate = new RestTemplate();
    @SneakyThrows
    @Override
    public List<TwitterVO.TwitterUserVO> lookupTwitterUsers(String usernames) {
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
        ResponseEntity<TwitterVO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, TwitterVO.class);
        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }
}
