package com.nfinity.service;

import com.nfinity.vo.FolderCreationInputVO;
import com.nfinity.vo.FolderDeletionInputVO;
import com.nfinity.vo.FolderVO;
import com.nfinity.vo.PageModel;

public interface FolderService {
    Long createFolderWithNfts(FolderCreationInputVO folderInputVO);

    PageModel<FolderVO> getFolderList(int page, int size);

    int deleteFolders(FolderDeletionInputVO folderDeletionInputVO);
}
