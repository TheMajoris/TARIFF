package com.cs203.core.dto.responses;

import java.time.ZonedDateTime;

public record GenericResponseDTO(boolean success, Object message, ZonedDateTime timestamp) {}
