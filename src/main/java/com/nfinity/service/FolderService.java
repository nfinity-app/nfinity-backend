package com.nfinity.service;

import com.nfinity.vo.*;

public interface FolderService {
    Long createFolderWithNfts(FolderCreationInputVO folderInputVO);

    PageModel<FolderVO> getFolderList(int page, int size);

    int deleteFolders(FolderDeletionInputVO folderDeletionInputVO);

    PageModel<NftVO> getFolderNfts(Long folderId, int page, int size);
}
