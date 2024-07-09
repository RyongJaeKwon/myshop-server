package com.kwon.myshop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@Builder
public class PageRequestDto {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 6;
}
