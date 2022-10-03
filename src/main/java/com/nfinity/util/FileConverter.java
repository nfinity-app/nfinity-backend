package com.nfinity.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileConverter {

    public static File multipartFileToFile(MultipartFile multipartFile, String path) throws IOException {
        Files.createDirectories(Paths.get(path));

        String filePath = path + (Objects.equals(multipartFile.getOriginalFilename(), "") ? multipartFile.getName() : multipartFile.getOriginalFilename());
        File file = new File(Objects.requireNonNull(filePath));

        if(!file.exists()) {
            Files.copy(multipartFile.getInputStream(), Path.of(filePath));
        }

        return file;
    }
}
