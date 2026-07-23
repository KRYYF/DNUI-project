package com.neusoft.nep.common;

import lombok.Data;

import java.util.List;

/**
 * 分页结果
 */
@Data
public class PageResult<T> {

    private Long total;
    private Long current;
    private Long size;
    private List<T> records;

    public PageResult() {
    }

    public PageResult(Long total, Long current, Long size, List<T> records) {
        this.total = total;
        this.current = current;
        this.size = size;
        this.records = records;
    }
}
