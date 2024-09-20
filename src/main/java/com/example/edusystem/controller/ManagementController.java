package com.example.edusystem.controller;

import com.example.edusystem.dto.UserUpdateDto;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.UserResponseDto;
import com.example.edusystem.service.ManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class ManagementController {
    private final ManagementService managementService;

    @PutMapping("/update-management/{id}")
    public ResponseEntity<UserResponseDto> updateManagement(@PathVariable Long id, @RequestBody @Valid UserUpdateDto userUpdateDto) throws CustomException, CustomNotFoundException {
        UserResponseDto userResponseDto = managementService.updateManagement(id, userUpdateDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/get-management/{id}")
    public ResponseEntity<UserResponseDto> getManagement(@PathVariable @Valid Long id) throws CustomNotFoundException {
        UserResponseDto management = managementService.getManagement(id);
        return ResponseEntity.ok(management);
    }

    @DeleteMapping("/delete-management/{id}")
    public ResponseEntity<CommonResponse> deleteManagement(@PathVariable @Valid Long id) throws CustomNotFoundException {
        CommonResponse commonResponse = managementService.deleteManagement(id);
        return ResponseEntity.ok(commonResponse);
    }

}
