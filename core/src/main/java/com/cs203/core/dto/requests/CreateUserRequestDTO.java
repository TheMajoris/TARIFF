package com.cs203.core.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequestDTO (
    @NotNull(message = "First name cannot be null")
    @NotEmpty(message = "First name canot be empty")
    String firstName,

    @NotNull(message = "Last name cannot be null")
    @NotEmpty(message = "Last name cannot be empty")
    String lastName,

    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    String email,

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    String password,

    @NotNull(message = "IsAdmin cannot be null")
    @NotEmpty(message = "IsAdmin cannot be empty")
    Boolean isAdmin
) {}
