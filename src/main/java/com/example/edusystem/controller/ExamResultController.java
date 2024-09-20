package com.example.edusystem.controller;


import com.example.edusystem.dto.ExamResultRequestDto;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.ExamResultResponseDto;
import com.example.edusystem.service.StudentExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam-results")
@RequiredArgsConstructor
public class ExamResultController {

    private final StudentExamService studentExamService;

    @PostMapping("/create-result")
    public ResponseEntity<ExamResultResponseDto> createExamResult(@RequestBody @Valid ExamResultRequestDto examResultRequestDto) throws CustomNotFoundException, BadRequestException {
        ExamResultResponseDto createdResult = studentExamService.createResult(examResultRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdResult);
    }

    @PutMapping("/update-result/{id}")
    public ResponseEntity<ExamResultResponseDto> updateExamResult(@PathVariable Long id, @RequestBody @Valid ExamResultRequestDto examResultRequestDto) throws CustomNotFoundException, BadRequestException {
        ExamResultResponseDto updatedResult = studentExamService.updateResult(id, examResultRequestDto);
        return ResponseEntity.ok(updatedResult);
    }

    @DeleteMapping("/delete-result/{id}")
    public ResponseEntity<CommonResponse> deleteExamResult(@PathVariable @Valid Long id) throws CustomNotFoundException {
        CommonResponse response = studentExamService.deleteResult(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-result/{id}")
    public ResponseEntity<ExamResultResponseDto> getExamResult(@PathVariable @Valid Long id) throws CustomNotFoundException {
        ExamResultResponseDto result = studentExamService.getResult(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-all-result")
    public ResponseEntity<List<ExamResultResponseDto>> getAllExamResults() {
        List<ExamResultResponseDto> results = studentExamService.getAllResults();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/get-result-by-student/{studentId}")
    public ResponseEntity<List<ExamResultResponseDto>> getResultsByStudentId(@PathVariable @Valid Long studentId) {
        List<ExamResultResponseDto> results = studentExamService.getResultsByStudentId(studentId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/get-result-by-group/{groupId}")
    public ResponseEntity<List<ExamResultResponseDto>> getResultsByGroupId(@PathVariable @Valid Long groupId) {
        List<ExamResultResponseDto> results = studentExamService.getResultsByGroupId(groupId);
        return ResponseEntity.ok(results);
    }
}

