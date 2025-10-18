package com.cs203.core.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;

@Component
public class CustomBearerTokenResolver implements BearerTokenResolver {
    // Spring Sec by default uses Authentication Bearer token, which can't work with HttpOnly cookies.
    // So need to create this resolver to check for the accessToken set in the cookies returned by JS.
    // If your path requires cookies, then add it to the `requiresAuthentication` function
    private Logger logger = LoggerFactory.getLogger(CustomBearerTokenResolver.class);

    private final DefaultBearerTokenResolver defaultResolver = new DefaultBearerTokenResolver();

    @Override
    public String resolve(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        logger.info("Resolving token for {} {} ", method, path);

        if (!requiresAuthentication(path, method)) {
            return null;
        }

        String token = defaultResolver.resolve(request);

        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    logger.info("Resolved token {}", cookie.getValue());

                    return cookie.getValue();
                }
            }
        }

        return token;
    }

    private boolean requiresAuthentication(String path, String method) {
        if (path.startsWith("/api/v1/tariff-rate") || path.equals("/api/v1/auth/logout") || path.equals("/api/v1/auth/refresh")) {
            return true;
        }

        return false;
    }
}
