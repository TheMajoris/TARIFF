package com.cs203.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class DuplicateEmailExceptionTest {
    @Test
    void constructor_ShouldSetMessage() {
        // Arrange
        String message = "Email address already exists in the system";

        // Act
        DuplicateEmailException exception = new DuplicateEmailException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }
}
