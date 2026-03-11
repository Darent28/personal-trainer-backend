package com.pt.personal_trainer.dto;

import com.pt.personal_trainer.entity.User;
import lombok.Builder;

@Builder
public record UserResponseDto(
    Long id,
    String username,
    String email
) {

    public static UserResponseDto fromEntity(User user) {

        
        return UserResponseDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();

    }
}
