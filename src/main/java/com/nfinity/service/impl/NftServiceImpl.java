package com.nfinity.service.impl;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.nfinity.aws.S3Util;
import com.nfinity.entity.FolderNftEntity;
import com.nfinity.entity.NftEntity;
import com.nfinity.enums.Status;
import com.nfinity.repository.FolderNftRepository;
import com.nfinity.repository.NftRepository;
import com.nfinity.service.NftService;
import com.nfinity.util.FileConverter;
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
import java.io.IOException;
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
    private final S3Util s3Util;

    @Override
    public PageModel<NftVO> uploadNftFiles(List<MultipartFile> multipartFileList) throws IOException {
        String s3Dir = uploadNftFilesToS3(multipartFileList);
        List<S3ObjectSummary> s3ObjectSummaries = listS3Objects();
        return saveNftsToDB(s3ObjectSummaries, s3Dir);
    }

    private String uploadNftFilesToS3(List<MultipartFile> multipartFileList) throws IOException {
        //1. get src files, create src directory
        String srcDir = "s3" + File.separator;
        List<File> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            files.add(FileConverter.multipartFileToFile(multipartFile, srcDir));
        }
        for (File file : files) {
            log.debug("[upload nfts] src file name: " + file.getName());
        }

        //2. create s3 directory
        Random random = new Random();
        //todo: To update a real account username until account creation
        String s3Dir = "username/" + random.nextInt(10000);
        log.info("[upload nfts] dest(s3) folder name: " + s3Dir);

        //3. upload files to s3
        s3Util.uploadFileListToS3(bucketName, s3Dir, srcDir, files);

        return s3Dir;
    }

    private List<S3ObjectSummary> listS3Objects(){
        log.debug("[upload nfts] Objects in S3 bucket: " + bucketName);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        return result.getObjectSummaries();
    }

    @Transactional
    PageModel<NftVO> saveNftsToDB(List<S3ObjectSummary> s3ObjectSummaries, String s3Dir){
        NftVO nftVO = new NftVO();
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
                nftEntity.setStatus(Status.ENABLE.getValue());
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
            folderNftEntityList.add(folderNftEntity);
        }
        folderNftRepository.saveAll(folderNftEntityList);

        for(NftEntity nftEntity : nftEntityList){
            BeanUtils.copyProperties(nftEntity, nftVO);
            nftVOList.add(nftVO);
        }

        PageModel<NftVO> pageModel = new PageModel<>();
        pageModel.setTotal(nftEntityList.size());
        pageModel.setRecords(nftVOList);
        return pageModel;
    }
}
