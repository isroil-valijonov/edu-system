package com.example.edusystem.controller;

import com.example.edusystem.dto.UserUpdateDto;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.UserResponseDto;
import com.example.edusystem.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;
    @PutMapping("/update-teacher/{id}")
    public ResponseEntity<UserResponseDto> updateTeacher(@PathVariable Long id, @RequestBody @Valid UserUpdateDto userUpdateDto) throws CustomException, CustomNotFoundException {
        UserResponseDto userResponseDto = teacherService.updateTeacher(id, userUpdateDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/get-teacher/{id}")
    public ResponseEntity<UserResponseDto> getTeacher(@PathVariable @Valid Long id) throws CustomNotFoundException {
        UserResponseDto teacher = teacherService.getTeacher(id);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/get-all-teacher")
    public ResponseEntity<List<UserResponseDto>> getAllTeacher() {
        List<UserResponseDto> allTeacher = teacherService.getAllTeacher();
        return ResponseEntity.ok(allTeacher);
    }

    @DeleteMapping("/delete-teacher/{id}")
    public ResponseEntity<CommonResponse> deleteTeacher(@PathVariable @Valid Long id) throws CustomNotFoundException {
        CommonResponse commonResponse = teacherService.deleteTeacher(id);
        return ResponseEntity.ok(commonResponse);
    }
}
