package com.nfinity.util;

import com.amazonaws.util.IOUtils;
import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileConverterTest {

//    @Test
    void multipartFileToFile() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("source.tmp", "hello world".getBytes());
        File file = FileConverter.multipartFileToFile(multipartFile, "s3/");
        assertEquals(FileUtil.readAsString(file), "hello world");
    }

//    @Test
    void multipartFileToFile_2() throws IOException {
        File file = new File("/Users/a1234/Downloads/images/image.jpeg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("fileItem", file.getName(), "image/jpeg", IOUtils.toByteArray(inputStream));
        File destFile = FileConverter.multipartFileToFile(multipartFile, "s3/");
        assertTrue(destFile.exists());
    }
}