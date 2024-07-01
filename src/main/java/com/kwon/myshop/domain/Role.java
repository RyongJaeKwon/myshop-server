package com.kwon.myshop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ADMIN", "ROLE_ADMIN"),
    MEMBER("MEMBER", "ROLE_MEMBER");

    private final String title;
    private final String value;
}
