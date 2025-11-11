package com.cs203.core.enums;

import lombok.Getter;

@Getter
public enum ApiPath {
    // Tariff Rate
    TARIFF_RATE("/api/v1/tariff-rate/**"),
    TARIFF_RATE_CALCULATION("/api/v1/tariff-rate/calculation"),

    // Product Categories
    PRODUCT_CATEGORIES("/api/v1/product-categories/**"),

    // Calculation History
    CALCULATION_HISTORY("/api/v1/calculation-history/**"),

    // Route
    ROUTE("/api/v1/route/**"),

    // Auth
    AUTH_LOGOUT("/api/v1/auth/logout"),
    AUTH_REFRESH("/api/v1/auth/refresh");

    private final String path;

    ApiPath(String path) {
        this.path = path;
    }

    public static String[] pathsOf(ApiPath... apiPaths) {
        return java.util.Arrays.stream(apiPaths)
                .map(ApiPath::getPath)
                .toArray(String[]::new);
    }
}
