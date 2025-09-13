package com.cs203.core.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserEntity Tests")
class UserEntityTest {

    @Test
    @DisplayName("Should create valid UserEntity")
    void shouldCreateValidUserEntity() {
        UserEntity user = new UserEntity(
            "testuser", 
            "test@example.com", 
            "hashedpassword123"
        );

        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hashedpassword123", user.getPasswordHash());
        assertFalse(user.getIsAdmin()); // Default value
    }

    @Test
    @DisplayName("Should allow setting admin status")
    void shouldAllowSettingAdminStatus() {
        UserEntity user = new UserEntity("adminuser", "admin@example.com", "securehashedpassword");
        user.setIsAdmin(true);
        assertTrue(user.getIsAdmin());
    }
}
