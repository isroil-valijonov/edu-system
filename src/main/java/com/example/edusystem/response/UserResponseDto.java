package com.example.edusystem.response;

import com.example.edusystem.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String password;
    private Role authentication;
    private String token;

    public UserResponseDto(Long id, String fullName, String phoneNumber, String password, Role authentication) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.authentication = authentication;
    }
}
