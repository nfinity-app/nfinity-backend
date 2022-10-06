package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContractStatus {
    INIT(1),
    PENDING(2),
    PUBLISHED(3),
    FAILED(4);

    private final int value;
}
