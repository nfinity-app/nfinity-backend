package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.CollectionService;
import com.nfinity.vo.CollectionInputVO;
import com.nfinity.vo.CollectionOutputVO;
import com.nfinity.vo.PageModel;
import com.nfinity.vo.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/nft-business/v1")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;

    @GetMapping("/collections")
    public Result<PageModel<CollectionOutputVO>> getCollectionList(@RequestParam(required = false, defaultValue = "1") int page,
                                                                   @RequestParam(required = false, defaultValue = "5") int size){
        try {
            PageModel<CollectionOutputVO> pageModel = collectionService.getCollectionList(page, size);
            return Result.succeed(ErrorCode.OK, pageModel);
        }catch (Exception e){
            log.error("get collection list error.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @PostMapping("/collection")
    public Result<Long> createCollection(@RequestBody CollectionInputVO collectionRequestVO){
        try {
            Long collectionId = collectionService.createCollection(collectionRequestVO);
            return Result.succeed(ErrorCode.OK, collectionId);
        }catch (Exception e){
            log.error("create collection failed.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }
}
