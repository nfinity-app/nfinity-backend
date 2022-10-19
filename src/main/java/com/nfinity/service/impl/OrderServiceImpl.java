package com.nfinity.service.impl;

import com.nfinity.entity.*;
import com.nfinity.enums.MintStatus;
import com.nfinity.enums.Status;
import com.nfinity.repository.*;
import com.nfinity.service.OrderService;
import com.nfinity.vo.CollectibleVO;
import com.nfinity.vo.OrderVO;
import com.nfinity.vo.PageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CollectionRepository collectionRepository;
    private final OrderNftRepository orderNftRepository;
    private final CollectionFolderNftRepository collectionFolderNftRepository;
    private final NftRepository nftRepository;

    @Override
    @Transactional
    public Long createOrder(OrderVO vo) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //1. save data to table order
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(vo, orderEntity);
        orderEntity.setCreateTime(timestamp);
        orderEntity.setUpdateTime(timestamp);
        Long orderId = orderRepository.save(orderEntity).getId();

        int mintQty = vo.getMintQty();

        List<CollectionFolderNftEntity> collectionFolderNftEntityList = collectionFolderNftRepository.findAllByCollectionIdAndNftStatus(vo.getCollectionId(), Status.ENABLE.getValue());
        if(!CollectionUtils.isEmpty(collectionFolderNftEntityList)) {
            int size = collectionFolderNftEntityList.size();
            for (int i = 0; i < size && mintQty > 0; i++) {
                CollectionFolderNftEntity entity = collectionFolderNftEntityList.get(i);
                Optional<NftEntity> optional = nftRepository.findById(entity.getNftId());
                if(optional.isPresent()){
                    NftEntity nftEntity = optional.get();

                    if(nftEntity.getMintStatus() == MintStatus.INIT.getValue() || nftEntity.getMintStatus() == MintStatus.DEPLOYED.getValue()) {
                        //2. save data to table order_collection_nft
                        long tokenId = 0;
                        OrderNftEntity orderNftEntity = new OrderNftEntity();
                        orderNftEntity.setOrderId(orderId);
                        orderNftEntity.setUserId(vo.getUserId());
                        orderNftEntity.setNftId(nftEntity.getId());
                        orderNftEntity.setTokenId(++tokenId);
                        orderNftEntity.setCreateTime(timestamp);
                        orderNftEntity.setUpdateTime(timestamp);
                        orderNftRepository.save(orderNftEntity);

                        //3. update nft mint status to minting in the table nft
                        nftEntity.setMintStatus(MintStatus.MINTING.getValue());
                        nftEntity.setUpdateTime(timestamp);
                        nftRepository.save(nftEntity);

                        mintQty--;
                    }
                }
            }
        }

        return orderId;
    }

    @Override
    public PageModel<CollectibleVO> getCollectibles(Long userId, int page, int size) {
        PageModel<CollectibleVO> pageModel = new PageModel<>();
        List<CollectibleVO> collectibleVOList = new ArrayList<>();

        int total = orderNftRepository.countByUserId(userId);
        List<OrderNftEntity> orderNftEntityList = orderNftRepository.findAllByUserId(userId, PageRequest.of(page - 1, size));

        if(!CollectionUtils.isEmpty(orderNftEntityList)) {
            for(OrderNftEntity orderNftEntity : orderNftEntityList) {
                Optional<OrderEntity> orderEntityOptional = orderRepository.findById(orderNftEntity.getOrderId());
                if (orderEntityOptional.isPresent()) {
                    OrderEntity orderEntity = orderEntityOptional.get();
                    Long collectionId = orderEntity.getCollectionId();

                    String collectionName = "";
                    Optional<CollectionEntity> optional = collectionRepository.findById(collectionId);
                    if (optional.isPresent()) {
                        collectionName = optional.get().getName();
                    }

                    CollectibleVO vo = new CollectibleVO();
                    vo.setCollectionId(orderEntity.getCollectionId());
                    vo.setCollectionName(collectionName);

                    vo.setTokenId(orderNftEntity.getTokenId());
                    vo.setNftId(orderNftEntity.getNftId());

                    Optional<NftEntity> nftEntityOptional = nftRepository.findById(orderNftEntity.getNftId());
                    if (nftEntityOptional.isPresent()) {
                        NftEntity nftEntity = nftEntityOptional.get();
                        vo.setNftPath(nftEntity.getPath());
                    }
                    collectibleVOList.add(vo);
                }
            }
        }

        pageModel.setTotal(total);
        pageModel.setRecords(collectibleVOList);
        return pageModel;
    }
}
