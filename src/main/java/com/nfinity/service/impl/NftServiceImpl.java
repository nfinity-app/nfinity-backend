package com.nfinity.service.impl;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.nfinity.aws.S3Util;
import com.nfinity.entity.FolderNftEntity;
import com.nfinity.entity.NftEntity;
import com.nfinity.entity.UserEntity;
import com.nfinity.enums.MintStatus;
import com.nfinity.enums.UploadType;
import com.nfinity.repository.FolderNftRepository;
import com.nfinity.repository.NftRepository;
import com.nfinity.repository.UserRepository;
import com.nfinity.service.NftService;
import com.nfinity.vo.NftVO;
import com.nfinity.vo.PageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

import static com.nfinity.constant.Constant.S3_FILE_PATH;

@Slf4j
@Service
@RequiredArgsConstructor
public class NftServiceImpl implements NftService {
    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final NftRepository nftRepository;
    private final FolderNftRepository folderNftRepository;
    private final UserRepository userRepository;
    private final S3Util s3Util;

    @Override
    public PageModel<NftVO> uploadNftFiles(List<MultipartFile> multipartFileList, Long userId) throws Exception {
        Optional<UserEntity> optional = userRepository.findById(userId);
        String email = null;
        if(optional.isPresent()){
            email = optional.get().getEmail();
        }

        String s3Dir = s3Util.preUploadFiles(multipartFileList, email, UploadType.NFT.getValue());
        List<S3ObjectSummary> s3ObjectSummaries = s3Util.listS3Objects();
        return saveNftsToDB(s3ObjectSummaries, s3Dir);
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> multipartFiles, Long userId, int type) throws Exception {
        List<String> urls = new ArrayList<>();

        Optional<UserEntity> optional = userRepository.findById(userId);
        String email = null;
        if(optional.isPresent()){
            email = optional.get().getEmail();
        }

        String s3Dir = s3Util.preUploadFiles(multipartFiles, email, type);
        for(MultipartFile file : multipartFiles){
            urls.add(S3_FILE_PATH + bucketName + File.separator + s3Dir + File.separator + file.getOriginalFilename());
        }
        return urls;
    }

    @Transactional
    public PageModel<NftVO> saveNftsToDB(List<S3ObjectSummary> s3ObjectSummaries, String s3Dir){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Map<Long, String> folderNftMap = new HashMap<>();
        List<NftEntity> nftEntityList = new ArrayList<>();
        List<NftVO> nftVOList = new ArrayList<>();
        List<FolderNftEntity> folderNftEntityList = new ArrayList<>();

        //1. save data to table nft
        for (S3ObjectSummary os : s3ObjectSummaries) {
            //1. save data to table nft
            String key = os.getKey();
            if(key.startsWith(s3Dir)) {
                log.info("[upload nfts] get image:  " + key);

                NftEntity nftEntity = new NftEntity();
                nftEntity.setPath(S3_FILE_PATH + bucketName + File.separator + key);
                nftEntity.setMintStatus(MintStatus.INIT.getValue());
                nftEntity.setCreateTime(timestamp);
                nftEntity.setUpdateTime(timestamp);

                NftEntity savedNftEntity = nftRepository.save(nftEntity);

                nftEntityList.add(savedNftEntity);

                //2.get nft id, and correlate nft id and s3 folder name
                int index = key.lastIndexOf("/");
                String s3FolderName = key.substring(0, index);
                folderNftMap.put(savedNftEntity.getId(), s3FolderName);
            }
        }

        //2. save data to table folder_nft
        for(Map.Entry<Long, String> entry : folderNftMap.entrySet()) {
            FolderNftEntity folderNftEntity = new FolderNftEntity();
            folderNftEntity.setNftId(entry.getKey());
            folderNftEntity.setS3FolderName(entry.getValue());
            folderNftEntity.setCreateTime(timestamp);
            folderNftEntity.setUpdateTime(timestamp);
            folderNftEntityList.add(folderNftEntity);
        }
        folderNftRepository.saveAll(folderNftEntityList);

        for(NftEntity nftEntity : nftEntityList){
            NftVO nftVO = new NftVO();
            BeanUtils.copyProperties(nftEntity, nftVO);
            nftVOList.add(nftVO);
        }

        PageModel<NftVO> pageModel = new PageModel<>();
        pageModel.setTotal(nftEntityList.size());
        pageModel.setRecords(nftVOList);
        return pageModel;
    }
}
