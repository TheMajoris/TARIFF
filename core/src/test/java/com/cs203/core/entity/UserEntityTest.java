package com.cs203.core.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserEntity Tests")
class UserEntityTest {

    @Test
    @DisplayName("Should create valid UserEntity")
    void shouldCreateValidUserEntity() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedpassword123");
        user.setIsAdmin(false);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEnabled(true);

        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hashedpassword123", user.getPasswordHash());
        assertFalse(user.getIsAdmin()); // Default value
    }

    @Test
    @DisplayName("Should allow setting admin status")
    void shouldAllowSettingAdminStatus() {
        UserEntity user = new UserEntity();
        user.setUsername("adminuser");
        user.setEmail("admin@example.com");
        user.setPasswordHash("securehashedpassword");
        user.setIsAdmin(false);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEnabled(true);
        user.setIsAdmin(true);

        assertTrue(user.getIsAdmin());
    }
}
