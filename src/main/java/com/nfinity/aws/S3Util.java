package com.nfinity.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Configuration
public class S3Util {
    public void uploadFileListToS3(String bucketName, String destDir, String srcDir, List<File> files) {

        TransferManager ferMgr = TransferManagerBuilder.standard().build();
        try {
            MultipleFileUpload xfer = ferMgr.uploadFileList(bucketName,
                    destDir, new File(srcDir), files);
            // loop with Transfer.isDone()
            XferMgrProgress.showTransferProgress(xfer);
            // or block with Transfer.waitForCompletion()
            XferMgrProgress.waitForCompletion(xfer);
        } catch (AmazonServiceException e) {
            System.out.println("upload to s3: " + Arrays.toString(e.getStackTrace()));
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        ferMgr.shutdownNow();
    }
}
