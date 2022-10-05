package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    //0-Apparel, 1-Beauty, 2-Fashion, 3-Influencer
    APPAREL(1),
    BEAUTY(2),
    FASHION(3),
    INFLUENCER(4);

    private final int value;
}
