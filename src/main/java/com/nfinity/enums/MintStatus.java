package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MintStatus {
    DRAFTED(0),
    PENDING(1),
    PUBLISHED(2),
    SUSPENDED(3);

    private final int value;
}
