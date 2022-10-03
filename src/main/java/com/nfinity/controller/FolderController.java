package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.FolderService;
import com.nfinity.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/nft-business/v1")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/folder/nfts")
    public Result<Long> createFolderWithNfts(@RequestBody FolderCreationInputVO folderInputVO){
        try {
            Long folderId = folderService.createFolderWithNfts(folderInputVO);
            return Result.succeed(ErrorCode.OK, folderId);
        }catch (Exception e){
            log.error("create folder with nfts failed.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @GetMapping("/folders")
    public Result<PageModel<FolderVO>> getFolderList(@RequestParam(required = false, defaultValue = "1") int page,
                                                     @RequestParam(required = false, defaultValue = "6") int size){
        try {
            PageModel<FolderVO> folderVOPageModel = folderService.getFolderList(page, size);
            return Result.succeed(ErrorCode.OK, folderVOPageModel);
        }catch (Exception e){
            log.error("get folder list error.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @DeleteMapping("/folders")
    public Result<Integer> deleteFolders(@RequestBody FolderDeletionInputVO folderDeletionInputVO){
        try {
            int count = folderService.deleteFolders(folderDeletionInputVO);
            return Result.succeed(ErrorCode.OK, count);
        }catch (Exception e){
            log.error("delete folders error.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @GetMapping("/folders/{id}/nfts")
    public Result<PageModel<NftVO>> getFolderNfts(@PathVariable("id") Long folderId, @RequestParam(required = false, defaultValue = "1") int page,
                                                  @RequestParam(required = false, defaultValue = "50") int size){
        try {
            PageModel<NftVO> pageModel = folderService.getFolderNfts(folderId, page, size);
            return Result.succeed(ErrorCode.OK, pageModel);
        }catch (Exception e){
            log.error("get folder nfts error.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @DeleteMapping("/folders/{id}/nfts")
    public Result<Integer> deleteFolderNfts(@PathVariable("id") Long folderId, @RequestBody NftsInputVO nftDeletionInputVO){
        try {
            int count = folderService.deleteFolderNfts(folderId, nftDeletionInputVO);
            return Result.succeed(ErrorCode.OK, count);
        }catch (Exception e){
            log.error("delete folder nfts error.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @PostMapping("/folders/{id}/nfts")
    public Result<Integer> addNftsToFolder(@PathVariable("id") Long folderId, @RequestBody NftsInputVO nftsInputVO){
        try {
            int count = folderService.addNftsToFolder(folderId, nftsInputVO);
            return Result.succeed(ErrorCode.OK, count);
        }catch (Exception e){
            log.error("delete folder nfts error.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }
}
