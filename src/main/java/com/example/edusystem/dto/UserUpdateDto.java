package com.example.edusystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String fullName;

    @NotNull(message = "Phone number cannot be null")
    private String phoneNumber;

    private String password;
}
