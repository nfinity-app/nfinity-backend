package com.nfinity.service;

import com.nfinity.vo.NftVO;
import com.nfinity.vo.PageModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NftService {
    PageModel<NftVO> uploadNftFiles(List<MultipartFile> multipartFileList, Long userId) throws Exception;

    List<String> uploadFiles(List<MultipartFile> multipartFiles, Long userId, int type) throws Exception;
}
