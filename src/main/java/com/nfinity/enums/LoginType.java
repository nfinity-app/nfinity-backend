package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {
    REGISTER(1),
    LOGIN(2),
    RESET_PASSWORD(3),
    CHANGE_PASSWORD(4),
    ;

    private final int value;
}
