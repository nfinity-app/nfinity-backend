package com.nfinity.service;

import com.nfinity.vo.CeWithdrawVO;
import com.nfinity.vo.ChainBillVO;
import com.nfinity.vo.PageModel;
import com.nfinity.vo.WalletVO;

public interface WalletService {
    WalletVO getWalletFinance(Long userId);

    String getChainAddress(Long userId, String type, String coin);

    Long withdraw(CeWithdrawVO vo);

    PageModel<ChainBillVO> getTransactionHistory(Long userId, int page, int size);
}
