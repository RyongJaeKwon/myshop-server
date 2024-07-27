package com.kwon.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderItemDto {

    private Long itemId;
    private String itemName;
    private String color;
    private String size;
    private int price;
    private int quantity;
    private String imageUrl;
}
