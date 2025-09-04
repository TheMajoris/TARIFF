package com.cs203.core.dto.requests;

import jakarta.validation.constraints.*;

public record LoginRequestDTO (
    @NotNull(message = "Empty cannot be null")
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    String email,

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    String password
){}
