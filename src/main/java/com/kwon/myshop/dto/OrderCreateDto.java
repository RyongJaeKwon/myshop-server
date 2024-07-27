package com.kwon.myshop.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderCreateDto {

    private String userId;
    private List<OrderItemDto> orderItems;
    private String postcode;
    private String basicAddress;
    private String detailAddress;
    private String receiverName;
    private String receiverPhone;
    private String message;
}
