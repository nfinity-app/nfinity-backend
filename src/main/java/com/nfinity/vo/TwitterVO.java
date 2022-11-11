package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TwitterVO<T> {

    private List<T> data;
    private MetaVO meta;


    @Getter
    @Setter
    public static class MetaVO{
        @JsonProperty("result_count")
        private long resultCount;

        @JsonProperty("next_token")
        private String nextToken;
    }
}
