package com.nfinity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    DISABLE(0),
    ENABLE(1);

    private final int value;
}
