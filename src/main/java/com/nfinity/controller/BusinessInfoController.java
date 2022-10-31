package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.BusinessInfoService;
import com.nfinity.util.JwtUtil;
import com.nfinity.vo.BusinessInfoVO;
import com.nfinity.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/nft-business/v1")
@RequiredArgsConstructor
public class BusinessInfoController {
    private final BusinessInfoService businessInfoService;

    private final JwtUtil jwtUtil;

    @PutMapping("/business-info")
    public Result<Boolean> existsBusinessInfo(@RequestHeader("Authentication") String token){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        boolean flag = businessInfoService.existsBusinessInfo(userId);
        return Result.succeed(ErrorCode.OK, flag);
    }

    @PostMapping("/business-info")
    public Result<Long> createBusinessInfo(@RequestHeader("Authentication") String token, @Valid @RequestBody BusinessInfoVO vo){
        Long id = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        vo.setUserId(id);
        Long businessId = businessInfoService.createBusinessInfo(vo);
        return Result.succeed(ErrorCode.OK, businessId);
    }

    @GetMapping("/business-infos/{id}")
    public Result<BusinessInfoVO> getBusinessInfo(@RequestHeader("Authentication") String token, @PathVariable("id") Long businessId){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        BusinessInfoVO vo = businessInfoService.getBusinessInfo(userId, businessId);
        return Result.succeed(ErrorCode.OK, vo);
    }

    @PatchMapping("/business-infos/{id}")
    public Result<Long> editBusinessInfo(@RequestHeader("Authentication") String token, @PathVariable("id") Long businessId,
                                         @RequestBody BusinessInfoVO vo){
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));
        vo.setId(businessId);
        vo.setUserId(userId);
        businessId = businessInfoService.editBusinessInfo(vo);
        return Result.succeed(ErrorCode.OK, businessId);
    }

    @PatchMapping("/business-infos/{id}/logo")
    public Result<String> uploadLogo(@RequestHeader("Authentication") String token, @PathVariable("id") Long businessId,
                                    HttpServletRequest request) throws Exception {
        Long userId = Long.valueOf((Integer) jwtUtil.validateToken(token).get("id"));

        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> multipartFile = multipartHttpServletRequest.getFiles("file");
        String logo = businessInfoService.uploadLogo(multipartFile, userId, businessId);

        return Result.succeed(ErrorCode.OK, logo);
    }
}
