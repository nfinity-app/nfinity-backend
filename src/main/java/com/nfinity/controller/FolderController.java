package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.FolderService;
import com.nfinity.vo.FolderInputVO;
import com.nfinity.vo.FolderOutputVO;
import com.nfinity.vo.PageModel;
import com.nfinity.vo.Result;
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
    public Result<Long> createFolderWithNfts(@RequestBody FolderInputVO folderInputVO){
        try {
            Long folderId = folderService.createFolderWithNfts(folderInputVO);
            return Result.succeed(ErrorCode.OK, folderId);
        }catch (Exception e){
            log.error("create folder with nfts failed.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }

    @GetMapping("/folders")
    public Result<PageModel<FolderOutputVO>> getFolderList(@RequestParam(required = false, defaultValue = "1") int page,
                                                           @RequestParam(required = false, defaultValue = "6") int size){
        try {
            PageModel<FolderOutputVO> folderVOPageModel = folderService.getFolderList(page, size);
            return Result.succeed(ErrorCode.OK, folderVOPageModel);
        }catch (Exception e){
            log.error("get folder list error.", e);
            return Result.fail(ErrorCode.ERROR);
        }
    }
}
