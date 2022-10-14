package com.nfinity.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class S3Util {
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
}
