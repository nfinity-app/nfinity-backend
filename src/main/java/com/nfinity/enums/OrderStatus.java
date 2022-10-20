package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    UNPAID(1),
    SUCCEED(2),
    FAILED(3),
    CANCELLED(4)
    ;

    private final int value;
}
