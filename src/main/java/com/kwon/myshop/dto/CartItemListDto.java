package com.kwon.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartItemDetailsDto {

    private Long cartItemId;
    private Long itemId;
    private String itemName;
    private int price;
    private int quantity;
    private String imageUrl;
}