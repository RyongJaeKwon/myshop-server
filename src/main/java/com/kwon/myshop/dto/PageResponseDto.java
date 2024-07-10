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
    private int totalPages, start, end, current, prevPage, nextPage;
    private boolean prev, next;
    private List<Integer> pageList;

    @Builder
    public PageResponseDto(List<E> dtoList, PageRequestDto pageRequestDto, int totalPages) {
        this.dtoList = dtoList;
        this.current = pageRequestDto.getPage();
        this.pageRequestDto = pageRequestDto;
        this.totalPages = totalPages;
        int tempEnd = (int)(Math.ceil(pageRequestDto.getPage() / 10.0)) * 10;
        this.end = totalPages > tempEnd ? tempEnd : totalPages;
        this.start = tempEnd - 9;
        this.prev = current > 1;
        this.next = current < totalPages;
        this.pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        this.prevPage = getPrevPage();
        this.nextPage = getNextPage();

        System.out.println("Current: " + current);
        System.out.println("Prev: " + prev);
        System.out.println("Next: " + next);
    }

    public int getPrevPage() {
        return current > 1 ? current - 1 : 1;
    }

    public int getNextPage() {
        return current < totalPages ? current + 1 : totalPages;
    }
}