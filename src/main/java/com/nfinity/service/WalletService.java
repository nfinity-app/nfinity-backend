package com.nfinity.service;

import com.nfinity.vo.*;

public interface WalletService {
    WalletVO getWalletFinance(Long userId);

    String getChainAddress(Long userId, String type, String coin);

    Long withdraw(CeWithdrawVO vo);

    PageModel<ChainBillVO> getTransactionHistory(Long userId, int page, int size, int txTime, String coin, int txType);

    PortfolioVO getWalletByTypeAndCoin(Long userId, String type, String coin);
}
