package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderVO {
    private Long id;
    private String name;
    private String icon;
    @JsonProperty("mint_status")
    private Integer mintStatus;
}
