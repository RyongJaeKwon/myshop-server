package com.kwon.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class OrderItemDto {

    private Long itemId;
    private String itemName;
    private String color;
    private String size;
    private int price;
    private int quantity;
    private String imageUrl;
}
