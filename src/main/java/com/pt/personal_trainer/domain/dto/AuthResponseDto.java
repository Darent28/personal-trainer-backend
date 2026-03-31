package com.pt.personal_trainer.domain.dto;

import java.time.Instant;

import lombok.Builder;

@Builder
public record AuthResponseDto(
    String token,
    Instant expiresAt,
    UserResponseDto user
) {}
