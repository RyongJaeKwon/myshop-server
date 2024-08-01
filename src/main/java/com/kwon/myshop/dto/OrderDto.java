package com.kwon.myshop.dto;

import com.kwon.myshop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderDto {

    private Long orderId;
    private LocalDateTime orderDate;
    private int totalPrice;
    private OrderStatus orderStatus;
    private String deliveryStatus;
    private List<OrderItemDto> items;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderItemDto {
        private Long itemId;
        private String itemName;
    }
}
