package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.NftService;
import com.nfinity.util.JwtUtil;
import com.nfinity.vo.NftVO;
import com.nfinity.vo.PageModel;
import com.nfinity.vo.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/nft-business/v1")
@RequiredArgsConstructor
public class NftController {
    private final NftService nftService;
    private final JwtUtil jwtUtil;

    @PostMapping("/nfts")
    public Result<PageModel<NftVO>> uploadNfts(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));

        //get files from http request
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> multipartFile = multipartHttpServletRequest.getFiles("files");
        log.info("[upload nfts] multipartFile size: " + multipartFile.size());

        return Result.succeed(ErrorCode.OK, nftService.uploadNftFiles(multipartFile, userId));
    }

    @PostMapping("/files")
    public Result<List<String>> uploadFiles(@RequestHeader("Authorization") String token, HttpServletRequest request) throws Exception {
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));

        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        int type = Integer.parseInt(multipartHttpServletRequest.getParameter("type"));
        List<MultipartFile> multipartFiles = multipartHttpServletRequest.getFiles("files");

        List<String> urls = nftService.uploadFiles(multipartFiles, userId, type);
        return Result.succeed(ErrorCode.OK, urls);
    }
}
