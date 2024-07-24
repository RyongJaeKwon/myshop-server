package com.kwon.myshop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class ReplyDto {

    private Long id;
    private Long memberId;
    private Long itemId;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
