package com.cs203.core.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    NOT_ADMIN("ROLE_NOT_ADMIN"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getScopeAuthority() {
        return "SCOPE_" + authority;
    }
}
