package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    //0-Apparel, 1-Beauty, 2-Fashion, 3-Influencer
    APPAREL(0),
    BEAUTY(1),
    FASHION(2),
    INFLUENCER(3);

    private final int value;
}
