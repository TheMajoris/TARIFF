package com.cs203.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InvalidTokenExceptionTest {
    @Test
    void exception_WithDifferentMessages_ShouldWork() {
        // Test with various message scenarios
        String message1 = "Token signature invalid";
        String message2 = "Token malformed";

        InvalidTokenException exception1 = new InvalidTokenException(message1);
        InvalidTokenException exception2 = new InvalidTokenException(message2);

        assertEquals(message1, exception1.getMessage());
        assertEquals(message2, exception2.getMessage());
        assertNotEquals(exception1.getMessage(), exception2.getMessage());
    }
}
