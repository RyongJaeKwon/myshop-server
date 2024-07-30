package com.kwon.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderCreateDto {

    private String userId;
    private List<OrderItemDto> orderItems;
    private String postcode;
    private String basicAddress;
    private String detailAddress;
    private String receiverName;
    private String receiverPhone;
    private String message;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderItemDto {
        private Long itemId;
        private int price;
        private int quantity;
    }
}
