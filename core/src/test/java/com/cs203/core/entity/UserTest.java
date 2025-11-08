package com.cs203.core.entity;

import com.cs203.core.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User (UserDetails) Tests")
class UserTest {

    @Test
    @DisplayName("Should return correct authorities, username, password and enabled status")
    void testUserDetailsMethods() {
        // Setup UserEntity with ADMIN role
        UserEntity userEntity = new UserEntity();
        userEntity.setId(42L);
        userEntity.setPasswordHash("securePassword");
        userEntity.getUserRole();
        userEntity.setIsAdmin(true);
        userEntity.setEnabled(true);

        User user = new User(userEntity);

        // getAuthorities
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());

        // getUsername
        assertEquals("42", user.getUsername());

        // getPassword
        assertEquals("securePassword", user.getPassword());

        // isEnabled
        assertTrue(user.isEnabled());
    }

    @Test
    @DisplayName("Should handle NOT_ADMIN role")
    void testAuthoritiesWithNotAdminRole() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(100L);
        userEntity.setPasswordHash("pass123");
        userEntity.setIsAdmin(Boolean.FALSE);
        userEntity.setEnabled(false);

        User user = new User(userEntity);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_NOT_ADMIN", authorities.iterator().next().getAuthority());

        assertEquals("100", user.getUsername());
        assertEquals("pass123", user.getPassword());
        assertFalse(user.isEnabled());
    }
}
