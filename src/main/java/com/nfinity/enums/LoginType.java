package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {
    REGISTER(1),
    LOGIN(2),
    ;

    private final int value;
}
