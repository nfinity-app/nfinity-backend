package com.nfinity.service;

import com.nfinity.aws.S3Util;
import com.nfinity.entity.CollectionEntity;
import com.nfinity.repository.CollectionRepository;
import com.nfinity.util.FileConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class CollectionService {
//    @Value("${aws.s3.bucket.name}")
//    private String bucketName;
//    private CollectionRepository collectionRepository;
    private S3Util s3Util;

//    public List<CollectionEntity> getCollectionList(){
//        return collectionRepository.findAll();
//    }

    public void uploadNftFiles(List<MultipartFile> multipartFileList) throws IOException {
        String bucketName = "nft.business.bucket.test";
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
}
