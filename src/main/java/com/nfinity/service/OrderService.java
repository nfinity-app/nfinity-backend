package com.nfinity.service;

import com.nfinity.vo.CollectibleVO;
import com.nfinity.vo.OrderVO;
import com.nfinity.vo.PageModel;

public interface OrderService {
    Long createOrder(OrderVO vo);

    PageModel<CollectibleVO> getCollectibles(Long userId, int page, int size);
}
