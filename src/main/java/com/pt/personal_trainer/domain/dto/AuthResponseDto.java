package com.pt.personal_trainer.domain.dto;

import java.time.Instant;

public record AuthResponseDto(
    String token,
    Instant expiresAt,
    UserResponseDto user
) {}
