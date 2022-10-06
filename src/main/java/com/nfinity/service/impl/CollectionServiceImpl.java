package com.nfinity.service.impl;

import com.nfinity.entity.CollectionEntity;
import com.nfinity.entity.CollectionFolderNftEntity;
import com.nfinity.entity.DraftCollectionEntity;
import com.nfinity.entity.DraftCollectionFolderNftEntity;
import com.nfinity.enums.ContractStatus;
import com.nfinity.enums.MintStatus;
import com.nfinity.enums.Status;
import com.nfinity.repository.CollectionFolderNftRepository;
import com.nfinity.repository.CollectionRepository;
import com.nfinity.repository.DraftCollectionFolderNftRepository;
import com.nfinity.repository.DraftCollectionRepository;
import com.nfinity.service.CollectionService;
import com.nfinity.util.BeansUtil;
import com.nfinity.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final CollectionFolderNftRepository collectionFolderNftRepository;
    private final DraftCollectionRepository draftCollectionRepository;
    private final DraftCollectionFolderNftRepository draftCollectionFolderNftRepository;

    public PageModel<CollectionOutputVO> getCollectionList(int page, int size){
        PageModel<CollectionOutputVO> pageModel = new PageModel<>();

        long total = collectionRepository.count();
        List<CollectionOutputVO> collectionVOList = collectionRepository.findAll(PageRequest.of(page - 1, size))
                .stream()
                .map(collectionEntity -> {
                    CollectionOutputVO collectionVO = new CollectionOutputVO();
                    BeanUtils.copyProperties(collectionEntity, collectionVO);
                    return collectionVO;
                })
                .collect(Collectors.toList());

        pageModel.setTotal(total);
        pageModel.setRecords(collectionVOList);
        return pageModel;
    }

    @Override
    @Transactional
    public Long createCollection(CollectionInputVO vo) {
        //1. save data to table collection, and get collection id
        CollectionEntity collectionEntity = new CollectionEntity();
        BeanUtils.copyProperties(vo, collectionEntity);
        collectionEntity.setName(vo.getCollectionName());
        collectionEntity.setIcon(vo.getRecords().get(0).getPath());
        collectionEntity.setAddress("");
        collectionEntity.setRevenue(new BigDecimal(0));
        collectionEntity.setMintedQty(0);
        collectionEntity.setStatus(MintStatus.PENDING.getValue());
        collectionEntity.setContractStatus(ContractStatus.INIT.getValue());

        Long collectionId = collectionRepository.save(collectionEntity).getId();

        //2. save data to table collection_folder_nft
        List<CollectionFolderNftEntity> collectionFolderNftEntityList = new ArrayList<>();
        for(NftVO nftVO: vo.getRecords()) {
            if(Status.ENABLE.getValue() == nftVO.getStatus()) {
                CollectionFolderNftEntity collectionFolderNftEntity = new CollectionFolderNftEntity();
                collectionFolderNftEntity.setCollectionId(collectionId);
                collectionFolderNftEntity.setFolderId(vo.getFolderId());
                collectionFolderNftEntity.setNftId(nftVO.getId());
                collectionFolderNftEntityList.add(collectionFolderNftEntity);
            }
        }
        collectionFolderNftRepository.saveAll(collectionFolderNftEntityList);

        return collectionId;
    }

    @Override
    public CollectionOutputVO getCollectionDetail(Long collectionId) {
        CollectionEntity collectionEntity = collectionRepository.findById(collectionId).get();

        CollectionOutputVO collectionOutputVO = new CollectionOutputVO();
        BeanUtils.copyProperties(collectionEntity, collectionOutputVO);

        return collectionOutputVO;
    }

    @Override
    public int editCollectionDetail(Long collectionId, CollectionDetailVO vo) {
        CollectionEntity entity = collectionRepository.findById(collectionId).get();

        if(Objects.nonNull(vo.getMintPrice())) {
            entity.setMintPrice(vo.getMintPrice());
        }
        if(Objects.nonNull(vo.getMintStatus())) {
            entity.setStatus(vo.getMintStatus());
        }
        if(Objects.nonNull(vo.getDescription())) {
            entity.setDescription(vo.getDescription());
        }

        CollectionEntity updatedEntity = collectionRepository.save(entity);

        return entity.equals(updatedEntity) ? 0 : 1;
    }

    @Override
    @Transactional
    public Long saveDraftCollection(DraftCollectionInputVO vo) {
        DraftCollectionEntity entity = new DraftCollectionEntity();
        BeanUtils.copyProperties(vo, entity, BeansUtil.getNullFields(vo));
        entity.setName(vo.getCollectionName());
        entity.setStatus(MintStatus.DRAFTED.getValue());

        Long collectionId = draftCollectionRepository.save(entity).getId();

        if(!CollectionUtils.isEmpty(vo.getRecords())){
            List<DraftCollectionFolderNftEntity> nftEntityList = new ArrayList<>();
            for(NftVO nftVO : vo.getRecords()){
                if(Status.ENABLE.getValue() == nftVO.getStatus()){
                    DraftCollectionFolderNftEntity nftEntity = new DraftCollectionFolderNftEntity();
                    nftEntity.setCollectionId(collectionId);
                    nftEntity.setFolderId(vo.getFolderId());
                    nftEntity.setNftId(nftVO.getId());
                    nftEntityList.add(nftEntity);
                }
            }
            draftCollectionFolderNftRepository.saveAll(nftEntityList);
        }

        return collectionId;
    }
}

