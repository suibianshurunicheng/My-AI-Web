package com.employee.management.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    
    private long total;
    private int page;
    private int size;
    private List<T> list;
    
    public PageResponse() {
    }
    
    public PageResponse(long total, int page, int size, List<T> list) {
        this.total = total;
        this.page = page;
        this.size = size;
        this.list = list;
    }
}