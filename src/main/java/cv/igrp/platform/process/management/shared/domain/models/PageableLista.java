package cv.igrp.platform.process.management.shared.domain.models;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PageableLista<T> {

    private Integer pageNumber;


    private Integer pageSize;


    private Long totalElements;


    private Integer totalPages;


    private boolean last;


    private boolean first;


    private List<T> content;

    @Builder
    public PageableLista(Integer pageNumber, Integer pageSize, Long totalElements,
                         Integer totalPages, boolean last, boolean first, List<T> content) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
        this.first = first;
        this.content = content;
    }

}
