package com.nfinity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    DISABLE(1),
    ENABLE(2);

    private final int value;
}
