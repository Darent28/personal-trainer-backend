package com.pt.personal_trainer.domain.input;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInput {

    @NotNull(message = "Username is required")
    @NotBlank(message = "Username is required")
    private String username;

    @NotNull(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Gender is required")
    private Integer genderId;

    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be a past date")
    private LocalDate birthday;
}
