package com.nfinity.service;

import com.nfinity.vo.CollectionInputVO;
import com.nfinity.vo.CollectionOutputVO;
import com.nfinity.vo.PageModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CollectionService {
    PageModel<CollectionOutputVO> getCollectionList(int page, int size);

    void uploadNftFiles(List<MultipartFile> multipartFileList) throws IOException;

    int createCollection(CollectionInputVO vo);
}
