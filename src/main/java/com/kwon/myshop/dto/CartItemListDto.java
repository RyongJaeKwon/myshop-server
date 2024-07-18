package com.kwon.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class CartItemListDto {

    private Long cartItemId;
    private Long itemId;
    private String itemName;
    private int price;
    private int quantity;
    private String imageUrl;
}