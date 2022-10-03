package com.nfinity.service.impl;

import com.nfinity.entity.CollectionEntity;
import com.nfinity.entity.CollectionFolderNftEntity;
import com.nfinity.enums.Status;
import com.nfinity.repository.CollectionFolderNftRepository;
import com.nfinity.repository.CollectionRepository;
import com.nfinity.service.CollectionService;
import com.nfinity.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        collectionEntity.setStatus(Status.ENABLE.getValue());

        Long collectionId = collectionRepository.save(collectionEntity).getId();

        //2. save data to table collection_folder_nft
        List<CollectionFolderNftEntity> collectionFolderNftEntityList = new ArrayList<>();
        for(NftVO nftRequestVO: vo.getRecords()) {
            CollectionFolderNftEntity collectionFolderNftEntity = new CollectionFolderNftEntity();
            collectionFolderNftEntity.setCollectionId(collectionId);
            collectionFolderNftEntity.setFolderId(vo.getFolderId());
            collectionFolderNftEntity.setNftId(nftRequestVO.getId());
            collectionFolderNftEntityList.add(collectionFolderNftEntity);
        }

        collectionFolderNftRepository.saveAll(collectionFolderNftEntityList);

        //TODO: 3. update nft status in the table nft

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
}

