package com.kwon.myshop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryStatus {

    DEPOSIT_READY("DEPOSIT_READY", "입금대기"),
    DELIVERY_READY("DELIVERY_READY", "배송준비중"),
    DELIVERY_IN_PROGRESS("DELIVERY_IN_PROGRESS", "배송중"),
    DELIVERY_COMP("DELIVERY_COMP", "배송완료"),
    DEFAULT("DEFAULT", "-");

    private final String title;
    private final String value;

}