package com.nfinity.controller;

import com.nfinity.entity.CollectionEntity;
import com.nfinity.service.CollectionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/nft-business/v1")
@AllArgsConstructor
public class CollectionController {
    private CollectionService collectionService;


//    @GetMapping("/collections")
//    public List<CollectionEntity> getCollectionList(@RequestParam(required = false, defaultValue = "1") int page,
//                                                    @RequestParam(required = false, defaultValue = "50") int size){
//        return collectionService.getCollectionList();
//    }

    @PostMapping("/nfts")
    public void uploadNfts(HttpServletRequest request) throws IOException {
        //get files from http request
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> multipartFile = multipartHttpServletRequest.getFiles("files");
        System.out.println("multipartFile: " + multipartFile.size());

        collectionService.uploadNftFiles(multipartFile);
    }
}
