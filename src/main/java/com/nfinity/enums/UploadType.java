package com.nfinity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UploadType {
    PROFILE_PHOTO(1),
    BUSINESS_LOGO(2),
    NFT(3),
    ;

    private final int value;
}
