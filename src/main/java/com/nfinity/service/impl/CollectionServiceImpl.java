package com.nfinity.service.impl;

import com.nfinity.entity.*;
import com.nfinity.enums.ContractStatus;
import com.nfinity.enums.DisplayStatus;
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
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final CollectionFolderNftRepository collectionFolderNftRepository;
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

            if(Objects.nonNull(chainNftContractEntity)) {
                collectionVO.setAddress(chainNftContractEntity.getContractAddr());
                collectionVO.setMintedQty((int) chainNftContractEntity.getMintNum());
                collectionVO.setRevenue(chainNftContractEntity.getProfit());
            }

            collectionVOList.add(collectionVO);
        }

        pageModel.setTotal(total);
        pageModel.setRecords(collectionVOList);
        return pageModel;
    }

    @Override
    @Transactional
    public Long createCollection(CollectionInputVO vo) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //1. save data to table collection, and get collection id
        CollectionEntity collectionEntity = new CollectionEntity();
        BeanUtils.copyProperties(vo, collectionEntity, BeansUtil.getNullFields(vo));
        collectionEntity.setName(vo.getCollectionName());
        collectionEntity.setIcon(vo.getRecords().get(0).getPath());
        collectionEntity.setAddress("");
        collectionEntity.setRevenue(new BigDecimal(0));
        collectionEntity.setMintedQty(0);
        collectionEntity.setStatus(DisplayStatus.PENDING.getValue());
        collectionEntity.setContractStatus(ContractStatus.INIT.getValue());
        collectionEntity.setCreateTime(timestamp);
        collectionEntity.setUpdateTime(timestamp);

        Long collectionId = collectionRepository.save(collectionEntity).getId();

        List<CollectionFolderNftEntity> collectionFolderNftEntityList = new ArrayList<>();
        for(NftVO nftVO: vo.getRecords()) {
            //2. save data to table collection_folder_nft
            if(Status.ENABLE.getValue() == nftVO.getStatus()) {
                CollectionFolderNftEntity collectionFolderNftEntity = new CollectionFolderNftEntity();
                collectionFolderNftEntity.setCollectionId(collectionId);
                collectionFolderNftEntity.setFolderId(vo.getFolderId());
                collectionFolderNftEntity.setNftId(nftVO.getId());
                collectionFolderNftEntity.setNftStatus(nftVO.getStatus());
                collectionFolderNftEntity.setCreateTime(timestamp);
                collectionFolderNftEntity.setUpdateTime(timestamp);
                collectionFolderNftEntityList.add(collectionFolderNftEntity);
            }

            //3. update mint status from unminted to minted in the table nft
            Optional<NftEntity> nftEntityOptional = nftRepository.findById(nftVO.getId());
            if(nftEntityOptional.isPresent()){
                NftEntity nftEntity = nftEntityOptional.get();
                nftEntity.setMintStatus(MintStatus.MINTED.getValue());
                nftRepository.save(nftEntity);
            }
        }
        collectionFolderNftRepository.saveAll(collectionFolderNftEntityList);

        //4. update mint status from unminted to minted in the table folder
        Optional<FolderEntity> folderEntityOptional = folderRepository.findById(vo.getFolderId());
        if(folderEntityOptional.isPresent()){
            FolderEntity folderEntity = folderEntityOptional.get();
            folderEntity.setMintStatus(MintStatus.MINTED.getValue());
            folderRepository.save(folderEntity);
        }

        return collectionId;
    }

    @Override
    public int editCollectionDetail(Long collectionId, CollectionDetailInputVO vo) {
        Optional<CollectionEntity> optional = collectionRepository.findById(collectionId);
        if(optional.isEmpty()){
            return 0;
        }
        CollectionEntity entity = optional.get();

        if(Objects.nonNull(vo.getMintPrice())) {
            entity.setMintPrice(vo.getMintPrice());
        }
        if(Objects.nonNull(vo.getMintStatus())) {
            entity.setStatus(vo.getMintStatus());
        }
        if(Objects.nonNull(vo.getDescription())) {
            entity.setDescription(vo.getDescription());
        }

        entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        CollectionEntity updatedEntity = collectionRepository.save(entity);

        return entity.equals(updatedEntity) ? 0 : 1;
    }

    @Override
    @Transactional
    public Long saveDraftCollection(DraftCollectionVO vo) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        CollectionEntity collectionEntity = new CollectionEntity();
        BeanUtils.copyProperties(vo, collectionEntity, BeansUtil.getNullFields(vo));
        collectionEntity.setName(vo.getCollectionName());
        collectionEntity.setStatus(DisplayStatus.DRAFTED.getValue());
        collectionEntity.setCreateTime(timestamp);
        collectionEntity.setUpdateTime(timestamp);

        Long collectionId = collectionRepository.save(collectionEntity).getId();

        if(!CollectionUtils.isEmpty(vo.getRecords())){
            List<CollectionFolderNftEntity> entityList = new ArrayList<>();
            for(NftVO nftVO : vo.getRecords()){
                CollectionFolderNftEntity entity = new CollectionFolderNftEntity();
                entity.setCollectionId(collectionId);
                entity.setFolderId(vo.getFolderId());
                entity.setNftId(nftVO.getId());
                entity.setNftStatus(nftVO.getStatus());
                entity.setCreateTime(timestamp);
                entity.setUpdateTime(timestamp);
                entityList.add(entity);
            }
            collectionFolderNftRepository.saveAll(entityList);
        }

        return collectionId;
    }

    @Override
    public CollectionDetailOutputVO getCollectionDetail(Long collectionId) {
        CollectionDetailOutputVO collectionVO = new CollectionDetailOutputVO();

        //1. get data from table collection
        Optional<CollectionEntity> optional = collectionRepository.findById(collectionId);
        if(optional.isPresent()){
            CollectionEntity collectionEntity = optional.get();
            BeanUtils.copyProperties(collectionEntity, collectionVO, BeansUtil.getNullFields(collectionEntity));
            collectionVO.setCollectionName(collectionEntity.getName());
        }

        //2. get data from chain_nft_contract
        ChainNftContractEntity chainNftContractEntity = chainNftContractRepository.findByCollectionId(collectionId);
        if(Objects.nonNull(chainNftContractEntity)) {
            collectionVO.setRevenue(chainNftContractEntity.getProfit());
            collectionVO.setAddress(chainNftContractEntity.getContractAddr());
            collectionVO.setMintedQty((int) chainNftContractEntity.getMintNum());
        }

        //3. get data from table collection_folder_nft
        List<CollectionFolderNftEntity> entityList = collectionFolderNftRepository.findAllByCollectionId(collectionId);
        //one collection corresponds to only one folder
        if(!CollectionUtils.isEmpty(entityList)) {
            Long folderId = entityList.get(0).getFolderId();
            collectionVO.setFolderId(folderId);

            //4. get data from table folder
            Optional<FolderEntity> folderEntityOptional = folderRepository.findById(folderId);
            if(folderEntityOptional.isPresent()) {
                String folderName = folderEntityOptional.get().getName();
                collectionVO.setFolderName(folderName);
            }

            //5. get data from table nft
            List<NftVO> nftVOList = new ArrayList<>(entityList.size());
            for (CollectionFolderNftEntity entity : entityList) {
                NftVO vo = new NftVO();
                Long nftId = entity.getNftId();
                vo.setId(nftId);

                Optional<NftEntity> nftEntityOptional = nftRepository.findById(nftId);
                if(nftEntityOptional.isPresent()) {
                    String path =nftEntityOptional.get().getPath();
                    vo.setPath(path);
                }

                int nftStatus = entity.getNftStatus();
                vo.setStatus(nftStatus);
                nftVOList.add(vo);
            }
            collectionVO.setRecords(nftVOList);
        }

        return collectionVO;
    }

    /**
     * fixed gas fee
     * @return gas fee
     */
    @Override
    public String getGasFee(String chainType, Integer txType) {
        BigDecimal gasPrice = new BigDecimal("5000000000");
        BigDecimal gasLimit = new BigDecimal("5000000");
        BigDecimal gasFee = gasPrice.multiply(gasLimit).divide(new BigDecimal("1000000000000000000"), 10, RoundingMode.HALF_UP);
        return gasFee.stripTrailingZeros().toPlainString();
    }
}

