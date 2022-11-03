package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TierVO {
    private Long id;
    private String name;
    @JsonProperty("required_points")
    private long requiredPoints;
}
