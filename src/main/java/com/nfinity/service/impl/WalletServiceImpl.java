package com.nfinity.service.impl;

import com.nfinity.entity.CeFinanceEntity;
import com.nfinity.entity.ChainAddressEntity;
import com.nfinity.entity.ChainCoinEntity;
import com.nfinity.repository.CeFinanceRepository;
import com.nfinity.repository.ChainAddressRepository;
import com.nfinity.repository.ChainCoinRepository;
import com.nfinity.service.WalletService;
import com.nfinity.vo.PortfolioVO;
import com.nfinity.vo.WalletVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final CeFinanceRepository ceFinanceRepository;
    private final ChainCoinRepository chainCoinRepository;
    private final ChainAddressRepository chainAddressRepository;

    @Override
    public WalletVO getWalletFinance(Long userId) {
        WalletVO walletVO = new WalletVO();

        List<CeFinanceEntity> ceFinanceEntityList = ceFinanceRepository.findAllByUserId(userId);
        List<PortfolioVO> portfolioVOList = new ArrayList<>(ceFinanceEntityList.size());

        if(!CollectionUtils.isEmpty(ceFinanceEntityList)){
            for(CeFinanceEntity ceFinanceEntity : ceFinanceEntityList){
                PortfolioVO portfolioVO = new PortfolioVO();
                Optional<ChainCoinEntity> chainCoinEntityOptional = chainCoinRepository.findById(ceFinanceEntity.getCoinId());
                if(chainCoinEntityOptional.isPresent()){
                    ChainCoinEntity chainCoinEntity = chainCoinEntityOptional.get();
                    portfolioVO.setCoinId(chainCoinEntity.getId());
                    portfolioVO.setSymbol(chainCoinEntity.getSymbol());
                    portfolioVO.setIcon(chainCoinEntity.getImg());
                    portfolioVO.setUseAmount(ceFinanceEntity.getUseAmount());
                }

                Optional<ChainAddressEntity> chainAddressEntityOptional = chainAddressRepository.findByCoinIdAndUserId(ceFinanceEntity.getCoinId(), userId);
                if(chainAddressEntityOptional.isPresent()){
                    ChainAddressEntity chainAddressEntity = chainAddressEntityOptional.get();
                    portfolioVO.setAddress(chainAddressEntity.getAddress());
                }

                portfolioVOList.add(portfolioVO);
            }
        }

        //TODO: need fiat-coin converter
        walletVO.setBalance(BigDecimal.ZERO);
        walletVO.setRecords(portfolioVOList);
        return walletVO;
    }

    @Override
    public String getChainAddress(Long userId, String type) {
        Optional<ChainAddressEntity> optional = chainAddressRepository.findByUserIdAndChainType(userId, type);
        if(optional.isPresent()){
            ChainAddressEntity entity = optional.get();
            return entity.getAddress();
        }
        return null;
    }
}
