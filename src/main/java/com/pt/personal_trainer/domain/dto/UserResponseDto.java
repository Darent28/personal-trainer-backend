package com.pt.personal_trainer.domain.dto;

import com.pt.personal_trainer.entity.User;

public record UserResponseDto(
    Long id,
    String username,
    String email,
    Integer genderId,
    Boolean emailVerified
) {

    public static UserResponseDto fromEntity(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getGenderId(),
            user.getEmailVerified()
        );
    }
}
