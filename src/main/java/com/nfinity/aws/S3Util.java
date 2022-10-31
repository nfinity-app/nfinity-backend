package com.nfinity.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.nfinity.enums.UploadType;
import com.nfinity.util.FileConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class S3Util {
    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public String preUploadFiles(List<MultipartFile> multipartFileList, String email, int type) throws Exception {
        //1. get src files, create src directory
        String srcDir = "s3" + File.separator;
        List<File> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            files.add(FileConverter.multipartFileToFile(multipartFile, srcDir));
        }
        for (File file : files) {
            log.debug("[upload files] src file name: " + file.getName());
        }

        //2. create s3 directory
        StringBuilder s3DirSb = new StringBuilder().append(email).append("/");
        if(UploadType.NFT.getValue() == type) {
            Random random = new Random();
            s3DirSb.append(random.nextInt(10000));
        }else if(UploadType.PROFILE_PHOTO.getValue() == type){
            s3DirSb.append("profile_photo");
        }else if(UploadType.BUSINESS_LOGO.getValue() == type){
            s3DirSb.append("business_logo");
        }
        String s3Dir = s3DirSb.toString();
        log.info("[upload files] dest(s3) folder name: " + s3Dir);

        //3. upload files to s3
        uploadFileListToS3(bucketName, s3Dir, srcDir, files);

        //4. clear files in the srcDir
        FileUtils.cleanDirectory(new File(srcDir));

        return s3Dir;
    }
    public void uploadFileListToS3(String bucketName, String destDir, String srcDir, List<File> files) throws Exception {

        TransferManager ferMgr = TransferManagerBuilder.standard().build();
        try {
            MultipleFileUpload xfer = ferMgr.uploadFileList(bucketName,
                    destDir, new File(srcDir), files);
            // loop with Transfer.isDone()
            XferMgrProgress.showTransferProgress(xfer);
            // or block with Transfer.waitForCompletion()
            XferMgrProgress.waitForCompletion(xfer);
        } catch (AmazonServiceException e) {
            log.info("upload files to s3: " + Arrays.toString(e.getStackTrace()));
            throw new Exception("[upload files to s3 failed]: ", e);
        }
        ferMgr.shutdownNow();
    }

    public List<S3ObjectSummary> listS3Objects(){
        log.debug("[upload files] Objects in S3 bucket: " + bucketName);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        return result.getObjectSummaries();
    }
}
