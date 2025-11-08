package com.cs203.core.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationExceptions_ShouldReturnBadRequest() {
        // Arrange
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Data may be invalid. Please check your inputs.", response.getBody().get("message"));
    }

    @Test
    void handleJsonParseExceptions_ShouldReturnBadRequest() {
        // Arrange
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleJsonParseExceptions(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Data may be invalid. Please check your inputs.", response.getBody().get("message"));
    }

    @Test
    void handleDuplicateEmailException_ShouldReturnBadRequestWithCustomMessage() {
        // Arrange
        String errorMessage = "Email already exists";
        DuplicateEmailException ex = new DuplicateEmailException(errorMessage);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleDuplicateEmailException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals(errorMessage, response.getBody().get("message"));
    }

    @Test
    void handleInvalidUserCredentials_ShouldReturnUnauthorized() {
        // Arrange
        String errorMessage = "Invalid credentials";
        InvalidUserCredentials ex = new InvalidUserCredentials(errorMessage);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleInvalidUserCredentials(ex);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().get("status"));
        assertEquals(errorMessage, response.getBody().get("message"));
    }

    @Test
    void handleBadCredentials_ShouldReturnUnauthorized() {
        // Arrange
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleBadCredentials(ex);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().get("status"));
        assertEquals("Invalid email or password", response.getBody().get("message"));
    }

    @Test
    void handleInvalidTokenException_ShouldReturnUnauthorized() {
        // Arrange
        String errorMessage = "Token is invalid or expired";
        InvalidTokenException ex = new InvalidTokenException(errorMessage);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleInvalidTokenException(ex);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().get("status"));
        assertEquals(errorMessage, response.getBody().get("message"));
    }

    @Test
    void handleTypeMismatchException_WithRequiredType_ShouldReturnBadRequest() {
        // Arrange
        TypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "abc", Long.class, "id", null, null);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleTypeMismatchException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertTrue(response.getBody().get("message").toString().contains("abc"));
        assertTrue(response.getBody().get("message").toString().contains("Long"));
    }

    @Test
    void handleTypeMismatchException_WithNullRequiredType_ShouldReturnBadRequest() {
        // Arrange
        TypeMismatchException ex = mock(TypeMismatchException.class);
        when(ex.getValue()).thenReturn("invalid");
        when(ex.getRequiredType()).thenReturn(null);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleTypeMismatchException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertTrue(response.getBody().get("message").toString().contains("invalid"));
        assertTrue(response.getBody().get("message").toString().contains("required type"));
    }

    @Test
    void handleDataIntegrityViolationException_ShouldReturnBadRequest() {
        // Arrange
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Constraint violation");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleDataIntegrityViolationException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Something went wrong. Please contact an administrator", response.getBody().get("message"));
    }

    @Test
    void handlePropertyReferenceException_ShouldReturnBadRequest() {
        // Arrange
        PropertyReferenceException ex = mock(PropertyReferenceException.class);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handlePropertyReferenceException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Invalid property referenced.", response.getBody().get("message"));
    }

    @Test
    void handleMethodNotSupported_ShouldReturnMethodNotAllowed() {
        // Arrange
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");
        when(request.getRequestURI()).thenReturn("/api/users");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleMethodNotSupported(ex, request);

        // Assert
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(405, response.getBody().get("status"));
        assertEquals("Method not allowed for this endpoint.", response.getBody().get("message"));
        assertEquals("/api/users", response.getBody().get("path"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void handleNotFound_WithNoHandlerFoundException_ShouldReturnNotFound() {
        // Arrange
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/api/unknown", null);
        when(request.getRequestURI()).thenReturn("/api/unknown");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleNotFound(ex, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Endpoint not found.", response.getBody().get("message"));
        assertEquals("/api/unknown", response.getBody().get("path"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void handleNotFound_WithNoResourceFoundException_ShouldReturnNotFound() {
        // Arrange
        NoResourceFoundException ex = mock(NoResourceFoundException.class);
        when(request.getRequestURI()).thenReturn("/static/missing.js");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleNotFound(ex, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Endpoint not found.", response.getBody().get("message"));
        assertEquals("/static/missing.js", response.getBody().get("path"));
        assertNotNull(response.getBody().get("timestamp"));
    }
}
