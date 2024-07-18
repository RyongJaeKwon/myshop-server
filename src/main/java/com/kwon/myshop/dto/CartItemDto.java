package com.kwon.myshop.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemDto {

    private String userId;
    private Long cartItemId;
    private Long itemId;
    private int quantity;
}
