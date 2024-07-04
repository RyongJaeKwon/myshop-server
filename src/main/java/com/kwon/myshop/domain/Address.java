package com.kwon.myshop.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String postcode;            // 우편번호
    private String basic_address;       // 기본 주소
    private String detail_address;      // 상세 주소
}
