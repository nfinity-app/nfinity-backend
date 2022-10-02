package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.CollectionService;
import com.nfinity.vo.CollectionInputVO;
import com.nfinity.vo.CollectionOutputVO;
import com.nfinity.vo.PageModel;
import com.nfinity.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/nft-business/v1")
@AllArgsConstructor
public class CollectionController {
    private CollectionService collectionService;

    @GetMapping("/collections")
    public Result<PageModel<CollectionOutputVO>> getCollectionList(@RequestParam(required = false, defaultValue = "1") int page,
                                                                   @RequestParam(required = false, defaultValue = "50") int size){
        try {
            PageModel<CollectionOutputVO> pageModel = collectionService.getCollectionList(page, size);
            return Result.succeed(ErrorCode.OK, pageModel);
        }catch (Exception e){
            log.error("get collection list error.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @PostMapping("/collection")
    public Result<Integer> createCollection(@RequestBody CollectionInputVO collectionRequestVO){
        try {
            int collectionId = collectionService.createCollection(collectionRequestVO);
            return Result.succeed(ErrorCode.OK, collectionId);
        }catch (Exception e){
            log.error("create collection failed.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @PostMapping("/nfts")
    public void uploadNfts(HttpServletRequest request) throws IOException {
        //get files from http request
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> multipartFile = multipartHttpServletRequest.getFiles("files");
        log.info("[upload nfts] multipartFile: " + multipartFile.size());

        collectionService.uploadNftFiles(multipartFile);
    }
}
