package com.nfinity.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class NftsInputVO {
    @NotEmpty
    private List<@NotNull NftVO> records;
}
