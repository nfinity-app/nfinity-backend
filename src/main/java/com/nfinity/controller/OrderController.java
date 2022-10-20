package com.nfinity.controller;

import com.nfinity.enums.ErrorCode;
import com.nfinity.service.OrderService;
import com.nfinity.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nft-business/v1")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/pre-order")
    public Result<Long> createPreOrder(@RequestBody PreOrderVO vo){
        Long orderId = orderService.createPreOrder(vo);
        return Result.succeed(ErrorCode.OK, orderId);
    }

    @PostMapping("/order")
    public Result<Long> createOrder(@RequestBody OrderVO vo){
        Long orderId = orderService.updateOrder(vo);
        return Result.succeed(ErrorCode.OK, orderId);
    }

    @GetMapping("/users/{id}/collectibles")
    public Result<PageModel<CollectibleVO>> getCollectibles(@PathVariable("id") Long userId,
                                                            @RequestParam(required = false, defaultValue = "1") int page,
                                                            @RequestParam(required = false, defaultValue = "4") int size){
        PageModel<CollectibleVO> pageModel = orderService.getCollectibles(userId, page, size);
        return Result.succeed(ErrorCode.OK, pageModel);
    }
}
