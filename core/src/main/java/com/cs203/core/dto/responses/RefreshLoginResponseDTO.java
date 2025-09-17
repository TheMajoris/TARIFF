package com.cs203.core.dto.responses;

public record RefreshLoginResponseDTO(String userId, String fullName, String role, String jwt) {}
