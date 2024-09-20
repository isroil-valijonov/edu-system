package com.example.edusystem.controller;

import com.example.edusystem.service.CustomUserDetailsService;
import com.example.edusystem.dto.SignInRequest;
import com.example.edusystem.dto.SignUpRequest;
import com.example.edusystem.response.UserSignInResponse;
import com.example.edusystem.response.UserSignUpResponse;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.service.AdminService;
import com.example.edusystem.service.StudentService;
import com.example.edusystem.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth")
public class AuthController {

    private final AdminService adminService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final CustomUserDetailsService userDetailsService;

    @PreAuthorize("hasRole('MANAGEMENT')")
    @PostMapping("/create-admin")
    public ResponseEntity<UserSignUpResponse> createAdmin(@RequestBody @Valid SignUpRequest signUp) throws CustomException {
        UserSignUpResponse admin = adminService.createAdmin(signUp);
        return ResponseEntity.ok(admin);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-teacher")
    public ResponseEntity<UserSignUpResponse> createTeacher(@RequestBody @Valid SignUpRequest signUp) throws CustomException {
        UserSignUpResponse teacher = teacherService.createTeacher(signUp);
        return ResponseEntity.ok(teacher);
    }

    @PostMapping("/create-student")
    public ResponseEntity<UserSignUpResponse> createStudent(@RequestBody @Valid SignUpRequest signUp) throws CustomException {
        UserSignUpResponse student = studentService.createStudent(signUp);
        return ResponseEntity.ok(student);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserSignInResponse> signIn(@RequestBody @Valid SignInRequest sign) {
        UserSignInResponse userSignInResponse = userDetailsService.signIn(sign);
        return ResponseEntity.ok(userSignInResponse);
    }


}
