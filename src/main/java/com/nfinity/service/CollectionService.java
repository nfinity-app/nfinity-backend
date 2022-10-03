package com.nfinity.service;

import com.nfinity.vo.CollectionDetailVO;
import com.nfinity.vo.CollectionInputVO;
import com.nfinity.vo.CollectionOutputVO;
import com.nfinity.vo.PageModel;

public interface CollectionService {
    PageModel<CollectionOutputVO> getCollectionList(int page, int size);

    Long createCollection(CollectionInputVO vo);

    CollectionOutputVO getCollectionDetail(Long collectionId);

    int editCollectionDetail(Long collectionId, CollectionDetailVO vo);
}
