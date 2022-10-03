package com.nfinity.service;

import com.nfinity.vo.FolderInputVO;
import com.nfinity.vo.FolderOutputVO;
import com.nfinity.vo.PageModel;

public interface FolderService {
    Long createFolderWithNfts(FolderInputVO folderInputVO);

    PageModel<FolderOutputVO> getFolderList(int page, int size);
}
