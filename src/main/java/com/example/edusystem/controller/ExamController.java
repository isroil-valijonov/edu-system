package com.example.edusystem.controller;

import com.example.edusystem.dto.ExamRequestDto;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.ExamResponseDto;
import com.example.edusystem.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("/create-exam")
    public ResponseEntity<ExamResponseDto> createExam(@RequestBody @Valid ExamRequestDto examRequestDto) throws CustomNotFoundException, BadRequestException {
        ExamResponseDto responseDto = examService.createExam(examRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @GetMapping("/get-all-exams")
    public ResponseEntity<List<ExamResponseDto>> getAllExams() {
        List<ExamResponseDto> responseDto = examService.getAllExams();
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/get-exam/{id}")
    public ResponseEntity<ExamResponseDto> getExam(@PathVariable @Valid Long id) throws CustomNotFoundException {
        ExamResponseDto responseDto = examService.getExam(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/update-exam/{id}")
    public ResponseEntity<ExamResponseDto> updateExam(@PathVariable Long id, @RequestBody @Valid ExamRequestDto examUpdateDto) throws CustomNotFoundException, BadRequestException {
        ExamResponseDto responseDto = examService.updateExam(id, examUpdateDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete-exam/{id}")
    public ResponseEntity<CommonResponse> deleteExam(@PathVariable @Valid Long id) throws CustomNotFoundException {
        CommonResponse response = examService.deleteExam(id);
        return ResponseEntity.ok(response);
    }
}

