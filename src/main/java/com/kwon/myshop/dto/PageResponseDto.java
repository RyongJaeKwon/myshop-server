package com.kwon.myshop.dto;

import com.kwon.myshop.domain.Item;
import com.kwon.myshop.domain.ItemImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class PageResponseDto<E> {

    private List<E> dtoList;
    private PageRequestDto pageRequestDto;
    private int totalPages, start, end, current;
    private boolean prev, next;
    private List<Integer> pageList;

    @Builder
    public PageResponseDto(List<E> dtoList, PageRequestDto pageRequestDto, int totalPages) {
        this.dtoList = dtoList;
        this.pageRequestDto = pageRequestDto;
        this.totalPages = totalPages;
        int tempEnd = (int)(Math.ceil(pageRequestDto.getPage() / 10.0)) * 10;
        this.end = totalPages > tempEnd ? tempEnd : totalPages;
        this.start = tempEnd - 9;
        this.prev = start > 1;
        this.next = totalPages > tempEnd;
        this.current = pageRequestDto.getPage();
        this.pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

    }
}