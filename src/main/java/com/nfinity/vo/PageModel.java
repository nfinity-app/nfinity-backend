package com.nfinity.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageModel<T> {
    private long total;
    private List<T> records;
}
