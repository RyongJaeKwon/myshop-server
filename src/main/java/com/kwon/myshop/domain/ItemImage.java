package com.kwon.myshop.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage {

    private String fileName;
    private int ord;            // 대표 이미지만 출력할때 사용

    public void setOrd(int ord) {
        this.ord = ord;
    }
}
