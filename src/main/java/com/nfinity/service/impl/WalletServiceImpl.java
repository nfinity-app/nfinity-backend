package com.nfinity.service.impl;

import com.nfinity.entity.*;
import com.nfinity.repository.*;
import com.nfinity.service.WalletService;
import com.nfinity.util.BeansUtil;
import com.nfinity.util.BigDecimalUtil;
import com.nfinity.vo.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
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
    private final CeWithdrawRepository ceWithdrawRepository;
    private final ChainBillRepository chainBillRepository;

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
                    portfolioVO.setImg(chainCoinEntity.getImg());
                    portfolioVO.setUseAmount(BigDecimalUtil.stripTrailingZeros(ceFinanceEntity.getUseAmount()));
                }

                Optional<ChainAddressEntity> chainAddressEntityOptional = chainAddressRepository.findByCoinIdAndUserId(ceFinanceEntity.getCoinId(), userId);
                if(chainAddressEntityOptional.isPresent() && StringUtils.isBlank(walletVO.getAddress())){
                    ChainAddressEntity chainAddressEntity = chainAddressEntityOptional.get();
                    walletVO.setAddress(chainAddressEntity.getAddress());
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
    public String getChainAddress(Long userId, String type, String coin) {
        Optional<ChainCoinEntity> chainCoinEntityOptional = chainCoinRepository.findBySymbol(coin);
        if(chainCoinEntityOptional.isPresent()){
            Long coinId = chainCoinEntityOptional.get().getId();

            Optional<ChainAddressEntity> chainAddressEntityOptional = chainAddressRepository.findByUserIdAndCoinIdAndChainType(userId, coinId, type);
            if(chainAddressEntityOptional.isPresent()){
                ChainAddressEntity entity = chainAddressEntityOptional.get();
                return entity.getAddress();
            }
            return null;
        }
        return null;
    }

    @Override
    public Long withdraw(CeWithdrawVO vo) {
        CeWithdrawEntity ceWithdrawEntity = new CeWithdrawEntity();
        BeanUtils.copyProperties(vo, ceWithdrawEntity, BeansUtil.getNullFields(vo));

        Optional<ChainCoinEntity> chainCoinEntityOptional = chainCoinRepository.findBySymbol(vo.getSymbol());
        if(chainCoinEntityOptional.isPresent()){
            Long coinId = chainCoinEntityOptional.get().getId();
            ceWithdrawEntity.setCoinId(coinId);
        }

        return ceWithdrawRepository.save(ceWithdrawEntity).getId();
    }

    @Override
    public PageModel<ChainBillVO> getTransactionHistory(Long userId, int page, int size) {
        PageModel<ChainBillVO> pageModel = new PageModel<>();
        List<ChainBillVO> chainBillVOS = new ArrayList<>();

        int total = chainBillRepository.countByUserId(userId);
        List<ChainBillEntity> chainBillEntityList = chainBillRepository.findAllByUserId(userId, PageRequest.of(page - 1, size));
        if(!CollectionUtils.isEmpty(chainBillEntityList)){
            for(ChainBillEntity entity : chainBillEntityList){
                  ChainBillVO vo = new ChainBillVO();
                  BeanUtils.copyProperties(entity, vo, BeansUtil.getNullFields(entity));

                  Long coinId = entity.getCoinId();
                  Optional<ChainCoinEntity> chainCoinEntityOptional = chainCoinRepository.findById(coinId);
                  if(chainCoinEntityOptional.isPresent()){
                      ChainCoinEntity chainCoinEntity = chainCoinEntityOptional.get();
                      vo.setSymbol(chainCoinEntity.getSymbol());
                      vo.setImg(chainCoinEntity.getImg());
                  }
                  chainBillVOS.add(vo);
            }
        }

        pageModel.setTotal(total);
        pageModel.setRecords(chainBillVOS);
        return pageModel;
    }
}
