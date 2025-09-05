package com.cs203.core.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("Should validate required fields")
    void shouldValidateRequiredFields() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        UserEntity user = new UserEntity();
        
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        
        // Should have violations for required fields
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Username is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password is required")));
    }

    @Test
    @DisplayName("Should validate username constraints")
    void shouldValidateUsernameConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedpassword");
        
        // Test username too short
        user.setUsername("abc");
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Username must be between 4 and 20 characters")));
        
        // Test username too long
        user.setUsername("a".repeat(21));
        violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Username must be between 4 and 20 characters")));
        
        // Test empty username
        user.setUsername("");
        violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Username is required")));
        
        // Test valid username
        user.setUsername("validuser");
        violations = validator.validate(user);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    @DisplayName("Should validate email constraints")
    void shouldValidateEmailConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPasswordHash("hashedpassword");
        
        // Test invalid email format
        user.setEmail("invalid-email");
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Invalid email format")));
        
        // Test empty email
        user.setEmail("");
        violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email is required")));
        
        // Test valid email
        user.setEmail("test@example.com");
        violations = validator.validate(user);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("Should validate password constraints")
    void shouldValidatePasswordConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        
        // Test empty password
        user.setPasswordHash("");
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password is required")));
        
        // Test null password
        user.setPasswordHash(null);
        violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password is required")));
        
        // Test valid password
        user.setPasswordHash("hashedpassword123");
        violations = validator.validate(user);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("passwordHash")));
    }

    @Test
    @DisplayName("Should validate name constraints")
    void shouldValidateNameConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        UserEntity user = new UserEntity("testuser", "test@example.com", "hashedpassword");
        
        // Test first name too long
        user.setFirstName("A".repeat(101));
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("First name cannot exceed 100 characters")));
        
        // Test last name too long
        user.setFirstName("John");
        user.setLastName("B".repeat(101));
        violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Last name cannot exceed 100 characters")));
        
        // Test valid names
        user.setFirstName("John");
        user.setLastName("Doe");
        violations = validator.validate(user);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("lastName")));
        
        // Test null names (should be valid)
        user.setFirstName(null);
        user.setLastName(null);
        violations = validator.validate(user);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    @DisplayName("Should set default values correctly")
    void shouldSetDefaultValuesCorrectly() {
        UserEntity user = new UserEntity();
        
        // Default isAdmin should be false
        assertFalse(user.getIsAdmin());
    }

    @Test
    @DisplayName("Should handle all getters and setters")
    void shouldHandleAllGettersAndSetters() {
        UserEntity user = new UserEntity();
        
        user.setId(100L);
        user.setUsername("adminuser");
        user.setEmail("admin@example.com");
        user.setPasswordHash("securehashedpassword");
        user.setIsAdmin(true);
        user.setFirstName("John");
        user.setLastName("Admin");
        
        assertEquals(100L, user.getId());
        assertEquals("adminuser", user.getUsername());
        assertEquals("admin@example.com", user.getEmail());
        assertEquals("securehashedpassword", user.getPasswordHash());
        assertTrue(user.getIsAdmin());
        assertEquals("John", user.getFirstName());
        assertEquals("Admin", user.getLastName());
    }

    @Test
    @DisplayName("Should create user with minimal required fields")
    void shouldCreateUserWithMinimalRequiredFields() {
        UserEntity user = new UserEntity("minuser", "min@example.com", "minpassword");
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);
        
        assertTrue(violations.isEmpty());
        assertEquals("minuser", user.getUsername());
        assertEquals("min@example.com", user.getEmail());
        assertEquals("minpassword", user.getPasswordHash());
        assertFalse(user.getIsAdmin());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
    }

    @Test
    @DisplayName("Should handle empty constructor")
    void shouldHandleEmptyConstructor() {
        UserEntity user = new UserEntity();
        
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPasswordHash());
        assertFalse(user.getIsAdmin()); // Default value
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
    }

    @Test
    @DisplayName("Should handle admin user creation")
    void shouldHandleAdminUserCreation() {
        UserEntity user = new UserEntity("adminuser", "admin@example.com", "adminpassword");
        user.setIsAdmin(true);
        user.setFirstName("Admin");
        user.setLastName("User");
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(user);
        
        assertTrue(violations.isEmpty());
        assertTrue(user.getIsAdmin());
        assertEquals("Admin", user.getFirstName());
        assertEquals("User", user.getLastName());
    }
}
