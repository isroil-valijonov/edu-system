package com.example.edusystem.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotEmpty(message = "Full Name cannot be empty!")
    private String fullName;

    @Pattern(regexp = "^(20|50|90|91|93|94|95|97|98|99)\\d{7}$")
    @Column(unique = true)
    private String phoneNumber;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", message = "Password must contain one digit, one uppercase letter, one lowercase letter, one special character, and be between 8 and 20 characters long.")
    private String password;
}
