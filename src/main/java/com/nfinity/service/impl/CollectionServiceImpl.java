package com.nfinity.service.impl;

import com.nfinity.aws.S3Util;
import com.nfinity.entity.CollectionEntity;
import com.nfinity.entity.CollectionFolderNftEntity;
import com.nfinity.enums.Status;
import com.nfinity.repository.CollectionFolderNftRepository;
import com.nfinity.repository.CollectionRepository;
import com.nfinity.service.CollectionService;
import com.nfinity.util.FileConverter;
import com.nfinity.vo.CollectionInputVO;
import com.nfinity.vo.CollectionOutputVO;
import com.nfinity.vo.NftInputVO;
import com.nfinity.vo.PageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;
    private final CollectionRepository collectionRepository;
    private final CollectionFolderNftRepository collectionFolderNftRepository;
    private final S3Util s3Util;

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

    public void uploadNftFiles(List<MultipartFile> multipartFileList) throws IOException {
        String path = "s3" + File.separator;
        List<File> files = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFileList){
            files.add(FileConverter.multipartFileToFile(multipartFile, path));
        }

        Random random = new Random();
        //todo: To update a real account username until account creation
        String s3FolderName = "username/" + random.nextInt(10000);

        for(File file : files) {
            System.out.println("file name: " + file.getName());
        }
        System.out.println("folder name: " + s3FolderName);
        s3Util.uploadFileListToS3(bucketName, s3FolderName, path, files);
    }

    @Override
    @Transactional
    public int createCollection(CollectionInputVO vo) {
        //1. save data to table collection, and get collection id
        CollectionEntity collectionEntity = new CollectionEntity();
        BeanUtils.copyProperties(vo, collectionEntity);
        collectionEntity.setName(vo.getCollectionName());
        collectionEntity.setIcon(vo.getRecords().get(0).getPath());
        collectionEntity.setAddress("");
        collectionEntity.setRevenue(new BigDecimal(0));
        collectionEntity.setMintedQty(0);
        collectionEntity.setStatus(Status.ENABLE.getValue());

        int collectionId = collectionRepository.save(collectionEntity).getId();

        //2. save data to table collection_folder_nft
        List<CollectionFolderNftEntity> collectionFolderNftEntityList = new ArrayList<>();
        for(NftInputVO nftRequestVO: vo.getRecords()) {
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
}

