package com.kwon.myshop.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage {

    private String fileName;
    private int ord;

    public void setOrd(int ord) {
        this.ord = ord;
    }
}
