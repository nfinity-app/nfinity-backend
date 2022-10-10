package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DisplayStatus {
    DRAFTED(1),
    PENDING(2),
    PUBLISHED(3),
    SUSPENDED(4);

    private final int value;
}
