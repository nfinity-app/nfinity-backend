package com.nfinity.service;

import com.nfinity.vo.*;

public interface CollectionService {
    PageModel<CollectionOutputVO> getCollectionList(int page, int size);

    Long createCollection(CollectionInputVO vo);

    CollectionDetailOutputVO getCollectionDetail(Long collectionId);

    int editCollectionDetail(Long collectionId, CollectionDetailInputVO vo);

    Long saveDraftCollection(DraftCollectionVO vo);

    String getGasFee(String chainType, Integer txType);
}
