package com.kwon.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderCreateDto {

    private String userId;
    private Long itemId;
    private int price;
    private int quantity;
    private String postcode;
    private String basicAddress;
    private String detailAddress;
    private String receiverName;
    private String receiverPhone;
    private String message;

}
