package com.example.edusystem.response;

import com.example.edusystem.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpResponse {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String password;
    private Role authenticates;
    private int status;
}
