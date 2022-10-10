package com.nfinity.service;

import com.nfinity.vo.*;

public interface CollectionService {
    PageModel<CollectionOutputVO> getCollectionList(int page, int size);

    Long createCollection(CollectionInputVO vo);

    CollectionOutputVO getCollectionDetail(Long collectionId);

    int editCollectionDetail(Long collectionId, CollectionDetailVO vo);

    Long saveDraftCollection(DraftCollectionVO vo);

    DraftCollectionVO getDraftCollectionDetail(Long collectionId);

    String getGasFee(String chainType, Integer txType);
}
