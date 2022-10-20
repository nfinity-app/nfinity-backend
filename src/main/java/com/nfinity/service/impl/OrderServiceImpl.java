package com.nfinity.service.impl;

import com.nfinity.entity.*;
import com.nfinity.enums.ErrorCode;
import com.nfinity.enums.MintStatus;
import com.nfinity.enums.OrderStatus;
import com.nfinity.enums.Status;
import com.nfinity.exception.BusinessException;
import com.nfinity.repository.*;
import com.nfinity.service.OrderService;
import com.nfinity.util.BeansUtil;
import com.nfinity.vo.CollectibleVO;
import com.nfinity.vo.OrderVO;
import com.nfinity.vo.PageModel;
import com.nfinity.vo.PreOrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CollectionRepository collectionRepository;
    private final OrderNftRepository orderNftRepository;
    private final CollectionFolderNftRepository collectionFolderNftRepository;
    private final NftRepository nftRepository;
    private final ChainNftContractRepository chainNftContractRepository;

    @Override
    public Long createPreOrder(PreOrderVO vo) {
        Long collectionId = vo.getCollectionId();
        int mintQty = vo.getMintQty();

        int remainingQty = collectionFolderNftRepository.countAllByCollectionIdAndMintStatus(collectionId);
        if(mintQty <= remainingQty){
            //place a reservation
            return createOrder(vo, OrderStatus.UNPAID.getValue());
        }else{
            throw new BusinessException(ErrorCode.NOT_ENOUGH);
        }
    }

    @Override
    @Transactional
    public Long updateOrder(OrderVO vo) {
        Optional<OrderEntity> optional = orderRepository.findById(vo.getId());
        if(optional.isPresent()){
            OrderEntity orderEntity = optional.get();
            BeanUtils.copyProperties(vo, orderEntity, BeansUtil.getNullFields(vo));
            orderEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            return orderRepository.save(orderEntity).getId();
        }else{
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
    }

    private Long createOrder(PreOrderVO vo, int orderStatus){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //1. save data to table order
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(vo, orderEntity, BeansUtil.getNullFields(vo));
        orderEntity.setStatus(orderStatus);
        orderEntity.setCreateTime(timestamp);
        orderEntity.setUpdateTime(timestamp);
        Long orderId = orderRepository.save(orderEntity).getId();

        int mintQty = vo.getMintQty();

        List<CollectionFolderNftEntity> collectionFolderNftEntityList = collectionFolderNftRepository
                .findAllByCollectionIdAndNftStatus(vo.getCollectionId(), Status.ENABLE.getValue());

        if(!CollectionUtils.isEmpty(collectionFolderNftEntityList)) {
            int size = collectionFolderNftEntityList.size();
            for (int i = 0; i < size && mintQty > 0; i++) {
                CollectionFolderNftEntity entity = collectionFolderNftEntityList.get(i);
                Optional<NftEntity> optional = nftRepository.findById(entity.getNftId());
                if(optional.isPresent()){
                    NftEntity nftEntity = optional.get();

                    if(nftEntity.getMintStatus() == MintStatus.DEPLOYED.getValue()) {
                        //2. save data to table order_collection_nft
                        OrderNftEntity orderNftEntity = new OrderNftEntity();
                        orderNftEntity.setOrderId(orderId);
                        orderNftEntity.setNftId(nftEntity.getId());

                        //TODO: remove, just for ios test
                        orderNftEntity.setTokenId((long) new Random().nextInt(99));

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

        //TODO: remove, just for ios Test
        //update minted qty
        ChainNftContractEntity chainNftContractEntity = chainNftContractRepository.findByCollectionId(vo.getCollectionId());
        if(Objects.nonNull(chainNftContractEntity)){
            long mintedQty = chainNftContractEntity.getMintNum();
            chainNftContractEntity.setMintNum(mintedQty + vo.getMintQty());
            chainNftContractEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            chainNftContractRepository.save(chainNftContractEntity);
        }

        return orderId;
    }

    @Override
    public PageModel<CollectibleVO> getCollectibles(Long userId, int page, int size) {
        PageModel<CollectibleVO> pageModel = new PageModel<>();
        List<CollectibleVO> collectibleVOList = new ArrayList<>();

        int total = orderRepository.countByUserIdAndStatus(userId);
        List<OrderNftEntity> orderNftEntityList = orderNftRepository.findAllByUserIdAndStatus(userId, PageRequest.of(page - 1, size));

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
