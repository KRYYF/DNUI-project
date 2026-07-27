package com.neusoft.nep.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {
    private Long total;
    private Integer current;
    private Integer size;
    private List<T> records;

    public PageVO(Long total, Integer current, Integer size, List<T> records) {
        this.total = total;
        this.current = current;
        this.size = size;
        this.records = records;
    }
}
