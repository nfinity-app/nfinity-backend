package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MintStatus {
    INIT(1),
    DEPLOYED(2),
    MINTING(3),
    MINTED(4),
    ;

    private final int value;
}
