package com.kwon.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class ReplyDto {

    private Long id;
    private Long memberId;
    private Long itemId;
    private String userId;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
