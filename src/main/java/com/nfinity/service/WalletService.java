package com.nfinity.service;

import com.nfinity.vo.WalletVO;

public interface WalletService {
    WalletVO getWalletFinance(Long userId);

    String getChainAddress(Long userId, String type);
}
