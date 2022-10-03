package com.nfinity.service.impl;

import com.nfinity.entity.FolderEntity;
import com.nfinity.entity.FolderNftEntity;
import com.nfinity.repository.FolderNftRepository;
import com.nfinity.repository.FolderRepository;
import com.nfinity.service.FolderService;
import com.nfinity.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private final FolderNftRepository folderNftRepository;

    @Override
    @Transactional
    public Long createFolderWithNfts(FolderCreationInputVO folderInputVO) {
        //1. save data to table folder
        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setName(folderInputVO.getFolderName());
        folderEntity.setIcon(folderInputVO.getRecords().get(0).getPath());

        Long folderId = folderRepository.save(folderEntity).getId();

        //2. save data to table folder_nft
        List<FolderNftEntity> folderNftEntityList = new ArrayList<>(folderInputVO.getRecords().size());
        for(NftVO vo : folderInputVO.getRecords()){
            FolderNftEntity folderNftEntity = folderNftRepository.findByNftId(vo.getId());
            folderNftEntity.setFolderId(folderId);
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
        //1. delete from table folder by folder id
        List<FolderEntity> folderEntityList = new ArrayList<>();
        List<Long> folderIds = new ArrayList<>();
        for(FolderVO folderVO : folderVOList) {
            FolderEntity folderEntity = new FolderEntity();
            folderEntity.setId(folderVO.getId());
            folderEntityList.add(folderEntity);
            folderIds.add(folderVO.getId());
        }
        folderRepository.deleteAll(folderEntityList);

        //2. delete from table folder_nft by folder id
        for(Long folderId : folderIds) {
            folderNftRepository.deleteAllByFolderId(folderId);
        }

        return folderVOList.size();
    }
}
