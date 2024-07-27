package com.kwon.myshop.dto;

import com.kwon.myshop.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderDto {

    private Long orderId;
    private Long itemId;
    private String itemName;
    private String size;
    private String color;
    private String brand;
    private int quantity;
    private String imageUrl;
    private LocalDateTime orderDate;
    private int totalPrice;
    private OrderStatus status;
}
