package com.nfinity.service;

import com.nfinity.vo.CollectibleVO;
import com.nfinity.vo.OrderVO;
import com.nfinity.vo.PageModel;
import com.nfinity.vo.PreOrderVO;

public interface OrderService {
    Long updateOrder(OrderVO vo);

    PageModel<CollectibleVO> getCollectibles(Long userId, int page, int size);

    Long createPreOrder(PreOrderVO vo);
}
