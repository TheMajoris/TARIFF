package com.cs203.core.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertTrue(user.isEnabled());
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

    @Test
    @DisplayName("Should test all getters for completeness")
    void shouldTestAllGetters() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("getteruser");
        user.setEmail("getter@example.com");
        user.setPasswordHash("hash123");
        user.setFirstName("Getter");
        user.setLastName("Tester");
        user.setEnabled(false);
        user.setIsAdmin(false);

        assertEquals(1L, user.getId());
        assertEquals("getteruser", user.getUsername());
        assertEquals("getter@example.com", user.getEmail());
        assertEquals("hash123", user.getPasswordHash());
        assertEquals("Getter", user.getFirstName());
        assertEquals("Tester", user.getLastName());
        assertFalse(user.isEnabled());
        assertFalse(user.getIsAdmin());
    }

    @Test
    @DisplayName("Should set and get createdAt")
    void testCreatedAtSetterGetter() {
        UserEntity user = new UserEntity();
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    @DisplayName("Should set enabled to true on PrePersist")
    void testPrePersistSetUp() {
        UserEntity user = new UserEntity();
        user.setEnabled(false); // initially disabled
        user.setUp(); // simulate JPA @PrePersist callback
        assertTrue(user.isEnabled());
    }

}
