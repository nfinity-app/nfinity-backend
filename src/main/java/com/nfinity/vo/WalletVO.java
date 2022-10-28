package com.nfinity.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class WalletVO {
    private BigDecimal balance;
    private String address;
    private List<PortfolioVO> records;
}
