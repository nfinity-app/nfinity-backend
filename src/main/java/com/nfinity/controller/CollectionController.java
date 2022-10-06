package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.CollectionService;
import com.nfinity.vo.*;
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

    @GetMapping("/collections/{id}")
    public Result<CollectionOutputVO> getCollectionDetail(@PathVariable("id") Long collectionId){
        try {
            CollectionOutputVO collectionOutputVO = collectionService.getCollectionDetail(collectionId);
            return Result.succeed(ErrorCode.OK, collectionOutputVO);
        }catch (Exception e){
            log.error("get collection detail error.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @PatchMapping("/collections/{id}")
    public Result<Integer> editCollectionDetail(@PathVariable("id") Long collectionId, @RequestBody CollectionDetailVO vo){
        try {
            int count = collectionService.editCollectionDetail(collectionId, vo);
            return Result.succeed(ErrorCode.OK, count);
        }catch (Exception e){
            log.error("edit collection detail error.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @PostMapping("/collection/draft")
    public Result<Long> saveDraftCollection(@RequestBody DraftCollectionVO vo){
        try {
            Long collectionId = collectionService.saveDraftCollection(vo);
            return Result.succeed(ErrorCode.OK, collectionId);
        }catch (Exception e){
            log.error("save draft collection failed.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @GetMapping("/collections/draft/{id}")
    public Result<DraftCollectionVO> getDraftCollectionDetail(@PathVariable("id") Long collectionId){
        try {
            DraftCollectionVO draftCollectionVO = collectionService.getDraftCollectionDetail(collectionId);
            return Result.succeed(ErrorCode.OK, draftCollectionVO);
        }catch (Exception e){
            log.error("get collection detail error.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }
}
