package com.example.edusystem.controller;

import com.example.edusystem.dto.UserUpdateDto;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.UserResponseDto;
import com.example.edusystem.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    @PutMapping("/update-admin/{id}")
    public ResponseEntity<UserResponseDto> updateAdmin(@Valid @PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) throws CustomException, CustomNotFoundException {
        UserResponseDto userResponseDto = adminService.updateAdmin(id, userUpdateDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/get-admin/{id}")
    public ResponseEntity<UserResponseDto> getAdmin(@Valid @PathVariable Long id) throws CustomException, CustomNotFoundException {
        UserResponseDto admin = adminService.getAdmin(id);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/get-all-admin")
    public ResponseEntity<List<UserResponseDto>> getAllAdmin() {
        List<UserResponseDto> allAdmin = adminService.getAllAdmin();
        return ResponseEntity.ok(allAdmin);
    }

    @DeleteMapping("/delete-admin/{id}")
    public ResponseEntity<CommonResponse> deleteAdmin(@Valid @PathVariable Long id) throws CustomNotFoundException {
        CommonResponse commonResponse = adminService.deleteAdmin(id);
        return ResponseEntity.ok(commonResponse);
    }
}
