package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum EmailType {
    REGISTER(1, "register"),
    RESET_PASSWORD(2, "reset"),
    BUSINESS(3, "business"),
    ;

    private final int key;
    private final String value;

    public static final Map<Integer, String> EMAIL_TYPE_MAP = new HashMap<>();

    static {
        for(EmailType type : values ()) {
            EMAIL_TYPE_MAP.put(type.key, type.value);
        }
    }
}
