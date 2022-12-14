package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MintStatus {
    UNMINTED(1),
    MINTED(2);

    private final int value;
}
