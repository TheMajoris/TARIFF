package com.cs203.core.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    TRADER("ROLE_TRADER"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }
}
