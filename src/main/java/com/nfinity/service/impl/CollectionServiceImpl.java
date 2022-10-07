package com.nfinity.service.impl;

import com.nfinity.entity.*;
import com.nfinity.enums.ContractStatus;
import com.nfinity.enums.MintStatus;
import com.nfinity.enums.Status;
import com.nfinity.repository.*;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final CollectionFolderNftRepository collectionFolderNftRepository;
    private final DraftCollectionRepository draftCollectionRepository;
    private final DraftCollectionFolderNftRepository draftCollectionFolderNftRepository;
    private final FolderRepository folderRepository;
    private final NftRepository nftRepository;
    private final ChainNftContractRepository chainNftContractRepository;

    public PageModel<CollectionOutputVO> getCollectionList(int page, int size){
        PageModel<CollectionOutputVO> pageModel = new PageModel<>();

        long total = collectionRepository.count();
        List<CollectionOutputVO> collectionVOList = new ArrayList<>();

        List<CollectionEntity> collectionEntityList = collectionRepository.findAll(PageRequest.of(page - 1, size)).toList();

        for(CollectionEntity collectionEntity : collectionEntityList) {
            CollectionOutputVO collectionVO = new CollectionOutputVO();
            BeanUtils.copyProperties(collectionEntity, collectionVO);

            Long collectionId = collectionEntity.getId();

            ChainNftContractEntity chainNftContractEntity = chainNftContractRepository.findByCollectionId(collectionId);

            collectionVO.setAddress(chainNftContractEntity.getContractAddr());
            collectionVO.setMintedQty((int) chainNftContractEntity.getMintNum());
            collectionVO.setRevenue(chainNftContractEntity.getProfit());
            collectionVOList.add(collectionVO);
        }

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

        ChainNftContractEntity chainNftContractEntity = chainNftContractRepository.findByCollectionId(collectionId);
        collectionOutputVO.setRevenue(chainNftContractEntity.getProfit());
        collectionOutputVO.setAddress(chainNftContractEntity.getContractAddr());
        collectionOutputVO.setMintedQty((int) chainNftContractEntity.getMintNum());

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
    public Long saveDraftCollection(DraftCollectionVO vo) {
        DraftCollectionEntity entity = new DraftCollectionEntity();
        BeanUtils.copyProperties(vo, entity, BeansUtil.getNullFields(vo));
        entity.setName(vo.getCollectionName());
        entity.setStatus(MintStatus.DRAFTED.getValue());

        Long collectionId = draftCollectionRepository.save(entity).getId();

        if(!CollectionUtils.isEmpty(vo.getRecords())){
            List<DraftCollectionFolderNftEntity> nftEntityList = new ArrayList<>();
            for(NftVO nftVO : vo.getRecords()){
                DraftCollectionFolderNftEntity nftEntity = new DraftCollectionFolderNftEntity();
                nftEntity.setCollectionId(collectionId);
                nftEntity.setFolderId(vo.getFolderId());
                nftEntity.setNftId(nftVO.getId());
                nftEntity.setNftStatus(nftVO.getStatus());
                nftEntityList.add(nftEntity);
            }
            draftCollectionFolderNftRepository.saveAll(nftEntityList);
        }

        return collectionId;
    }

    @Override
    public DraftCollectionVO getDraftCollectionDetail(Long collectionId) {
        DraftCollectionVO draftCollectionVO = new DraftCollectionVO();

        DraftCollectionEntity draftCollectionEntity;
        Optional<DraftCollectionEntity> optional = draftCollectionRepository.findById(collectionId);
        if(optional.isPresent()){
            draftCollectionEntity = optional.get();
            BeanUtils.copyProperties(draftCollectionEntity, draftCollectionVO, BeansUtil.getNullFields(draftCollectionEntity));
            draftCollectionVO.setCollectionName(draftCollectionEntity.getName());
        }

        List<DraftCollectionFolderNftEntity> draftNftEntityList = draftCollectionFolderNftRepository.findAllByCollectionId(collectionId);
        //one collection corresponds to only one folder
        if(!CollectionUtils.isEmpty(draftNftEntityList)) {
            Long folderId = draftNftEntityList.get(0).getFolderId();
            String folderName = folderRepository.findById(folderId).get().getName();
            draftCollectionVO.setFolderId(folderId);
            draftCollectionVO.setFolderName(folderName);

            List<NftVO> nftVOList = new ArrayList<>(draftNftEntityList.size());
            for (DraftCollectionFolderNftEntity entity : draftNftEntityList) {
                NftVO vo = new NftVO();
                Long nftId = entity.getNftId();
                String path = nftRepository.findById(nftId).get().getPath();
                int nftStatus = entity.getNftStatus();

                vo.setId(nftId);
                vo.setPath(path);
                vo.setStatus(nftStatus);
                nftVOList.add(vo);
            }
            draftCollectionVO.setRecords(nftVOList);
        }

        return draftCollectionVO;
    }
}

