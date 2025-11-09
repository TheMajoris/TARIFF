package com.cs203.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class JwtCreationExceptionTest {
    @Test
    void constructor_ShouldSetMessageAndCause() {
        // Arrange
        String message = "Failed to create JWT token";
        Throwable cause = new IllegalArgumentException("Invalid secret key");

        // Act
        JwtCreationException exception = new JwtCreationException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertInstanceOf(RuntimeException.class, exception);
    }
}
