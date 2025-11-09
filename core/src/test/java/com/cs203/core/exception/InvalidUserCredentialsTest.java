package com.cs203.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class InvalidUserCredentialsTest {
    @Test
    void constructor_ShouldSetMessage() {
        // Arrange
        String message = "Invalid username or password";

        // Act
        InvalidUserCredentials exception = new InvalidUserCredentials(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }
}
