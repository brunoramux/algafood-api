package com.algafoods.api.model.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageModel {

    private int size;
    private long totalElements;
    private int totalPages;
    private int page;
    private boolean first;
    private boolean last;

    public PageModel(int size, long totalElements, int totalPages, int page) {
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.first = page == 0;
        this.last = page + 1 == totalPages;
    }

}