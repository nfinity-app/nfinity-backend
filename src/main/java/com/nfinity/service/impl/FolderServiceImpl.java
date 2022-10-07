package com.nfinity.service.impl;

import com.nfinity.entity.FolderEntity;
import com.nfinity.entity.FolderNftEntity;
import com.nfinity.entity.NftEntity;
import com.nfinity.repository.FolderNftRepository;
import com.nfinity.repository.FolderRepository;
import com.nfinity.repository.NftRepository;
import com.nfinity.service.FolderService;
import com.nfinity.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private final FolderNftRepository folderNftRepository;
    private final NftRepository nftRepository;

    @Override
    @Transactional
    public Long createFolderWithNfts(FolderCreationInputVO folderInputVO) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //1. save data to table folder
        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setName(folderInputVO.getFolderName());
        folderEntity.setIcon(folderInputVO.getRecords().get(0).getPath());
        folderEntity.setCreateTime(timestamp);
        folderEntity.setUpdateTime(timestamp);

        Long folderId = folderRepository.save(folderEntity).getId();

        //2. save data to table folder_nft
        List<FolderNftEntity> folderNftEntityList = new ArrayList<>(folderInputVO.getRecords().size());
        for(NftVO vo : folderInputVO.getRecords()){
            FolderNftEntity folderNftEntity = folderNftRepository.findByNftId(vo.getId());
            folderNftEntity.setFolderId(folderId);
            folderNftEntity.setCreateTime(timestamp);
            folderNftEntity.setUpdateTime(timestamp);
            folderNftEntityList.add(folderNftEntity);
        }
        folderNftRepository.saveAll(folderNftEntityList);

        return folderId;
    }

    @Override
    public PageModel<FolderVO> getFolderList(int page, int size) {
        long total = folderRepository.count();
        List<FolderVO> voList = folderRepository.findAll(PageRequest.of(page - 1, size)).stream()
                .map(folderEntity -> {
                    FolderVO folderOutputVO = new FolderVO();
                    BeanUtils.copyProperties(folderEntity, folderOutputVO);
                    return folderOutputVO;
                }).collect(Collectors.toList());

        PageModel<FolderVO> pageModel = new PageModel<>();
        pageModel.setTotal(total);
        pageModel.setRecords(voList);
        return pageModel;
    }

    @Override
    @Transactional
    public int deleteFolders(FolderDeletionInputVO folderDeletionInputVO) {
        List<FolderVO> folderVOList = folderDeletionInputVO.getRecords();
        List<Long> folderIds = new ArrayList<>();

        //1. delete from table folder_nft by folder id
        for(FolderVO folderVO : folderVOList) {
            folderIds.add(folderVO.getId());
            folderNftRepository.deleteAllByFolderId(folderVO.getId());
        }

        //2. delete from table folder by folder
        folderRepository.deleteAllById(folderIds);

        return folderIds.size();
    }

    @Override
    public PageModel<NftVO> getFolderNfts(Long folderId, int page, int size) {
        List<NftVO> nftVOList = new ArrayList<>();

        int total = folderNftRepository.countByFolderId(folderId);

        List<Long> nftIds = folderNftRepository.findAllByFolderId(folderId, PageRequest.of(page - 1, size)).stream()
                .map(FolderNftEntity::getNftId).collect(Collectors.toList());

        List<NftEntity> nftEntities = nftRepository.findAllById(nftIds);

        for(NftEntity nftEntity : nftEntities){
            NftVO nftVO = new NftVO();
            BeanUtils.copyProperties(nftEntity, nftVO);
            nftVOList.add(nftVO);
        }

        PageModel<NftVO> pageModel = new PageModel<>();
        pageModel.setTotal(total);
        pageModel.setRecords(nftVOList);
        return pageModel;
    }

    @Override
    @Transactional
    public int deleteFolderNfts(Long folderId, NftsInputVO nftDeletionInputVO) {
        List<NftVO> nftVOList = nftDeletionInputVO.getRecords();
        List<Long> nftIds = new ArrayList<>();

        //1. delete nfts from table folder_nft
        for(NftVO nftVO : nftVOList){
            nftIds.add(nftVO.getId());
            folderNftRepository.deleteByFolderIdAndNftId(folderId, nftVO.getId());
        }

        //2. delete nfts from table nft
        nftRepository.deleteAllById(nftIds);

        return nftIds.size();
    }

    @Override
    public int addNftsToFolder(Long folderId, NftsInputVO nftsInputVO) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //add nfts to table folder_nft
        List<FolderNftEntity> folderNftEntityList = new ArrayList<>();
        for(NftVO nftVO : nftsInputVO.getRecords()){
            FolderNftEntity folderNftEntity = folderNftRepository.findByNftId(nftVO.getId());
            folderNftEntity.setFolderId(folderId);
            folderNftEntity.setCreateTime(timestamp);
            folderNftEntity.setUpdateTime(timestamp);
            folderNftEntityList.add(folderNftEntity);
        }
        folderNftRepository.saveAll(folderNftEntityList);

        return folderNftEntityList.size();
    }
}
